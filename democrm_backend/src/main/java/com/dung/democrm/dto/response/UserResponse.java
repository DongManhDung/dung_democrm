package com.dung.democrm.dto.response;

import com.dung.democrm.common.enums.Role;
import com.dung.democrm.common.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String employeeCode;
    private String fullName;
    private String email;
    private Role role;
    private UserStatus status;
}
