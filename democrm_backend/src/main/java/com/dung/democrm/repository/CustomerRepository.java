package com.dung.democrm.repository;

import com.dung.democrm.entity.Customer;
import com.dung.democrm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends BaseRepository<Customer, Long> {
    Optional<Customer> findByPhoneAndActiveTrue(String phone);
    Optional<Customer> findByEmailAndActiveTrue(String email);
    boolean existsByPhoneAndActiveTrue(String phone);
    Page<Customer> findAllByActiveTrue(Pageable pageable);
    Optional<Customer> findByIdAndActiveTrue(Long id);
}
