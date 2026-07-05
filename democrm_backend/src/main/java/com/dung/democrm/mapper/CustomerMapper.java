package com.dung.democrm.mapper;

import com.dung.democrm.dto.request.CustomerRequest;
import com.dung.democrm.dto.response.CustomerResponse;
import com.dung.democrm.entity.Customer;
import com.dung.democrm.entity.User;

public final class CustomerMapper {
    private CustomerMapper(){

    }

    public static Customer toEntity(CustomerRequest request, User owner){
        Customer customer = new Customer();

        customer.setName(request.getName());
        customer.setCompany(request.getCompany());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setOwner(owner);

        return customer;
    }

    public static void updateEntity(Customer customer, CustomerRequest request, User owner){
        customer.setName(request.getName());
        customer.setCompany(request.getCompany());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setOwner(owner);
    }

    public static CustomerResponse toResponse(Customer customer){
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .company(customer.getCompany())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .ownerId(customer.getOwner().getId())
                .ownerName(customer.getOwner().getFullName())
                .ownerEmployeeCode(customer.getOwner().getEmployeeCode())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
