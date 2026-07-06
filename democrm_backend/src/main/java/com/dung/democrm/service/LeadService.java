package com.dung.democrm.service;

import com.dung.democrm.dto.request.LeadRequest;
import com.dung.democrm.dto.response.LeadResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeadService {
    Page<LeadResponse> getAllLeads(Pageable pageable);
    LeadResponse getLeadById(Long id);
    LeadResponse createLead(LeadRequest request);
    LeadResponse updateLead(Long id, LeadRequest request);
    void deleteLead(Long id);
}
