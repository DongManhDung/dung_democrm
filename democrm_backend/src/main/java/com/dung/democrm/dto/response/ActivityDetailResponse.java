package com.dung.democrm.dto.response;

import com.dung.democrm.common.enums.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ActivityDetailResponse {

    // Activity
    private Long id;

    private ActivityType type;

    private ActivityStatus status;

    private String subject;

    private String description;

    private LocalDate dueDate;

    private LocalDateTime completedAt;

    // Lead
    private Long leadId;

    private LeadStatus leadStatus;

    private LeadSource leadSource;

    private LeadPriority leadPriority;

    // Customer
    private Long customerId;

    private String customerName;

    private String customerCompany;

    // Created By
    private Long createdById;

    private String createdByName;

    private String createdByEmployeeCode;

    // Log
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
