package com.dung.democrm.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSearchRequest {
    private String name;
    private String phone;
    private String email;
    private Long ownerId;
    private String company;
}
