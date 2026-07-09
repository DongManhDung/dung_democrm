package com.dung.democrm.service.impl;

import com.dung.democrm.common.enums.ActivityStatus;
import com.dung.democrm.common.exception.ResourceNotFoundException;
import com.dung.democrm.dto.request.ActivityRequest;
import com.dung.democrm.dto.response.ActivityResponse;
import com.dung.democrm.entity.Activity;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.User;
import com.dung.democrm.mapper.ActivityMapper;
import com.dung.democrm.repository.ActivityRepository;
import com.dung.democrm.repository.LeadRepository;
import com.dung.democrm.repository.UserRepository;
import com.dung.democrm.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final LeadRepository leadRepository;
    private final UserRepository userRepository;

    private final ActivityMapper activityMapper;

    @Override
    public List<ActivityResponse> getAllActivities() {
        return activityRepository.findAllByActiveTrue()
                .stream().map(activityMapper::toResponse).toList();
    }

    @Override
    public ActivityResponse getActivityById(Long id) {
        return activityMapper.toResponse(getValidActivity(id));
    }

    @Override
    public ActivityResponse createActivity(ActivityRequest request) {
        Lead lead = getValidLead(request.getLeadId());

        User currentUser = getCurrentUser();

        Activity activity = new Activity();

        activity.setLead(lead);
        activity.setCreatedBy(currentUser);

        activity.setType(request.getType());
        activity.setStatus(ActivityStatus.PENDING);
        activity.setSubject(request.getSubject());
        activity.setDescription(request.getDescription());

        activity.setDueDate(request.getDueDate());

        Activity savedActivity = activityRepository.save(activity);

        return activityMapper.toResponse(savedActivity);
    }

    @Override
    public ActivityResponse updateActivity(Long id, ActivityRequest request) {

        Activity activity = getValidActivity(id);

        Lead lead = getValidLead(request.getLeadId());

        activity.setLead(lead);

        activity.setType(request.getType());

        activity.setSubject(request.getSubject());
        activity.setDescription(request.getDescription());

        activity.setDueDate(request.getDueDate());

        return activityMapper.toResponse(activityRepository.save(activity));
    }

    @Override
    public void deleteActivity(Long id) {
        Activity activity = getValidActivity(id);

        activity.setActive(false);
        activity.setDeletedAt(LocalDateTime.now());

        activityRepository.save(activity);
    }

    // Helper
    private Activity getValidActivity(Long id){
        return activityRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found."));
    }

    private Lead getValidLead(Long id){
        return leadRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found."));
    }

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }
}
