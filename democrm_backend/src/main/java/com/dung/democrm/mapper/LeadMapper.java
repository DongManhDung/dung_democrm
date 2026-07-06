package com.dung.democrm.mapper;

import com.dung.democrm.dto.response.LeadResponse;
import com.dung.democrm.entity.Lead;
import org.springframework.stereotype.Component;

@Component
public final class LeadMapper {
    public LeadResponse toResponse(Lead lead){
        return LeadResponse.builder()
                .id(lead.getId())
                .customerId(lead.getCustomer() != null ? lead.getCustomer().getId() : null)
                .customerName(lead.getCustomer() != null ? lead.getCustomer().getName() : null)
                .ownerId(lead.getOwner() != null ? lead.getOwner().getId() : null)
                .ownerName(lead.getOwner() != null ? lead.getOwner().getFullName() : null)
                .ownerEmployeeCode(lead.getOwner() != null ? lead.getOwner().getEmployeeCode() : null)
                .status(lead.getLeadStatus())
                .source(lead.getSource())
                .priority(lead.getPriority())
                .assignAt(lead.getAssignedAt())
                .expiredAt(lead.getExpiredAt())
                .transferCount(lead.getTransferCount())
                .note(lead.getNote())
                .createdAt(lead.getCreatedAt())
                .updatedAt(lead.getUpdatedAt())
                .build();
    }
}
