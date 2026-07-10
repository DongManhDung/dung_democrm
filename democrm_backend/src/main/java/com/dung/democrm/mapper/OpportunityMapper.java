package com.dung.democrm.mapper;

import com.dung.democrm.dto.response.OpportunityResponse;
import com.dung.democrm.entity.Opportunity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OpportunityMapper {

    @Mapping(target = "leadId", source = "lead.id")

    @Mapping(target = "customerId", source = "lead.customer.id")
    @Mapping(target = "customerName", source = "lead.customer.name")

    @Mapping(target = "ownerId", source = "lead.owner.id")
    @Mapping(target = "ownerName", source = "lead.owner.fullName")

    OpportunityResponse toResponse(Opportunity opportunity);
}
