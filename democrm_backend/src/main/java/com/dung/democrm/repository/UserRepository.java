package com.dung.democrm.repository;

import com.dung.democrm.common.enums.Role;
import com.dung.democrm.common.enums.UserStatus;
import com.dung.democrm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByRoleAndStatus(Role role, UserStatus status);

    List<User> findByManager(User manager);

    List<User> findByManagerAndStatus(User manager, UserStatus status);

    Optional<User> findByEmployeeCode(String employeeCode);

    Optional<User> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByPhone(String phone);

    Page<User> findByFullNameContainingIgnoreCaseAndActiveTrue(String keyword, Pageable pageable);

    List<User> findByManagerIdAndActiveTrue(Long managerId);

    List<User> findByRoleAndActiveTrue(Role role);
    long countByManagerIdAndActiveTrue(Long managerId);

    Optional<User> findByIdAndActiveTrue(Long id);
}
