package com.dung.democrm.repository;

import com.dung.democrm.entity.LeadTimeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadTimelineRepository extends JpaRepository<LeadTimeline, Long> {
    List<LeadTimeline> findByLeadIdAndActiveTrueOrderByCreatedAtDesc(Long leadId);
}
