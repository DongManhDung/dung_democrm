package com.dung.democrm.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeadAssignRequest {
    @NotNull(message = "Owner ID is required.")
    private Long ownerId;
}
