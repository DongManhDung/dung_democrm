package com.dung.democrm.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerOwnerRequest {
    @NotNull(message = "Owner id is required.")
    private Long ownerId;
}
