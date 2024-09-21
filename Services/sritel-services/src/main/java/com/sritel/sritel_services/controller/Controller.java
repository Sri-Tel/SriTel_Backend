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
  
    // custometr id eka awama services history @getmaping (/getservices) (services + customer service dto)
    //customer id eka ewwama service active or inactive @postmapping (object customer, service id, status)



    
    
    
}
