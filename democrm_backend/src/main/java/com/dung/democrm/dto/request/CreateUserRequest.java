package com.dung.democrm.dto.request;

import com.dung.democrm.common.enums.Role;
import com.dung.democrm.common.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    @NotBlank(message = "Full name is required.")
    private String fullName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @Pattern(regexp = "^(0|\\+84)\\d{9}$", message = "Invalid phone number.")
    private String phone;

    @NotBlank(message = "Employee code is required.")
    private String employeeCode;

    @NotNull(message = "Role is required.")
    private Role role;

    @NotNull(message = "Status is required.")
    private UserStatus status;

    private Long managerId; // Optional field for manager ID
}
