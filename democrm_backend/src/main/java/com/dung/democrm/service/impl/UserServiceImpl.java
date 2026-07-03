package com.dung.democrm.service.impl;

import com.dung.democrm.common.enums.Role;
import com.dung.democrm.common.enums.UserStatus;
import com.dung.democrm.common.exception.BadRequestException;
import com.dung.democrm.common.exception.DuplicateResourceException;
import com.dung.democrm.common.exception.ResourceNotFoundException;
import com.dung.democrm.dto.request.*;
import com.dung.democrm.dto.response.TeamMemberResponse;
import com.dung.democrm.dto.response.UserDetailResponse;
import com.dung.democrm.dto.response.UserResponse;
import com.dung.democrm.entity.User;
import com.dung.democrm.mapper.UserMapper;
import com.dung.democrm.repository.UserRepository;
import com.dung.democrm.service.RefreshTokenService;
import com.dung.democrm.service.UserService;
import com.dung.democrm.user.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @Override
    public Page<UserResponse> getAll(UserSearchRequest request, Pageable pageable) {
        Specification<User> specification = UserSpecification.filter(request);
        return userRepository.findAll(specification,pageable).map(UserMapper::toResponse);
    }

    @Override
    public UserDetailResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        return UserMapper.toDetailResponse(user);
    }

    @Override
    public UserDetailResponse create(CreateUserRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("Email already exists.");
        }

        if(userRepository.existsByEmployeeCode(request.getEmployeeCode())){
            throw new DuplicateResourceException("Employee code already exists.");
        }

        if(request.getPhone() != null && userRepository.existsByPhone(request.getPhone())){
            throw new DuplicateResourceException("Phone already exists.");
        }

        User manager = null;

        if(request.getManagerId() != null){
            manager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found."));

            if(manager.getRole() != Role.MANAGER){
                throw new BadRequestException("Selected user is not a manager.");
            }
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setEmployeeCode(request.getEmployeeCode());
        user.setRole(request.getRole());
        user.setStatus(request.getStatus());

        if(request.getRole() != Role.SALES){
            manager = null;
        }
        user.setManager(manager);

        userRepository.save(user);

        return UserMapper.toDetailResponse(user);
    }

    @Override
    public UserDetailResponse update(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if(!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("Email already exists.");
        }



        if(request.getPhone() != null
                && !request.getPhone().equals(user.getPhone())
                && userRepository.existsByPhone(request.getPhone())
        ){
            throw new DuplicateResourceException("Phone already exists.");
        }

        User manager = null;

        if(request.getManagerId() != null){
            manager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found."));

            if(manager.getRole() != Role.MANAGER){
                throw new BadRequestException("Selected user is not a manager.");
            }
        }

        if(manager != null && manager.getId().equals(id)){
            throw new BadRequestException("User cannot manage themselves.");
        }

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setStatus(request.getStatus());

        if(request.getRole() != Role.SALES){
            manager = null;
        }
        user.setManager(manager);

        userRepository.save(user);

        return UserMapper.toDetailResponse(user);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        userRepository.softDelete(user);
    }

    @Override
    public void resetPassword(Long id, ResetPasswordRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new BadRequestException("Password and confirm password do not match.");
        }

        if(passwordEncoder.matches(request.getNewPassword(), user.getPassword())){
            throw new BadRequestException("New password must be different from the current password.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        refreshTokenService.revokeAll(user);

        userRepository.save(user);
    }

    @Override
    public void changeStatus(Long id, ChangeUserStatusRequest request, Authentication authentication) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found."));

        // Rule 1: Không được đổi sang trạng thái hiện tại
        if(user.getStatus() == request.getStatus()){
            throw new BadRequestException("User is already " + request.getStatus() + ".");
        }

        // Rule 2: Admin không được tự khóa chính mình
        if(authentication != null
                && request.getStatus() == UserStatus.LOCKED
                && authentication.getName().equals(user.getEmail())){
            throw new BadRequestException("You cannot lock your own account.");
        }

        // Rule 3: Kiểm tra chuyển trạng thái hợp lệ
        switch (user.getStatus()){
            case ACTIVE -> {
                // ACTIVE -> ON_LEAVE/LOCKED/RESIGNED
                if(request.getStatus() != UserStatus.ON_LEAVE
                        && request.getStatus() != UserStatus.LOCKED
                        && request.getStatus() != UserStatus.RESIGNED
                ){
                    throw new BadRequestException("Cannot change status from ACTIVE to " + request.getStatus() + ".");
                }
            }

            case ON_LEAVE -> {
                // ON_LEAVE -> ACTIVE/LOCKED/RESIGNED
                if(request.getStatus() != UserStatus.ACTIVE
                        && request.getStatus() != UserStatus.LOCKED
                        && request.getStatus() != UserStatus.RESIGNED
                ){
                    throw new BadRequestException("Cannot change status from ON_LEAVE to " + request.getStatus() + ".");
                }
            }

            case LOCKED -> {
                // LOCKED -> ACTIVE / RESIGNED
                if(request.getStatus() != UserStatus.ACTIVE && request.getStatus() != UserStatus.RESIGNED){
                    throw new BadRequestException("Cannot change status from LOCKED to " + request.getStatus() + ".");
                }
            }

            case RESIGNED -> {
                // Không cho mở gì hết
                throw new BadRequestException("A resigned user cannot change status.");
            }
        }
        user.setStatus(request.getStatus());
        userRepository.save(user);

        // Rule 4: Thu hồi toàn bộ Refresh Token nếu User bị chuyển sang LOCKED hoặc RESIGNED
        if(request.getStatus() == UserStatus.LOCKED || request.getStatus() == UserStatus.RESIGNED){
            refreshTokenService.revokeAll(user);
        }

    }

    @Override
    public List<TeamMemberResponse> getMyTeam(Authentication authentication) {
        User manager = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if(manager.getRole() != Role.MANAGER){
            throw new BadRequestException("Only manager can view their team.");
        }

        return userRepository.findByManagerIdAndActiveTrue(manager.getId())
                .stream().
                map(UserMapper::toTeamMemberResponse)
                .toList();
    }

    @Override
    public List<TeamMemberResponse> getTeamByManager(Long managerId) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found."));

        if(manager.getRole() != Role.MANAGER){
            throw new BadRequestException("Selected user is not a manager");
        }

        if(manager.getStatus() == UserStatus.RESIGNED){
            throw new BadRequestException("Manager has resigned.");
        }

        return userRepository.findByManagerIdAndActiveTrue(managerId).stream()
                .map(UserMapper::toTeamMemberResponse)
                .toList();
    }
}
