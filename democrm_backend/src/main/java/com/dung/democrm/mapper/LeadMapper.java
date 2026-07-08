package com.dung.democrm.mapper;

import com.dung.democrm.dto.response.LeadDetailResponse;
import com.dung.democrm.dto.response.LeadResponse;
import com.dung.democrm.entity.Lead;
import org.springframework.stereotype.Component;

@Component
public class LeadMapper {

    public LeadResponse toResponse(Lead lead){
        return LeadResponse.builder()
                .id(lead.getId())
                .customerId(lead.getCustomer().getId())
                .customerName(lead.getCustomer().getName())
                .ownerId(lead.getOwner().getId())
                .ownerName(lead.getOwner().getFullName())
                .ownerEmployeeCode(lead.getOwner().getEmployeeCode())
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

    public LeadDetailResponse toDetailResponse(Lead lead){
        return LeadDetailResponse.builder()
                .id(lead.getId())

                // Customer
                .customerId(lead.getCustomer().getId())
                .customerName(lead.getCustomer().getName())
                .customerCompany(lead.getCustomer().getCompany())
                .customerPhone(lead.getCustomer().getPhone())
                .customerEmail(lead.getCustomer().getEmail())

                // Sales Owner
                .ownerId(lead.getOwner().getId())
                .ownerName(lead.getOwner().getFullName())
                .ownerEmployeeCode(lead.getOwner().getEmployeeCode())

                // Team Owner
                .teamOwnerId(lead.getTeamOwner().getId())
                .teamOwnerName(lead.getTeamOwner().getFullName())
                .teamOwnerEmployeeCode(lead.getTeamOwner().getEmployeeCode())

                // Lead
                .leadStatus(lead.getLeadStatus())
                .leadSource(lead.getSource())
                .priority(lead.getPriority())
                .lostReason(lead.getLostReason())
                .note(lead.getNote())
                .assignedAt(lead.getAssignedAt())
                .expiredAt(lead.getExpiredAt())
                .transferCount(lead.getTransferCount())

                .createdAt(lead.getCreatedAt())
                .updatedAt(lead.getUpdatedAt())

                .build();
    }
}
