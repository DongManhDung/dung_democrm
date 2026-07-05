package com.dung.democrm.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest {
    @NotBlank(message = "Customer name is required.")
    private String name;

    private String company;

    @NotBlank(message = "Phone is required.")
    private String phone;

    @Email(message = "Invalid email format.")
    private String email;

    @NotNull(message = "Owner is required.")
    private Long ownerId;

}
