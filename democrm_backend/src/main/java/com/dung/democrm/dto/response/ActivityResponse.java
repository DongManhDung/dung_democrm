package com.dung.democrm.dto.response;

import com.dung.democrm.common.enums.ActivityStatus;
import com.dung.democrm.common.enums.ActivityType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResponse {
    private Long id;

    private Long leadId;

    private Long createById;

    private String createdByName;

    private String createdByEmployeeCode;

    private ActivityType type;

    private ActivityStatus status;

    private String subject;

    private String description;

    private LocalDate dueDate;

    private LocalDateTime completedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
