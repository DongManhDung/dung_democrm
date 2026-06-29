package com.dung.democrm.repository;

import com.dung.democrm.common.enums.LeadStatus;
import com.dung.democrm.entity.Customer;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeadRepository extends BaseRepository<Lead, Long> {
    List<Lead> findAllByActiveTrue();
    Optional<Lead> findByCustomerAndActiveTrue(Customer customer);
    List<Lead> findByOwner(User owner);
    List<Lead> findByTeamOwner(User manager);
    List<Lead> findByLeadStatus(LeadStatus leadStatus);
    List<Lead> findByOwnerAndLeadStatus(User owner, LeadStatus status);
    List<Lead> findByExpiredAtBefore(LocalDate date);;
}
