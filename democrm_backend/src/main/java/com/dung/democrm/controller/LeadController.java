package com.dung.democrm.controller;

import com.dung.democrm.dto.request.LeadRequest;
import com.dung.democrm.dto.request.LeadSearchRequest;
import com.dung.democrm.dto.response.LeadResponse;
import com.dung.democrm.service.LeadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {
    private final LeadService leadService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Page<LeadResponse> getAllLeads(Pageable pageable){
        return leadService.getAllLeads(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public LeadResponse getLeadById(@PathVariable("id") Long id){
        return leadService.getLeadById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    @ResponseStatus(HttpStatus.CREATED)
    public LeadResponse createLead(@Valid @RequestBody LeadRequest request){
        return leadService.createLead(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public LeadResponse updateLead(@PathVariable("id") Long id, @Valid @RequestBody LeadRequest request){
        return leadService.updateLead(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public void deleteLead(@PathVariable("id") Long id){
        leadService.deleteLead(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Page<LeadResponse> searchLeads(@ModelAttribute LeadSearchRequest request, Pageable pageable){
        return leadService.searchLeads(request, pageable);
    }
}
