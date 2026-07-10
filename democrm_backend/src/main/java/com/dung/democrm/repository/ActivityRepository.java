package com.dung.democrm.repository;

import com.dung.democrm.common.enums.ActivityStatus;
import com.dung.democrm.entity.Activity;
import com.dung.democrm.entity.Lead;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends BaseRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {
    Optional<Activity> findByIdAndActiveTrue(Long id);
    Optional<Activity> findByIdAndLeadOwnerIdAndActiveTrue(Long id, Long ownerId);
    Optional<Activity> findByIdAndLeadTeamOwnerIdAndActiveTrue(Long id, Long managerId);

    // Statistics
    long countByActiveTrue();
    @Query("SELECT a.type, COUNT(a) FROM Activity a WHERE a.active = true GROUP BY a.type")
    List<Object[]> countByType();
    @Query("SELECT a.status, count(a) FROM Activity a WHERE a.active = true GROUP BY a.status")
    List<Object[]> countByStatus();
    long countByActiveTrueAndStatusAndDueDateBefore(ActivityStatus status, LocalDate dueDate);
    long countByActiveTrueAndStatus(ActivityStatus status);
}

