package com.dung.democrm.dto.response;

import com.dung.democrm.common.enums.Role;
import com.dung.democrm.common.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDetailResponse {
    private Long id;
    private String employeeCode;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
    private UserStatus status;
    private Long managerId;
    private String managerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
