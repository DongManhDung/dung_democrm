package com.dung.democrm.controller;

import com.dung.democrm.dto.request.LeadAssignRequest;
import com.dung.democrm.dto.request.LeadRequest;
import com.dung.democrm.dto.request.LeadSearchRequest;
import com.dung.democrm.dto.request.UpdateLeadStatusRequest;
import com.dung.democrm.dto.response.LeadDetailResponse;
import com.dung.democrm.dto.response.LeadResponse;
import com.dung.democrm.dto.response.LeadTimelineResponse;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.service.LeadService;
import com.dung.democrm.service.LeadTimelineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {
    private final LeadService leadService;
    private final LeadTimelineService leadTimelineService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Page<LeadResponse> getAllLeads(Pageable pageable){
        return leadService.getAllLeads(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public LeadResponse getLeadById(@PathVariable("id") Long id) throws AccessDeniedException {
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
    @GetMapping("/{id}/detail")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public LeadDetailResponse getLeadDetail(@PathVariable("id") Long id) throws AccessDeniedException {
        return leadService.getLeadDetail(id);
    }

    @GetMapping("/{id}/timeline")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public List<LeadTimelineResponse> getLeadTimeline (@PathVariable("id") Long id){
        return leadTimelineService.getLeadTimeline(id);
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public LeadResponse assignLead(
            @PathVariable("id") Long id,
            @Valid @RequestBody LeadAssignRequest request
            ){
        return leadService.assignLead(id, request);
    }

    @PutMapping("/{id}/transfer")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public LeadResponse transferLead(
            @PathVariable("id") Long id,
            @Valid @RequestBody LeadAssignRequest request
    ){
        return leadService.transferLead(id, request);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public LeadResponse updateLeadStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateLeadStatusRequest request
    ){
        return leadService.updateLeadStatus(id, request);
    }
}
