package com.dung.democrm.dto.request;

import com.dung.democrm.common.enums.Role;
import com.dung.democrm.common.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchRequest {
    private String keyword;
    private Role role;
    private UserStatus status;
    private Long managerId;
}
