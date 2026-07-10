package com.dung.democrm.repository;

import com.dung.democrm.entity.Activity;
import com.dung.democrm.entity.Lead;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends BaseRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {
    Optional<Activity> findByIdAndActiveTrue(Long id);
    Optional<Activity> findByIdAndLeadOwnerIdAndActiveTrue(Long id, Long ownerId);
    Optional<Activity> findByIdAndLeadTeamOwnerIdAndActiveTrue(Long id, Long managerId);
}

