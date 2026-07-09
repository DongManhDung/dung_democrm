package com.dung.democrm.controller;

import com.dung.democrm.dto.request.ActivityRequest;
import com.dung.democrm.dto.response.ActivityResponse;
import com.dung.democrm.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public List<ActivityResponse> getAllActivities(){
        return activityService.getAllActivities();
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
}
