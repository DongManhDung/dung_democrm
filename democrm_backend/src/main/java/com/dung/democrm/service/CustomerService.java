package com.dung.democrm.service;


import com.dung.democrm.dto.request.CustomerRequest;
import com.dung.democrm.dto.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Page<CustomerResponse> getAllCustomers(Pageable pageable);
    CustomerResponse getCustomerById(Long id);
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse updateCustomer(Long id, CustomerRequest request);
    void deleteCustomer(Long id);
}
