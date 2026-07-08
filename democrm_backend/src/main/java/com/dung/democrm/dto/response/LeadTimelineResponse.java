package com.dung.democrm.dto.response;

import com.dung.democrm.common.enums.TimelineAction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LeadTimelineResponse {
    private Long id;
    private TimelineAction action;
    private String description;
    private Long performedById;
    private String performedByName;
    private String performedByEmployeeCode;
    private LocalDateTime createdAt;
}
