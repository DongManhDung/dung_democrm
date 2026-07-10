package com.dung.democrm.service.impl;

import com.dung.democrm.common.enums.ActivityStatus;
import com.dung.democrm.common.exception.ForbiddenException;
import com.dung.democrm.common.exception.ResourceNotFoundException;
import com.dung.democrm.dto.request.ActivityRequest;
import com.dung.democrm.dto.request.ActivitySearchRequest;
import com.dung.democrm.dto.response.ActivityDetailResponse;
import com.dung.democrm.dto.response.ActivityResponse;
import com.dung.democrm.entity.Activity;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.User;
import com.dung.democrm.mapper.ActivityMapper;
import com.dung.democrm.repository.ActivityRepository;
import com.dung.democrm.repository.LeadRepository;
import com.dung.democrm.repository.UserRepository;
import com.dung.democrm.service.ActivityService;
import com.dung.democrm.user.specification.ActivitySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<ActivityResponse> getAllActivities(Pageable pageable) {
        return searchActivities(new ActivitySearchRequest(), pageable);
    }

    @Override
    public Page<ActivityResponse> searchActivities(ActivitySearchRequest request, Pageable pageable) {
        return activityRepository.findAll(ActivitySpecification.search(request, getCurrentUser()), pageable)
                .map(activityMapper::toResponse);
    }

    @Override
    public ActivityResponse getActivityById(Long id) {
        return activityMapper.toResponse(getAccessibleActivityForRead(id));
    }

    @Override
    public ActivityResponse createActivity(ActivityRequest request) {
        Lead lead = getValidLead(request.getLeadId());

        User currentUser = getCurrentUser();

        Activity activity = new Activity();

        activity.setLead(lead);
        activity.setCreatedBy(currentUser);

        activity.setType(request.getType());
        activity.setStatus(request.getStatus());
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
        activity.setStatus(request.getStatus());

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

    @Override
    public ActivityDetailResponse getActivityDetail(Long id) {
        return activityMapper.toDetailResponse(getAccessibleActivityForRead(id));
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

    private Activity getAccessibleActivityForRead(Long id){
        User currentUser = getCurrentUser();

        return switch (currentUser.getRole()){
            case ADMIN -> getValidActivity(id);

            case MANAGER -> activityRepository.findByIdAndLeadTeamOwnerIdAndActiveTrue(id, currentUser.getId())
                    .orElseThrow(() -> new ForbiddenException("You do not have permission to access this activity."));

            case SALES -> activityRepository.findByIdAndLeadOwnerIdAndActiveTrue(id, currentUser.getId())
                    .orElseThrow(() -> new ForbiddenException("You do not have permission to access this activity."));

            default -> throw new ForbiddenException("Access Denied.");
        };
    }
}
