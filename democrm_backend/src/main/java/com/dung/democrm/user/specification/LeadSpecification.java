package com.dung.democrm.user.specification;

import com.dung.democrm.common.enums.Role;
import com.dung.democrm.dto.request.LeadSearchRequest;
import com.dung.democrm.entity.Customer;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class LeadSpecification {
    private LeadSpecification(){

    }

    public static Specification<Lead> search(LeadSearchRequest request, User currentUser){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Active
            predicates.add(cb.isTrue(root.get("active")));

            // Data permission
            if(currentUser.getRole() == Role.SALES){
                Join<Lead, User> ownerJoin = root.join("owner");
                predicates.add(
                        cb.equal(ownerJoin.get("id"), currentUser.getId())
                );
            } else if (currentUser.getRole() == Role.MANAGER){
                Join<Lead, User> teamOwnerJoin = root.join("teamOwner");

                predicates.add(
                        cb.equal(teamOwnerJoin.get("id"), currentUser.getId())
                );
            } // ADMIN không cần filter

            // Customer name
            if(StringUtils.hasText(request.getCustomerName())){
                Join<Lead, Customer> customerJoin = root.join("customer");

                predicates.add(
                        cb.like(
                                cb.lower(customerJoin.get("name")),
                                "%" + request.getCustomerName().trim().toLowerCase() + "%"
                        )
                );
            }

            // Owner
            if(request.getOwnerId() != null){
                Join<Lead, User> ownerJoin = root.join("owner");

                predicates.add(cb.equal(ownerJoin.get("id"), request.getOwnerId()));
            }

            // Team Owner
            if(request.getTeamOwnerId() != null){
                Join<Lead, User> teamOwnerJoin = root.join("teamOwner");
                predicates.add(
                        cb.equal(teamOwnerJoin.get("id"), request.getTeamOwnerId())
                );
            }

            // Status
            if(request.getStatus() != null){
                predicates.add(
                        cb.equal(root.get("leadStatus"), request.getStatus())
                );
            }

            // Source
            if(request.getSource() != null){
                predicates.add(
                        cb.equal(root.get("source"), request.getSource())
                );
            }

            // Priority
            if(request.getPriority() != null){
                predicates.add(
                        cb.equal(root.get("priority"), request.getPriority())
                );
            }

            // Assigned From
            if(request.getAssignedFrom() != null){
                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("assignedAt"), request.getAssignedFrom())
                );
            }

            // Assigned To
            if(request.getAssignedTo() != null){
                predicates.add(
                        cb.lessThanOrEqualTo(root.get("assignedAt"), request.getAssignedTo())
                );
            }

            // Expired
            if(request.getExpired() != null){
                LocalDate today = LocalDate.now();
                if(request.getExpired()){
                    predicates.add(
                            cb.lessThan(root.get("expiredAt"), today)
                    );
                } else {
                    predicates.add(
                            cb.greaterThanOrEqualTo(root.get("expiredAt"), today)
                    );
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
