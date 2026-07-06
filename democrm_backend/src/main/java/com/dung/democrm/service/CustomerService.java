package com.dung.democrm.service;


import com.dung.democrm.dto.request.CustomerOwnerRequest;
import com.dung.democrm.dto.request.CustomerRequest;
import com.dung.democrm.dto.request.CustomerSearchRequest;
import com.dung.democrm.dto.response.CustomerDetailResponse;
import com.dung.democrm.dto.response.CustomerResponse;
import com.dung.democrm.dto.response.CustomerTimelineResponse;
import com.dung.democrm.dto.response.DuplicatePhoneResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    Page<CustomerResponse> getAllCustomers(Pageable pageable);
    CustomerResponse getCustomerById(Long id);
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse updateCustomer(Long id, CustomerRequest request);
    void deleteCustomer(Long id);

    Page<CustomerResponse> searchCustomers(CustomerSearchRequest request, Pageable pageable);

    List<CustomerResponse> getMyCustomers();
    CustomerResponse assignOwner(Long customerId, CustomerOwnerRequest request);
    CustomerResponse transferOwner(Long customerId, CustomerOwnerRequest request);
    CustomerDetailResponse getCustomerDetail(Long id);
    List<CustomerTimelineResponse> getCustomerTimeline(Long customerId);
    DuplicatePhoneResponse checkDuplicatePhone(String phone);
}
