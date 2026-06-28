package com.dung.democrm.repository;

import com.dung.democrm.entity.BaseRepository;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OpportunityRepository extends BaseRepository<Opportunity, Long> {
    Optional<Opportunity> findByLead(Lead lead);
}
