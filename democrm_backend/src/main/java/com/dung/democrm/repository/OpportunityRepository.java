package com.dung.democrm.repository;

import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.Opportunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OpportunityRepository extends BaseRepository<Opportunity, Long> {
    Optional<Opportunity> findByLeadId(Long leadId);
    Page<Opportunity> findAllByActiveTrue(Pageable pageable);
    boolean existsByLeadId(Long leadId);
    Optional<Opportunity> findByIdAndActiveTrue(Long id);
}
