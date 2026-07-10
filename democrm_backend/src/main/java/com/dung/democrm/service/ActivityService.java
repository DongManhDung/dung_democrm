package com.dung.democrm.service;

import com.dung.democrm.dto.request.ActivityRequest;
import com.dung.democrm.dto.request.ActivitySearchRequest;
import com.dung.democrm.dto.response.ActivityDetailResponse;
import com.dung.democrm.dto.response.ActivityResponse;
import com.dung.democrm.dto.response.ActivityStatisticsResponse;
import com.dung.democrm.dto.response.ActivityTimelineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;


public interface ActivityService {
    Page<ActivityResponse> getAllActivities(Pageable pageable);
    Page<ActivityResponse> searchActivities(ActivitySearchRequest request, Pageable pageable);
    ActivityResponse getActivityById(Long id);
    ActivityResponse createActivity(ActivityRequest request);
    ActivityResponse updateActivity(Long id, ActivityRequest request);
    void deleteActivity(Long id);
    ActivityDetailResponse getActivityDetail(Long id);

    // Timeline
    Page<ActivityTimelineResponse> getLeadTimeline(Long leadId, Pageable pageable) throws AccessDeniedException;
    Page<ActivityTimelineResponse> getCustomerTimeline(Long customerId, Pageable pageable) throws AccessDeniedException;
    Page<ActivityTimelineResponse> getSalesTimeline(Long salesId, Pageable pageable);

    // Statistics
    ActivityStatisticsResponse getActivityStatistics();
}
