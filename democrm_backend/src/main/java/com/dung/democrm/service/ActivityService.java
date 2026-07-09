package com.dung.democrm.service;

import com.dung.democrm.dto.request.ActivityRequest;
import com.dung.democrm.dto.response.ActivityResponse;

import java.util.List;

public interface ActivityService {
    List<ActivityResponse> getAllActivities();
    ActivityResponse getActivityById(Long id);
    ActivityResponse createActivity(ActivityRequest request);
    ActivityResponse updateActivity(Long id, ActivityRequest request);
    void deleteActivity(Long id);
}
