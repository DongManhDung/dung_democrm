package com.dung.democrm.repository;

import com.dung.democrm.common.enums.LeadStatus;
import com.dung.democrm.entity.Customer;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeadRepository extends BaseRepository<Lead, Long>, JpaSpecificationExecutor<Lead> {
    Optional<Lead> findByIdAndActiveTrue(Long id);
    List<Lead> findByOwnerIdAndActiveTrue(Long ownerId);
    List<Lead> findByTeamOwnerIdAndActiveTrue(Long managerId);

    boolean existsByIdAndActiveTrue(Long id);

    Optional<Lead> findFirstByCustomerIdAndLeadStatusInAndActiveTrue(Long customerId, Collection<LeadStatus> statuses);

    // Statistics
    long countByActiveTrue();

    long countByExpiredAtBeforeAndActiveTrue(LocalDate today);

    long countByExpiredAtBetweenAndActiveTrue(LocalDate from, LocalDate to);

    @Query("SELECT l.leadStatus, COUNT(1) from Lead l WHERE l.active = true GROUP BY l.leadStatus")
    List<Object[]> countLeadByStatus();

    @Query("SELECT l.source, COUNT(1) from Lead l WHERE l.active = true GROUP BY l.source")
    List<Object[]> countLeadBySource();
}
