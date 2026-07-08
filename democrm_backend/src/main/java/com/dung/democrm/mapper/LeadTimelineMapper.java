package com.dung.democrm.mapper;

import com.dung.democrm.dto.response.LeadTimelineResponse;
import com.dung.democrm.entity.LeadTimeline;
import org.springframework.stereotype.Component;

@Component
public class LeadTimelineMapper {
    public LeadTimelineResponse toResponse(LeadTimeline timeline){
        return LeadTimelineResponse.builder()
                .id(timeline.getId())
                .action(timeline.getAction())
                .description(timeline.getDescription())

                .performedById(timeline.getPerformedBy().getId())
                .performedByName(timeline.getPerformedBy().getFullName())
                .performedByEmployeeCode(timeline.getPerformedBy().getEmployeeCode())

                .createdAt(timeline.getCreatedAt())
                .build();
    }
}
