package com.dung.democrm.repository;

import com.dung.democrm.entity.Customer;
import com.dung.democrm.entity.Lead;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeadRepository extends BaseRepository<Lead, Long> {
    List<Lead> findAllByActiveTrue();
    Optional<Lead> findByCustomerAndActiveTrue(Customer customer);
}
