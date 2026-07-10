package com.dung.democrm.controller;

import com.dung.democrm.dto.request.ActivityRequest;
import com.dung.democrm.dto.request.ActivitySearchRequest;
import com.dung.democrm.dto.response.ActivityDetailResponse;
import com.dung.democrm.dto.response.ActivityResponse;
import com.dung.democrm.dto.response.ActivityStatisticsResponse;
import com.dung.democrm.dto.response.ActivityTimelineResponse;
import com.dung.democrm.service.ActivityService;
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
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public Page<ActivityResponse> getAllActivities(Pageable pageable){
        return activityService.getAllActivities(pageable);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public Page<ActivityResponse> searchActivities(
            @ModelAttribute ActivitySearchRequest request,
            Pageable pageable
            ){
        return activityService.searchActivities(request, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public ActivityResponse getActivityById(@PathVariable("id") Long id){
        return activityService.getActivityById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityResponse createActivity(@Valid @RequestBody ActivityRequest request){
        return activityService.createActivity(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public ActivityResponse updateActivity(
            @PathVariable("id") Long id,
            @Valid @RequestBody ActivityRequest request
    ){
        return activityService.updateActivity(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActivity(@PathVariable("id") Long id){
        activityService.deleteActivity(id);
    }

    @GetMapping("/{id}/detail")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public ActivityDetailResponse getActivityDetail(@PathVariable("id") Long id){
        return activityService.getActivityDetail(id);
    }

    @GetMapping("/timeline/lead/{leadId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public Page<ActivityTimelineResponse> getLeadTimeline(@PathVariable("leadId") Long leadId, Pageable pageable) throws AccessDeniedException {
        return activityService.getLeadTimeline(leadId, pageable);
    }

    @GetMapping("/timeline/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public Page<ActivityTimelineResponse> getCustomerTimeline(@PathVariable("customerId") Long customerId, Pageable pageable) throws AccessDeniedException {
        return activityService.getCustomerTimeline(customerId, pageable);
    }

    @GetMapping("/timeline/sales/{salesId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public Page<ActivityTimelineResponse> getSalesTimeline(@PathVariable("salesId") Long salesId, Pageable pageable){
        return activityService.getSalesTimeline(salesId, pageable);
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ActivityStatisticsResponse getActivityStatistics(){
        return activityService.getActivityStatistics();
    }
}
