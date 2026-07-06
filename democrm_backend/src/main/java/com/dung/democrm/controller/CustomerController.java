package com.dung.democrm.controller;

import com.dung.democrm.dto.request.CustomerOwnerRequest;
import com.dung.democrm.dto.request.CustomerRequest;
import com.dung.democrm.dto.request.CustomerSearchRequest;
import com.dung.democrm.dto.response.CustomerDetailResponse;
import com.dung.democrm.dto.response.CustomerResponse;
import com.dung.democrm.entity.Customer;
import com.dung.democrm.service.CustomerService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping()
    public ResponseEntity<Page<CustomerResponse>> getAllCustomers(Pageable pageable){
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable("id") Long id){
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerService.createCustomer(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable("id") Long id,
            @Valid @RequestBody CustomerRequest request
            ){
        return ResponseEntity.ok(customerService.updateCustomer(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id){
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CustomerResponse>> searchCustomers(
            CustomerSearchRequest request,
            Pageable pageable
    ){
        return ResponseEntity.ok(customerService.searchCustomers(request,pageable));
    }

    @GetMapping("/my-customers")
    public ResponseEntity<List<CustomerResponse>> getMyCustomers(){
        return ResponseEntity.ok(customerService.getMyCustomers());
    }

    @PatchMapping("/{id}/assign-owner")
    public ResponseEntity<CustomerResponse> assignOwner(@PathVariable("id") Long id,
                                                        @Valid @RequestBody CustomerOwnerRequest request){
        return ResponseEntity.ok(customerService.assignOwner(id, request));
    }

    @PatchMapping("/{id}/transfer-owner")
    public ResponseEntity<CustomerResponse> transferOwner(@PathVariable("id") Long id,
                                                          @Valid @RequestBody CustomerOwnerRequest request){
        return ResponseEntity.ok(customerService.transferOwner(id, request));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<CustomerDetailResponse> getCustomerDetail(
        @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(customerService.getCustomerDetail(id));
    }
}
