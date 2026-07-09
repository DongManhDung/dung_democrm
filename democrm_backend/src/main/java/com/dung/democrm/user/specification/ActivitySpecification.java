package com.dung.democrm.user.specification;

import com.dung.democrm.common.enums.ActivityStatus;
import com.dung.democrm.dto.request.ActivitySearchRequest;
import com.dung.democrm.entity.Activity;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class ActivitySpecification {
    private ActivitySpecification(){

    }

    public static Specification<Activity> search(ActivitySearchRequest request){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Active
            predicates.add(cb.isTrue(root.get("active")));

            // Lead
            if (request.getLeadId() != null) {
                Join<Activity, Lead> leadJoin = root.join("lead");
                predicates.add(cb.equal(leadJoin.get("id"), request.getLeadId()));
            }

            // Created By
            if (request.getCreatedById() != null) {
                Join<Activity, User> userJoin = root.join("createdBy");
                predicates.add(cb.equal(userJoin.get("id"), request.getCreatedById()));
            }

            // Activity Type
            if (request.getType() != null) {
                predicates.add(cb.equal(root.get("type"), request.getType()));
            }

            // Activity Status
            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }

            // Subject
            if (StringUtils.hasText(request.getSubject())) {
                predicates.add(
                        cb.like(cb.lower(root.get("subject")), "%" + request.getSubject().trim().toLowerCase() + "%")
                );
            }

            // Due From
            if (request.getDueFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dueDate"), request.getDueFrom()));
            }

            // Due To
            if (request.getDueTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dueDate"), request.getDueTo()));
            }

            // Over Due
            LocalDate today = null;
            if (request.getOverDue() != null) {
                today = LocalDate.now();

                if (request.getOverDue()) {
                    predicates.add(cb.lessThan(root.get("dueDate"), today));

                    predicates.add(cb.equal(root.get("status"), ActivityStatus.PENDING));
                }
            } else {
                predicates.add(
                        cb.or(
                                cb.greaterThanOrEqualTo(root.get("dueDate"), today),
                                cb.notEqual(root.get("status"), ActivityStatus.PENDING)
                        )
                );
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
