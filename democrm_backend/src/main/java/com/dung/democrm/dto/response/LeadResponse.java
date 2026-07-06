package com.dung.democrm.dto.response;

import com.dung.democrm.common.enums.LeadPriority;
import com.dung.democrm.common.enums.LeadSource;
import com.dung.democrm.common.enums.LeadStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LeadResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long ownerId;
    private String ownerName;
    private String ownerEmployeeCode;
    private LeadStatus status;
    private LeadSource source;
    private LeadPriority priority;
    private LocalDate assignAt;
    private LocalDate expiredAt;
    private Integer transferCount;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
