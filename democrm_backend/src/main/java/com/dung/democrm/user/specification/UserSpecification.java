package com.dung.democrm.user.specification;

import com.dung.democrm.dto.request.UserSearchRequest;
import com.dung.democrm.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class UserSpecification {
    private UserSpecification() {

    }

    public static Specification<User> filter(UserSearchRequest request){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Lấy user chưa bị soft delete
            predicates.add(criteriaBuilder.isTrue(root.get("active")));

            // Keyword
            if(request.getKeyword() != null &&!request.getKeyword().isBlank()){
                String keyword = "%" + request.getKeyword().trim().toLowerCase() + "%";

                Predicate fullName = criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), keyword);
                Predicate email = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), keyword);
                Predicate employeeCode = criteriaBuilder.like(criteriaBuilder.lower(root.get("employeeCode")), keyword);
                predicates.add(criteriaBuilder.or(fullName,email,employeeCode));
            }

            // Role
            if(request.getRole() != null){
                predicates.add(criteriaBuilder.equal(root.get("role"), request.getRole()));
            }

            // Status
            if(request.getStatus() != null){
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }

            // Manager
            if(request.getManagerId() != null){
                predicates.add(criteriaBuilder.equal(root.get("manager").get("id"), request.getManagerId()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
