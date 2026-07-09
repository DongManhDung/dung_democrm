package com.dung.democrm.service.impl;

import com.dung.democrm.common.constant.LeadConstants;
import com.dung.democrm.common.enums.LeadStatus;
import com.dung.democrm.common.enums.Role;
import com.dung.democrm.common.enums.TimelineAction;
import com.dung.democrm.common.enums.UserStatus;
import com.dung.democrm.common.exception.BadRequestException;
import com.dung.democrm.common.exception.ForbiddenException;
import com.dung.democrm.common.exception.ResourceNotFoundException;
import com.dung.democrm.dto.request.LeadAssignRequest;
import com.dung.democrm.dto.request.LeadRequest;
import com.dung.democrm.dto.request.LeadSearchRequest;
import com.dung.democrm.dto.request.UpdateLeadStatusRequest;
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
import com.dung.democrm.service.LeadTimelineService;
import com.dung.democrm.user.specification.LeadSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final LeadMapper leadMapper;
    private final LeadTimelineService leadTimelineService;

    @Override
    public Page<LeadResponse> getAllLeads(Pageable pageable) {
        return searchLeads(new LeadSearchRequest(), pageable);
    }

    @Override
    public Page<LeadResponse> searchLeads(LeadSearchRequest request, Pageable pageable) {
        User currentUser = getCurrentUser();
        return leadRepository.findAll(LeadSpecification.search(request, currentUser), pageable)
                .map(leadMapper::toResponse);
    }

    @Override
    public LeadResponse getLeadById(Long id) throws AccessDeniedException {
        return leadMapper.toResponse(getAccessibleLeadForRead(id));
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

        Lead savedLead = leadRepository.save(lead);

        leadTimelineService.saveTimeLine(savedLead, TimelineAction.CREATED,"Lead created.",getCurrentUser());

        return leadMapper.toResponse(savedLead);
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

        Lead updatedLead = leadRepository.save(lead);

        leadTimelineService.saveTimeLine(updatedLead, TimelineAction.UPDATED, "Lead updated.", getCurrentUser());

        return leadMapper.toResponse(updatedLead);
    }

    @Override
    public void deleteLead(Long id) {
        Lead lead = getValidLead(id);
        lead.setActive(false);
        lead.setDeletedAt(LocalDateTime.now());
        leadRepository.save(lead);
        leadTimelineService.saveTimeLine(lead, TimelineAction.DELETED, "Lead deleted.", getCurrentUser());
    }

    @Override
    public LeadDetailResponse getLeadDetail(Long id) throws AccessDeniedException {
        return leadMapper.toDetailResponse(getAccessibleLeadForRead(id));
    }

    @Override
    public LeadResponse assignLead(Long id, LeadAssignRequest request) {
        Lead lead = getValidLead(id);

        validateLeadNotExpired(lead);

        User owner = getValidOwner(request.getOwnerId());

        if(lead.getOwner().getId().equals(request.getOwnerId())){
            throw new BadRequestException("Lead is already assigned to this owner.");
        }

        lead.setOwner(owner);
        lead.setTeamOwner(owner.getManager());

        LocalDate assignedAt = LocalDate.now();
        lead.setAssignedAt(assignedAt);
        lead.setExpiredAt(assignedAt.plusDays(15));

        Lead updatedLead = leadRepository.save(lead);

        leadTimelineService.saveTimeLine(
                updatedLead,
                TimelineAction.ASSIGNED,
                String.format(
                        "Lead assigned to %s (%s).",
                        owner.getFullName(),
                        owner.getEmployeeCode()
                ),
                getCurrentUser()
        );

        return leadMapper.toResponse(updatedLead);
    }

    @Override
    public LeadResponse transferLead(Long id, LeadAssignRequest request) {
        Lead lead = getValidLead(id);

        validateLeadNotExpired(lead);

        User newOwner = getValidOwner(request.getOwnerId());

        if(lead.getOwner().getId().equals(newOwner.getId())){
            throw new BadRequestException("Lead is already assigned to this owner.");
        }

        User oldOwner = lead.getOwner();

        lead.setOwner(newOwner);
        lead.setTeamOwner(newOwner.getManager());

        LocalDate assignedAt = LocalDate.now();
        lead.setAssignedAt(assignedAt);
        lead.setExpiredAt(assignedAt.plusDays(15));

        lead.setTransferCount(lead.getTransferCount() + 1); // Tăng khi transfer

        Lead updatedLead = leadRepository.save(lead);

        leadTimelineService.saveTimeLine(
                updatedLead,
                TimelineAction.TRANSFERRED,
                String.format(
                        "Lead transferred from %s (%s) to %s (%s).",
                        oldOwner.getFullName(),
                        oldOwner.getEmployeeCode(),
                        newOwner.getFullName(),
                        newOwner.getEmployeeCode()
                ),
                getCurrentUser()
        );
        return leadMapper.toResponse(updatedLead);
    }

    @Override
    public LeadResponse updateLeadStatus(Long id, UpdateLeadStatusRequest request) {
        Lead lead = getValidLead(id);

        validateLeadNotExpired(lead);

        LeadStatus oldStatus = lead.getLeadStatus();
        LeadStatus newStatus = request.getStatus();

        if(oldStatus == newStatus){
            throw new BadRequestException("Lead is already in this status");
        }

        if(newStatus == LeadStatus.LOST){
            if (request.getLostReason() == null){
                throw new BadRequestException("Lost reason is required when lead status is LOST.");
            }
            lead.setLostReason(request.getLostReason());
        } else {
            lead.setLostReason(null);
        }

        lead.setLeadStatus(newStatus);

        Lead updatedLead = leadRepository.save(lead);

        String description;

        if(newStatus == LeadStatus.LOST){
            description = String.format(
                    "Lead status changed from %s to %s. Reason: %s.",
                    oldStatus,
                    newStatus,
                    request.getLostReason()
            );
        } else {
            description = String.format(
                    "Lead status changed from %s to %s",
                    oldStatus,
                    newStatus
            );
        }

        leadTimelineService.saveTimeLine(
                updatedLead,
                TimelineAction.STATUS_CHANGED,
                description,
                getCurrentUser()
        );

        return leadMapper.toResponse(updatedLead);
    }

    private Lead getValidLead(Long id){
        return leadRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found."));
    }

    private Lead getAccessibleLeadForRead(Long id) throws AccessDeniedException {
        Lead lead = getValidLead(id);

        User currentUser = getCurrentUser();

        if(currentUser.getRole() == Role.ADMIN){
            return lead;
        }

        if(currentUser.getRole() == Role.MANAGER && lead.getTeamOwner().getId().equals(currentUser.getId())){
            return lead;
        }

        if (currentUser.getRole() == Role.SALES && lead.getOwner().getId().equals(currentUser.getId())){
            return lead;
        }

        throw new AccessDeniedException("You do not have permission to access this lead.");
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

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    private boolean isLeadExpired(Lead lead){
        return lead.getExpiredAt() != null && lead.getExpiredAt().isBefore(LocalDate.now());
    }

    private void validateLeadNotExpired(Lead lead){
        if (isLeadExpired(lead)){
            throw new BadRequestException("Lead has expired. Please assign or transfer the lead before continuing.");
        }
    }
}
