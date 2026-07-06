package com.dung.democrm.dto.request;

import com.dung.democrm.common.enums.LeadPriority;
import com.dung.democrm.common.enums.LeadSource;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeadRequest {
    @NotNull(message = "Customer is required.")
    private Long customerId;

    @NotNull(message = "Owner is required.")
    private Long ownerId;

    @NotNull(message = "Lead source is required.")
    private LeadSource leadSource;

    @NotNull(message = "Lead priority is required.")
    private LeadPriority priority;

    @Size(max = 500, message = "Note must not exceed 500 characters.")
    private String note;
}
