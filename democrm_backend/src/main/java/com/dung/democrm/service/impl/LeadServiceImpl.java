package com.dung.democrm.service.impl;

import com.dung.democrm.common.constant.LeadConstants;
import com.dung.democrm.common.enums.LeadStatus;
import com.dung.democrm.common.enums.Role;
import com.dung.democrm.common.enums.UserStatus;
import com.dung.democrm.common.exception.BadRequestException;
import com.dung.democrm.common.exception.ResourceNotFoundException;
import com.dung.democrm.dto.request.LeadRequest;
import com.dung.democrm.dto.request.LeadSearchRequest;
import com.dung.democrm.dto.response.LeadDetailResponse;
import com.dung.democrm.dto.response.LeadResponse;
import com.dung.democrm.entity.Customer;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.User;
import com.dung.democrm.mapper.LeadMapper;
import com.dung.democrm.repository.CustomerRepository;
import com.dung.democrm.repository.LeadRepository;
import com.dung.democrm.repository.UserRepository;
import com.dung.democrm.service.LeadService;
import com.dung.democrm.user.specification.LeadSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final LeadMapper leadMapper;

    @Override
    public Page<LeadResponse> getAllLeads(Pageable pageable) {
        return searchLeads(new LeadSearchRequest(), pageable);
    }

    @Override
    public Page<LeadResponse> searchLeads(LeadSearchRequest request, Pageable pageable) {
        return leadRepository.findAll(LeadSpecification.search(request), pageable)
                .map(leadMapper::toResponse);
    }

    @Override
    public LeadResponse getLeadById(Long id) {
        return leadMapper.toResponse(getValidLead(id));
    }

    @Override
    public LeadResponse createLead(LeadRequest request) {

        Customer customer = getValidCustomer(request.getCustomerId());

        User owner = getValidOwner(request.getOwnerId());

        if(leadRepository.existsByCustomerIdAndLeadStatusInAndActiveTrue(customer.getId(), LeadConstants.ACTIVE_LEAD_STATUSES)){
            throw new BadRequestException("Customer already has an active lead.");
        }

        Lead lead = new Lead();

        lead.setCustomer(customer);
        lead.setOwner(owner);
        lead.setTeamOwner(owner.getManager());
        lead.setLeadStatus(LeadStatus.NEW);
        lead.setSource(request.getLeadSource());
        lead.setPriority(request.getPriority());
        lead.setNote(request.getNote());

        LocalDate assignAt = LocalDate.now();
        lead.setAssignedAt(assignAt);
        lead.setExpiredAt(assignAt.plusDays(15));

        lead.setTransferCount(0);

        return leadMapper.toResponse(leadRepository.save(lead));
    }

    @Override
    public LeadResponse updateLead(Long id, LeadRequest request) {

        Lead lead = getValidLead(id);

        Customer customer = getValidCustomer(request.getCustomerId());

        User owner = getValidOwner(request.getOwnerId());

        if(!lead.getCustomer().getId().equals(customer.getId())
                && leadRepository.existsByCustomerIdAndLeadStatusInAndActiveTrue(customer.getId(), LeadConstants.ACTIVE_LEAD_STATUSES)
        ){
            throw new BadRequestException("Customer already has an active lead");
        }

        lead.setCustomer(customer);
        lead.setOwner(owner);
        lead.setTeamOwner(owner.getManager());
        lead.setSource(request.getLeadSource());
        lead.setPriority(request.getPriority());
        lead.setNote(request.getNote());

        return leadMapper.toResponse(leadRepository.save(lead));
    }

    @Override
    public void deleteLead(Long id) {
        Lead lead = getValidLead(id);
        lead.setActive(false);
        lead.setDeletedAt(LocalDateTime.now());
        leadRepository.save(lead);
    }

    @Override
    public LeadDetailResponse getLeadDetail(Long id) {
        Lead lead = leadRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found."));
        return leadMapper.toDetailResponse(lead);
    }

    private Lead getValidLead(Long id){
        return leadRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found."));
    }

    private Customer getValidCustomer(Long customerId){
        return customerRepository.findByIdAndActiveTrue(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));
    }

    private User getValidOwner(Long ownerId){
        User owner = userRepository.findByIdAndActiveTrue(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found."));

        if(owner.getRole() != Role.SALES){
            throw new BadRequestException("Owner must be a sales.");
        }

        if(owner.getStatus() != UserStatus.ACTIVE){
            throw new BadRequestException("Owner must be active.");
        }
        return owner;
    }
}
