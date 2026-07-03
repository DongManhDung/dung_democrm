package com.dung.democrm.mapper;

import com.dung.democrm.dto.response.CurrentUserResponse;
import com.dung.democrm.dto.response.UserDetailResponse;
import com.dung.democrm.dto.response.UserResponse;
import com.dung.democrm.entity.User;

public final class UserMapper {
    private UserMapper() {
        // Private constructor to prevent instantiation
    }

    public static UserResponse toResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .employeeCode(user.getEmployeeCode())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    public static UserDetailResponse toDetailResponse(User user){
        return UserDetailResponse.builder()
                .id(user.getId())
                .employeeCode(user.getEmployeeCode())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .managerId(
                        user.getManager() != null ? user.getManager().getId() : null
                )
                .managerName(
                        user.getManager() != null ? user.getManager().getFullName() : null
                )
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static CurrentUserResponse toCurrentUserResponse(User user){
        return CurrentUserResponse.builder()
                .id(user.getId())
                .employeeCode(user.getEmployeeCode())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .managerId(
                        user.getManager() != null ? user.getManager().getId() : null
                )
                .managerName(
                        user.getManager() != null ? user.getManager().getFullName() : null
                )
                .build();
    }
}
