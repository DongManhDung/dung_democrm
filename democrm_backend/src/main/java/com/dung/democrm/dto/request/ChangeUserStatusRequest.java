package com.dung.democrm.dto.request;

import com.dung.democrm.common.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserStatusRequest {
    @NotNull(message = "Status is required.")
    private UserStatus status;
}
