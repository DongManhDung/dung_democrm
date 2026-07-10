package com.dung.democrm.dto.response;

import com.dung.democrm.common.enums.ActivityStatus;
import com.dung.democrm.common.enums.ActivityType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ActivityTimelineResponse {

    // Activity
    private Long id;

    private ActivityType type;

    private ActivityStatus status;

    private String subject;

    // Lead
    private Long leadId;

    private Long customerId;

    private String customerName;

    // Sales
    private Long createdById;

    private String createdByName;

    // Schedule
    private LocalDate dueDate;

    private LocalDateTime completeAt;

    // Timeline
    private LocalDateTime createdAt;
}
