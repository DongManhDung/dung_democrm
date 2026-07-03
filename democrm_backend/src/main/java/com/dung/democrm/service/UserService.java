package com.dung.democrm.service;

import com.dung.democrm.dto.request.*;
import com.dung.democrm.dto.response.ManagerResponse;
import com.dung.democrm.dto.response.TeamMemberResponse;
import com.dung.democrm.dto.response.UserDetailResponse;
import com.dung.democrm.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    // Danh sách User
    Page<UserResponse> getAll(UserSearchRequest request, Pageable pageable);

    // Chi tiết User
    UserDetailResponse getById(Long id);

    // Tạo user
    UserDetailResponse create(CreateUserRequest request);

    // Cập nhật
    UserDetailResponse update(Long id, UpdateUserRequest request);

    // Soft delete
    void delete(Long id);

    void resetPassword(Long id, ResetPasswordRequest request);

    // Change Status
    void changeStatus(Long id, ChangeUserStatusRequest request, Authentication authentication);

    // Get My Team
    List<TeamMemberResponse> getMyTeam(Authentication authentication);

    // Admin get team by Manager
    List<TeamMemberResponse> getTeamByManager(Long managerId);

    // All Managers
    List<ManagerResponse> getAllManagers();
}
