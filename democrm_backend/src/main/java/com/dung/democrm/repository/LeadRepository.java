package com.dung.democrm.repository;

import com.dung.democrm.common.enums.LeadStatus;
import com.dung.democrm.entity.Customer;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeadRepository extends BaseRepository<Lead, Long> {
    Optional<Lead> findByIdAndActiveTrue(Long id);
    Page<Lead> findAllByActiveTrue(Pageable pageable);
    List<Lead> findByOwnerIdAndActiveTrue(Long ownerId);
    List<Lead> findByTeamOwnerIdAndActiveTrue(Long managerId);
    boolean existsByCustomerIdAndLeadStatusInAndActiveTrue(Long customerId, Collection<LeadStatus> statuses);
}
