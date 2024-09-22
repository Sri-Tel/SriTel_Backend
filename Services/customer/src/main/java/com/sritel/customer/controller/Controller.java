package com.sritel.customer.controller;

import com.sritel.customer.entity.Customer;
import com.sritel.customer.enums.UserGroup;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.sritel.customer.DTO.CustomerResponse;

import com.sritel.customer.service.CustomerService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.sritel.customer.DTO.CustomerRequest;


@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class Controller {
    
    private final CustomerService service;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody @Valid Customer request) {
        return ResponseEntity.ok(service.createCustomer(request));
    }

    @PutMapping
    public ResponseEntity<String> updateCustomer(@RequestBody @Valid CustomerRequest request) {
        return ResponseEntity.ok(service.updateCustomer(request));
    }

    @GetMapping()
    public ResponseEntity<List<CustomerResponse>> getCustomers() {
        return ResponseEntity.ok(service.getAllCustomers());
    }

    @GetMapping("/{email}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable String email) {
        CustomerResponse response = service.getCustomerByID(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("userGroup/{userGroup}")
    public ResponseEntity<List<Customer>> getCustomerByUserGroup(@PathVariable UserGroup userGroup) {
        List<Customer> response = service.getCustomerByUserGroup(userGroup);
        return ResponseEntity.ok(response);
    }

    
}
