package com.dung.democrm.repository;

import com.dung.democrm.entity.Customer;
import com.dung.democrm.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends BaseRepository<Customer, Long> {
    Optional<User> findByPhoneAndActiveTrue(String phone);
    boolean existsByPhoneAndActiveTrue(String phone);
}
