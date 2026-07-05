package com.dung.democrm.service.impl;

import com.dung.democrm.common.enums.Role;
import com.dung.democrm.common.enums.UserStatus;
import com.dung.democrm.common.exception.BadRequestException;
import com.dung.democrm.common.exception.ResourceNotFoundException;
import com.dung.democrm.dto.request.CustomerRequest;
import com.dung.democrm.dto.response.CustomerResponse;
import com.dung.democrm.entity.Customer;
import com.dung.democrm.entity.User;
import com.dung.democrm.mapper.CustomerMapper;
import com.dung.democrm.repository.CustomerRepository;
import com.dung.democrm.repository.UserRepository;
import com.dung.democrm.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

        return owner;
    }
}
