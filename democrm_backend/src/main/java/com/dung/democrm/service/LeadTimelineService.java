package com.dung.democrm.service;

import com.dung.democrm.common.enums.TimelineAction;
import com.dung.democrm.dto.response.LeadTimelineResponse;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.User;

import java.util.List;

public interface LeadTimelineService {
    void saveTimeLine(Lead lead, TimelineAction action, String description, User performedBy);

    List<LeadTimelineResponse> getLeadTimeline(Long leadId);
}
