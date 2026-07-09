package com.dung.democrm.dto.response;

import com.dung.democrm.common.enums.LeadPriority;
import com.dung.democrm.common.enums.LeadStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DuplicateLeadResponse {
    private Long leadId;
    private Long customerId;
    private String customerName;
    private Long ownerId;
    private String ownerName;
    private String ownerEmployeeCode;
    private LeadStatus status;
    private LeadPriority priority;
    private LocalDate assignedAt;
    private LocalDate expiredAt;
    private Integer transferCount;
}
