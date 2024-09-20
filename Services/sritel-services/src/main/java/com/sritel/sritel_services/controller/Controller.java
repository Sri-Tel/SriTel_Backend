package com.sritel.sritel_services.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class Controller {

    private final CustomersServiceService service;

    @PostMapping("/addCustomerService")
    public ResponseEntity<CustomersService> addCustomerService(@RequestBody CustomersService customersService) {
        return ResponseEntity.ok(service.addCustomerService(customersService));
    }
  
    
    
}
