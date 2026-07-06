package com.dung.democrm.service.impl;

import com.dung.democrm.common.enums.Role;
import com.dung.democrm.common.enums.UserStatus;
import com.dung.democrm.common.exception.BadRequestException;
import com.dung.democrm.common.exception.ResourceNotFoundException;
import com.dung.democrm.dto.request.CustomerOwnerRequest;
import com.dung.democrm.dto.request.CustomerRequest;
import com.dung.democrm.dto.request.CustomerSearchRequest;
import com.dung.democrm.dto.response.CustomerDetailResponse;
import com.dung.democrm.dto.response.CustomerTimelineResponse;
import com.dung.democrm.dto.response.DuplicatePhoneResponse;
import com.dung.democrm.user.specification.CustomerSpecification;
import com.dung.democrm.dto.response.CustomerResponse;
import com.dung.democrm.entity.Customer;
import com.dung.democrm.entity.User;
import com.dung.democrm.mapper.CustomerMapper;
import com.dung.democrm.repository.CustomerRepository;
import com.dung.democrm.repository.UserRepository;
import com.dung.democrm.service.CustomerService;
import com.dung.democrm.util.CustomerExcelExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Override
    public Page<CustomerResponse> getAllCustomers(Pageable pageable) {
        return customerRepository.findAllByActiveTrue(pageable).map(CustomerMapper::toResponse);
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

        return CustomerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        validateDuplicate(request,null);

        User owner = getValidOwner(request.getOwnerId());

        Customer customer = CustomerMapper.toEntity(request, owner);

        customerRepository.save(customer);

        return CustomerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

        validateDuplicate(request, customer.getId());

        User owner = getValidOwner(request.getOwnerId());

        CustomerMapper.updateEntity(customer, request, owner);

        customerRepository.save(customer);

        return CustomerMapper.toResponse(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

        customerRepository.softDelete(customer);
    }

    @Override
    public Page<CustomerResponse> searchCustomers(CustomerSearchRequest request, Pageable pageable) {
        return customerRepository.findAll(CustomerSpecification.search(request), pageable)
                .map(CustomerMapper::toResponse);
    }

    @Override
    public List<CustomerResponse> getMyCustomers() {
        User currentUser = getCurrentUser();

        return customerRepository.findByOwnerIdAndActiveTrue(currentUser.getId())
                .stream()
                .map(CustomerMapper::toResponse).toList();
    }

    @Override
    public CustomerResponse assignOwner(Long customerId, CustomerOwnerRequest request) {

        Customer customer = customerRepository.findByIdAndActiveTrue(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

        if(customer.getOwner() != null){
            throw new BadRequestException("Customer already has an owner.");
        }

        User owner = getValidOwner(request.getOwnerId());

        customer.setOwner(owner);

        customerRepository.save(customer);

        return CustomerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse transferOwner(Long customerId, CustomerOwnerRequest request) {

        Customer customer = customerRepository.findByIdAndActiveTrue(customerId)
                .orElseThrow(() -> new BadRequestException("Customer not found."));

        if(customer.getOwner() == null){
            throw new BadRequestException("Customer has no owner.");
        }

        User newOwner = getValidOwner(request.getOwnerId());

        if(customer.getOwner().getId().equals(newOwner.getId())){
            throw new BadRequestException("Customer is already assigned to this owner.");
        }

        customer.setOwner(newOwner);

        customerRepository.save(customer);

        return CustomerMapper.toResponse(customer);
    }

    @Override
    public CustomerDetailResponse getCustomerDetail(Long id) {
        Customer customer = customerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

        CustomerDetailResponse response = CustomerMapper.toDetailResponse(customer);

        // Về sau khu này sẽ chi tiết hơn khi có: Lead, Opportunity, Contract, Activity

        response.setTotalLeads(0);
        response.setTotalOpportunities(0);
        response.setTotalWonOpportunities(0);
        response.setTotalContracts(0);
        response.setTotalActivities(0);
        response.setTotalRevenue(BigDecimal.ZERO);

        return response;
    }

    @Override
    public List<CustomerTimelineResponse> getCustomerTimeline(Long customerId) {
        Customer customer = customerRepository.findByIdAndActiveTrue(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

        List<CustomerTimelineResponse> timeLine = new ArrayList<>();

        timeLine.add(
                CustomerTimelineResponse.builder()
                        .occurredAt(customer.getCreatedAt())
                        .type("CUSTOMER_CREATED")
                        .title("Customer created")
                        .description("Customer has been created")
                        .build()
        );

        if(customer.getOwner() != null){
            timeLine.add(
                    CustomerTimelineResponse.builder()
                            .occurredAt(customer.getUpdatedAt())
                            .type("OWNER_ASSIGNED")
                            .title("Owner Assigned")
                            .description("Assigned to " + customer.getOwner().getFullName())
                            .build()
            );
        }

        timeLine.sort(
                Comparator.comparing(CustomerTimelineResponse::getOccurredAt).reversed()
        );

        return timeLine;
    }

    @Override
    public DuplicatePhoneResponse checkDuplicatePhone(String phone) {

        return customerRepository.findByPhoneAndActiveTrue(phone)
                .map(customer -> DuplicatePhoneResponse.builder()
                        .duplicated(true)
                        .customerId(customer.getId())
                        .customerName(customer.getName())
                        .company(customer.getCompany())
                        .ownerId(customer.getOwner() != null ? customer.getOwner().getId() : null)
                        .ownerName(customer.getOwner() != null ? customer.getOwner().getFullName() : null)
                        .ownerEmployeeCode(customer.getOwner() != null ? customer.getOwner().getEmployeeCode() : null)
                        .build()
                )
                .orElse(
                        DuplicatePhoneResponse.builder().duplicated(false).build()
                );
    }

    @Override
    public ByteArrayInputStream exportCustomers() {
        List<Customer> customers = customerRepository.findAllByActiveTrue();
        return CustomerExcelExporter.export(customers);
    }

    private void validateDuplicate(CustomerRequest request, Long customerId){
        customerRepository.findByPhoneAndActiveTrue(request.getPhone())
                .ifPresent(customer -> {
                    if (!customer.getId().equals(customerId)){
                        throw new BadRequestException("Phone already exists.");
                    }
                });

        if(request.getEmail() != null && !request.getEmail().isBlank()){
            customerRepository.findByEmailAndActiveTrue(request.getEmail())
                    .ifPresent( customer -> {
                        if(!customer.getId().equals(customerId)){
                            throw new BadRequestException("Email already exists.");
                        }
                    });
        }
    }

    private User getValidOwner(Long ownerId){
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found."));

        if(owner.getRole() == Role.ADMIN){
            throw new BadRequestException("Admin cannot own a customer.");
        }

        if(owner.getStatus() == UserStatus.RESIGNED){
            throw new BadRequestException("Cannot assign a resigned user as owner.");
        }

        if (owner.getStatus() == UserStatus.LOCKED){
            throw new BadRequestException("Cannot assign a locked user as owner.");
        }

        return owner;
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();;

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found."));
    }
}
