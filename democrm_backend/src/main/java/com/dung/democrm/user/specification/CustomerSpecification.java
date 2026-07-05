package com.dung.democrm.user.specification;

import com.dung.democrm.dto.request.CustomerSearchRequest;
import com.dung.democrm.entity.Customer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {
    public CustomerSpecification(){

    }

    public static Specification<Customer> search(CustomerSearchRequest request){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isTrue(root.get("active")));

            // Name
            if(StringUtils.hasText(request.getName())){
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + request.getName().toLowerCase() + "%"
                ));
            }

            // Phone
            if(StringUtils.hasText(request.getPhone())){
                predicates.add(criteriaBuilder.like(
                       root.get("phone"), "%" + request.getPhone() + "%"
                ));
            }

            // Email
            if(StringUtils.hasText(request.getEmail())){
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        "%" + request.getEmail().toLowerCase() + "%"
                ));
            }

            // Company
            if(StringUtils.hasText(request.getCompany())){
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("company")),
                        "%" + request.getCompany().toLowerCase() + "%"
                ));
            }

            // Owner
            if(request.getOwnerId() != null){
                predicates.add(criteriaBuilder.equal(root.get("owner").get("id"),
                        request.getOwnerId()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
