package com.dung.democrm.service;

import com.dung.democrm.dto.request.ActivityRequest;
import com.dung.democrm.dto.request.ActivitySearchRequest;
import com.dung.democrm.dto.response.ActivityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityService {
    Page<ActivityResponse> getAllActivities(Pageable pageable);
    Page<ActivityResponse> searchActivities(ActivitySearchRequest request, Pageable pageable);
    ActivityResponse getActivityById(Long id);
    ActivityResponse createActivity(ActivityRequest request);
    ActivityResponse updateActivity(Long id, ActivityRequest request);
    void deleteActivity(Long id);
}
