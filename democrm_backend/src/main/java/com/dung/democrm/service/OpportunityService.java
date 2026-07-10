package com.dung.democrm.service;

import com.dung.democrm.dto.request.OpportunityRequest;
import com.dung.democrm.dto.response.OpportunityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OpportunityService {
    Page<OpportunityResponse> getAll(Pageable pageable);

    OpportunityResponse getById(Long id);

    OpportunityResponse create(OpportunityRequest request);

    OpportunityResponse update(Long id, OpportunityRequest request);

    void delete(Long id);
}
