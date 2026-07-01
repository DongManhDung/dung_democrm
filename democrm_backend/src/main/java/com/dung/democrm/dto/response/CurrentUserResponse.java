package com.dung.democrm.dto.response;

import com.dung.democrm.common.enums.Role;
import com.dung.democrm.common.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentUserResponse {
    private Long id;
    private String employeeCode;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
    private UserStatus status;
    private Long managerId;
    private String managerName;
}
