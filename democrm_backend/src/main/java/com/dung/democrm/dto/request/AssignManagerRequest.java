package com.dung.democrm.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignManagerRequest {
    @NotNull(message = "Manager id is required.")
    private Long managerId;
}
