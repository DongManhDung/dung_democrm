package com.dung.democrm.dto.response;

import com.dung.democrm.common.enums.OpportunityStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OpportunityResponse {

    private Long id;

    private Long leadId;

    private Long customerId;
    private String customerName;

    private Long ownerId;
    private String ownerName;

    private BigDecimal amount;

    private Integer probability;

    private OpportunityStatus status;

    private LocalDate expectedCloseDate;

    private LocalDateTime closedAt;

    private String lostReason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
