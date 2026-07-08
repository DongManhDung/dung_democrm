package com.dung.democrm.service.impl;

import com.dung.democrm.common.enums.TimelineAction;
import com.dung.democrm.common.exception.ResourceNotFoundException;
import com.dung.democrm.dto.response.LeadTimelineResponse;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.LeadTimeline;
import com.dung.democrm.entity.User;
import com.dung.democrm.mapper.LeadTimelineMapper;
import com.dung.democrm.repository.LeadRepository;
import com.dung.democrm.repository.LeadTimelineRepository;
import com.dung.democrm.service.LeadTimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeadTimelineServiceImpl implements LeadTimelineService {

    private final LeadTimelineRepository leadTimelineRepository;
    private final LeadRepository leadRepository;
    private final LeadTimelineMapper leadTimelineMapper;

    @Override
    public void saveTimeLine(Lead lead, TimelineAction action, String description, User performedBy) {
        LeadTimeline timeline = new LeadTimeline();

        timeline.setLead(lead);
        timeline.setAction(action);
        timeline.setDescription(description);
        timeline.setPerformedBy(performedBy);

        leadTimelineRepository.save(timeline);
    }

    @Override
    public List<LeadTimelineResponse> getLeadTimeline(Long leadId) {

        if(!leadRepository.existsByIdAndActiveTrue(leadId)){
            throw new ResourceNotFoundException("Lead not found.");
        }

        return leadTimelineRepository.findByLeadIdAndActiveTrueOrderByCreatedAtDesc(leadId)
                .stream().map(leadTimelineMapper::toResponse)
                .toList();
    }
}
