package com.dung.democrm.service;

import com.dung.democrm.dto.request.LeadAssignRequest;
import com.dung.democrm.dto.request.LeadRequest;
import com.dung.democrm.dto.request.LeadSearchRequest;
import com.dung.democrm.dto.request.UpdateLeadStatusRequest;
import com.dung.democrm.dto.response.LeadDetailResponse;
import com.dung.democrm.dto.response.LeadResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;

public interface LeadService {
    Page<LeadResponse> getAllLeads(Pageable pageable);
    Page<LeadResponse> searchLeads(LeadSearchRequest request, Pageable pageable);
    LeadResponse getLeadById(Long id) throws AccessDeniedException;
    LeadResponse createLead(LeadRequest request);
    LeadResponse updateLead(Long id, LeadRequest request);
    void deleteLead(Long id);
    LeadDetailResponse getLeadDetail(Long id) throws AccessDeniedException;
    LeadResponse assignLead(Long id, LeadAssignRequest request);
    LeadResponse transferLead(Long id, LeadAssignRequest request);
    LeadResponse updateLeadStatus(Long id, UpdateLeadStatusRequest request);
}
