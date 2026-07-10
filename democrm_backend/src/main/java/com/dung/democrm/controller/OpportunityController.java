package com.dung.democrm.controller;

import com.dung.democrm.dto.request.OpportunityRequest;
import com.dung.democrm.dto.response.OpportunityResponse;
import com.dung.democrm.service.OpportunityService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/opportunities")
@RequiredArgsConstructor
public class OpportunityController {

    private final OpportunityService opportunityService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public Page<OpportunityResponse> getAll(Pageable pageable){
        return opportunityService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public OpportunityResponse getById(@PathVariable("id") Long id){
        return opportunityService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    @ResponseStatus(HttpStatus.CREATED)
    public OpportunityResponse create(@Valid @RequestBody OpportunityRequest request){
        return opportunityService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public OpportunityResponse update(
            @PathVariable("id") Long id,
            @Valid @RequestBody OpportunityRequest request
    ){
        return opportunityService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id){
        opportunityService.delete(id);
    }
}
