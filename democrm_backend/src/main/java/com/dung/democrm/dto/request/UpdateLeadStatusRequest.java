package com.dung.democrm.dto.request;

import com.dung.democrm.common.enums.LeadStatus;
import com.dung.democrm.common.enums.LostReason;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLeadStatusRequest {

    @NotNull(message = "Lead status is required.")
    private LeadStatus status;

    private LostReason lostReason;
}
