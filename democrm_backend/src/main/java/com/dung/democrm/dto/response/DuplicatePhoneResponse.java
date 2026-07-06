package com.dung.democrm.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DuplicatePhoneResponse {
    private boolean duplicated;
    private Long customerId;
    private String customerName;
    private String company;
    private Long ownerId;
    private String ownerName;
    private String ownerEmployeeCode;
}
