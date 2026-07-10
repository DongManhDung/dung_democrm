package com.dung.democrm.service.impl;

import com.dung.democrm.common.enums.OpportunityStatus;
import com.dung.democrm.common.exception.BadRequestException;
import com.dung.democrm.common.exception.DuplicateResourceException;
import com.dung.democrm.common.exception.ResourceNotFoundException;
import com.dung.democrm.dto.request.OpportunityRequest;
import com.dung.democrm.dto.response.OpportunityResponse;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.Opportunity;
import com.dung.democrm.mapper.OpportunityMapper;
import com.dung.democrm.repository.LeadRepository;
import com.dung.democrm.repository.OpportunityRepository;
import com.dung.democrm.service.OpportunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final LeadRepository leadRepository;
    private final OpportunityMapper opportunityMapper;

    @Override
    public Page<OpportunityResponse> getAll(Pageable pageable) {
        return opportunityRepository.findAllByActiveTrue(pageable).map(opportunityMapper::toResponse);
    }

    @Override
    public OpportunityResponse getById(Long id) {
        return opportunityMapper.toResponse(getValidOpportunity(id));
    }

    @Override
    public OpportunityResponse create(OpportunityRequest request) {

        Lead lead = getValidLead(request.getLeadId());

        if (opportunityRepository.existsByLeadId(lead.getId())){
            throw new DuplicateResourceException("Opportunity already exists for this lead.");
        }

        Opportunity opportunity = new Opportunity();

        opportunity.setLead(lead);
        mapRequest(opportunity, request);

        return opportunityMapper.toResponse(opportunityRepository.save(opportunity));
    }

    @Override
    public OpportunityResponse update(Long id, OpportunityRequest request) {

        Opportunity opportunity = getValidOpportunity(id);

        mapRequest(opportunity, request);

        return opportunityMapper.toResponse(opportunityRepository.save(opportunity));
    }

    @Override
    public void delete(Long id) {
        Opportunity opportunity = getValidOpportunity(id);

        opportunity.setActive(false);

        opportunity.setDeletedAt(LocalDateTime.now());

        opportunityRepository.save(opportunity);
    }


    // Helper
    private Lead getValidLead(Long id){
        return leadRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found."));
    }

    private Opportunity getValidOpportunity(Long id){
        return opportunityRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity not found."));
    }

    private void mapRequest(Opportunity opportunity, OpportunityRequest request){
        opportunity.setAmount(request.getAmount());
        opportunity.setProbability(request.getProbability());
        opportunity.setStatus(request.getStatus());
        opportunity.setExpectedCloseDate(request.getExpectedCloseDate());
        opportunity.setLostReason(request.getLostReason());

        updateClosedAt(opportunity);
    }

    private void updateClosedAt(Opportunity opportunity){
        if (opportunity.getStatus() == OpportunityStatus.WON || opportunity.getStatus() == OpportunityStatus.LOST){
            if (opportunity.getClosedAt() == null){
                opportunity.setClosedAt(LocalDateTime.now());
            }
        } else {
            opportunity.setClosedAt(null);
            opportunity.setLostReason(null);
        }
    }
}
