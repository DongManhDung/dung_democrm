package com.dung.democrm.dto.response;

import com.dung.democrm.common.enums.LeadPriority;
import com.dung.democrm.common.enums.LeadSource;
import com.dung.democrm.common.enums.LeadStatus;
import com.dung.democrm.common.enums.LostReason;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LeadDetailResponse {
    private Long id;

    private Long customerId;
    private String customerName;
    private String customerCompany;
    private String customerPhone;
    private String customerEmail;

    // Sales owner
    private Long ownerId;
    private String ownerName;
    private String ownerEmployeeCode;

    // Manager
    private Long teamOwnerId;
    private String teamOwnerName;
    private String teamOwnerEmployeeCode;

    // Lead
    private LeadStatus leadStatus;
    private LeadSource leadSource;
    private LeadPriority priority;
    private LostReason lostReason;

    private String note;

    private LocalDate assignedAt;
    private LocalDate expiredAt;

    private Integer transferCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
