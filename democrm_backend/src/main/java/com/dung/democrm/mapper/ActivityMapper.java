package com.dung.democrm.mapper;

import com.dung.democrm.dto.response.ActivityDetailResponse;
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

    public ActivityDetailResponse toDetailResponse(Activity activity){
        return ActivityDetailResponse.builder()
                // Activity
                .id(activity.getId())
                .type(activity.getType())
                .status(activity.getStatus())
                .subject(activity.getSubject())
                .description(activity.getDescription())
                .dueDate(activity.getDueDate())
                .completedAt(activity.getCompletedAt())

                // Lead
                .leadId(activity.getLead().getId())
                .leadStatus(activity.getLead().getLeadStatus())
                .leadSource(activity.getLead().getSource())
                .leadPriority(activity.getLead().getPriority())

                // Customer
                .customerId(activity.getLead().getCustomer().getId())
                .customerName(activity.getLead().getCustomer().getName())
                .customerCompany(activity.getLead().getCustomer().getCompany())

                // Created By
                .createdById(activity.getCreatedBy().getId())
                .createdByName(activity.getCreatedBy().getFullName())
                .createdByEmployeeCode(activity.getCreatedBy().getEmployeeCode())

                // Log
                .createdAt(activity.getCreatedAt())
                .updatedAt(activity.getUpdatedAt())
                .build();
    }
}
