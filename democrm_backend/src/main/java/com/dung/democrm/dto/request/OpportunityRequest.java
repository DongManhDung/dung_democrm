package com.dung.democrm.dto.request;

import com.dung.democrm.common.enums.OpportunityStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class OpportunityRequest {

    @NotNull(message = "Lead is required.")
    private Long leadId;

    @NotNull(message = "Amount is required.")
    private BigDecimal amount;

    @NotNull(message = "Probability is required.")
    @Min(value = 0, message = "Probability must be at least 0.")
    @Max(value = 100, message = "Probability must not exceed 100.")
    private Integer probability;

    @NotNull(message = "Status is required.")
    private OpportunityStatus status;

    @NotNull(message = "Expected close date is required.")
    private LocalDate expectedCloseDate;

    private String lostReason;
}
