package com.dung.democrm.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CustomerResponse {
    private Long id;
    private String name;
    private String company;
    private String phone;
    private String email;
    private Long ownerId;
    private String ownerName;
    private String ownerEmployeeCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
