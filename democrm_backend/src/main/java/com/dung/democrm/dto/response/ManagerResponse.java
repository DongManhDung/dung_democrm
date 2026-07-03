package com.dung.democrm.dto.response;

import com.dung.democrm.common.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ManagerResponse {
    private Long id;
    private String employeeCode;
    private String fullName;
    private String email;
    private String phone;
    private UserStatus status;
    private Long teamSize;
}
