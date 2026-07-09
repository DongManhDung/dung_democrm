package com.dung.democrm.mapper;

import com.dung.democrm.dto.response.ActivityResponse;
import com.dung.democrm.entity.Activity;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {
    public ActivityResponse toResponse(Activity activity){
        return ActivityResponse.builder()
                .id(activity.getId())

                .leadId(activity.getLead().getId())

                .createById(activity.getCreatedBy().getId())
                .createdByName(activity.getCreatedBy().getFullName())
                .createdByEmployeeCode(activity.getCreatedBy().getEmployeeCode())

                .type(activity.getType())
                .status(activity.getStatus())

                .subject(activity.getSubject())
                .description(activity.getDescription())

                .dueDate(activity.getDueDate())
                .completedAt(activity.getCompletedAt())

                .createdAt(activity.getCreatedAt())
                .updatedAt(activity.getUpdatedAt())
                .build();
    }
}
