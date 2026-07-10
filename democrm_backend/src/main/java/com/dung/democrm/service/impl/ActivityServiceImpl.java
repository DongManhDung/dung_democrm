package com.dung.democrm.service.impl;

import com.dung.democrm.common.enums.ActivityStatus;
import com.dung.democrm.common.enums.ActivityType;
import com.dung.democrm.common.enums.Role;
import com.dung.democrm.common.exception.ForbiddenException;
import com.dung.democrm.common.exception.ResourceNotFoundException;
import com.dung.democrm.dto.request.ActivityRequest;
import com.dung.democrm.dto.request.ActivitySearchRequest;
import com.dung.democrm.dto.response.ActivityDetailResponse;
import com.dung.democrm.dto.response.ActivityResponse;
import com.dung.democrm.dto.response.ActivityStatisticsResponse;
import com.dung.democrm.dto.response.ActivityTimelineResponse;
import com.dung.democrm.entity.Activity;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.User;
import com.dung.democrm.mapper.ActivityMapper;
import com.dung.democrm.repository.ActivityRepository;
import com.dung.democrm.repository.CustomerRepository;
import com.dung.democrm.repository.LeadRepository;
import com.dung.democrm.repository.UserRepository;
import com.dung.democrm.service.ActivityService;
import com.dung.democrm.user.specification.ActivitySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final LeadRepository leadRepository;
    private final CustomerRepository customerRepository;
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

    @Override
    public Page<ActivityTimelineResponse> getLeadTimeline(Long leadId, Pageable pageable) throws AccessDeniedException {
        getValidLead(leadId);
        getAccessibleLeadForRead(leadId);
        return activityRepository.findAll(ActivitySpecification.timelineByLead(leadId, getCurrentUser()), pageable)
                .map(activityMapper::toTimelineResponse);
    }

    @Override
    public Page<ActivityTimelineResponse> getCustomerTimeline(Long customerId, Pageable pageable) throws AccessDeniedException {
        getValidCustomer(customerId);
        getAccessibleLeadForRead(customerId);
        return activityRepository.findAll(ActivitySpecification.timeLineByCustomer(customerId, getCurrentUser()), pageable)
                .map(activityMapper::toTimelineResponse);
    }

    @Override
    public Page<ActivityTimelineResponse> getSalesTimeline(Long salesId, Pageable pageable) {
        validateCanViewSalesTimeline(salesId);
        return activityRepository.findAll(ActivitySpecification.timeLineBySales(salesId, getCurrentUser()), pageable)
                .map(activityMapper::toTimelineResponse);
    }

    @Override
    public ActivityStatisticsResponse getActivityStatistics() {
        Map<ActivityType, Long> activitiesByType = toMap(activityRepository.countByType());
        Map<ActivityStatus, Long> activitiesByStatus = toMap(activityRepository.countByStatus());

        return ActivityStatisticsResponse.builder()
                .totalActivities(activityRepository.countByActiveTrue())
                .activitiesByType(activitiesByType)
                .activitiesByStatus(activitiesByStatus)
                .overdueActivities(activityRepository.countByActiveTrueAndStatusAndDueDateBefore(ActivityStatus.PENDING, LocalDate.now()))
                .completedActivities(activityRepository.countByActiveTrueAndStatus(ActivityStatus.COMPLETED))
                .build();
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

    private void getValidCustomer(Long id){
        customerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));
    }

    private User getValidUser(Long id){
        return userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    private void getAccessibleLeadForRead(Long id) throws AccessDeniedException {
        Lead lead = getValidLead(id);

        User currentUser = getCurrentUser();

        if(currentUser.getRole() == Role.ADMIN){
            return;
        }

        if(currentUser.getRole() == Role.MANAGER && lead.getTeamOwner().getId().equals(currentUser.getId())){
            return;
        }

        if (currentUser.getRole() == Role.SALES && lead.getOwner().getId().equals(currentUser.getId())){
            return;
        }

        throw new AccessDeniedException("You do not have permission.");
    }

    private void validateCanViewSalesTimeline(Long salesId){
        User currentUser = getCurrentUser();
        User sales = getValidUser(salesId);

        switch (currentUser.getRole()){
            case ADMIN: return;

            case MANAGER:
                if (sales.getManager() == null
                        || !sales.getManager().getId().equals(currentUser.getId())){
                    throw new ForbiddenException("You do not have permission.");
                }
                return;

            case SALES:
                if (!sales.getId().equals(currentUser.getId())){
                    throw new ForbiddenException("You do not have permission.");
                }
                return;

            default:
                throw new ForbiddenException("You do not have permission.");
        }

    }

    private <T> Map<T, Long> toMap(List<Object[]> rows){
        return rows.stream()
                .collect(
                  Collectors.toMap(
                          row -> (T) row[0],
                          row -> (Long) row[1]
                  )
                );
    }
}
