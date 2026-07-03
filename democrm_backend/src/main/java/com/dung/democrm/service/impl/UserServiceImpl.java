package com.dung.democrm.service.impl;

import com.dung.democrm.common.enums.Role;
import com.dung.democrm.common.exception.BadRequestException;
import com.dung.democrm.common.exception.DuplicateResourceException;
import com.dung.democrm.common.exception.ResourceNotFoundException;
import com.dung.democrm.dto.request.CreateUserRequest;
import com.dung.democrm.dto.request.UpdateUserRequest;
import com.dung.democrm.dto.response.UserDetailResponse;
import com.dung.democrm.dto.response.UserResponse;
import com.dung.democrm.entity.User;
import com.dung.democrm.mapper.UserMapper;
import com.dung.democrm.repository.UserRepository;
import com.dung.democrm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponse> getAll(Pageable pageable) {
        return userRepository.findByActiveTrue(pageable).map(UserMapper::toResponse);
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
}
