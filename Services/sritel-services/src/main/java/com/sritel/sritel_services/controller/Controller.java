package com.sritel.sritel_services.controller;

import com.sritel.sritel_services.Response.CustomerServicesResponse;
import com.sritel.sritel_services.entity.CustomersService;
import com.sritel.sritel_services.entity.Service;
import com.sritel.sritel_services.requests.UpdateCustomerServiceRequest;
import com.sritel.sritel_services.services.CustomersServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;


@RestController
@RequestMapping("/api/v1/service")
@RequiredArgsConstructor
public class Controller {


    private final CustomersServiceService service;

    @PostMapping("/addCustomerService")
    public ResponseEntity<CustomersService> addCustomerService(@RequestBody CustomersService customersService) {
        return ResponseEntity.ok(service.addCustomerService(customersService));
    }

    @PostMapping("/addService")
    public ResponseEntity<Service> addService(@RequestBody Service serviceReq) {
        return ResponseEntity.ok(service.addService(serviceReq));
    }

    // custometr id eka awama services history @getmaping (/getservices) (services + customer service dto)
    @GetMapping("/getServicesByUser/{userId}")
    public ResponseEntity<List<CustomerServicesResponse>> getServicesByUser(@PathVariable String userId) {
        List<CustomerServicesResponse> responses = service.getServicesByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    //customer id eka ewwama service active or inactive (object customer, service id, status)
    @PatchMapping("/updateCustomerService")
    public ResponseEntity<CustomersService> updateCustomerService(@RequestBody UpdateCustomerServiceRequest customersServiceRequest) {
        CustomersService res = service.updateCustomerServiceStatus(customersServiceRequest);
        return ResponseEntity.ok(res);
    }

}
