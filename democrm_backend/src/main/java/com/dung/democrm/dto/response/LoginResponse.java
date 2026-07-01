package com.dung.democrm.dto.response;

import com.dung.democrm.common.enums.Role;
import com.dung.democrm.common.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private UserInfo user;

    @Getter
    @Builder
    public static class UserInfo {
        private Long id;
        private String employeeCode;
        private String fullName;
        private String email;
        private Role role;
        private UserStatus status;
    }
}
