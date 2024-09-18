package com.sritel.customer.service;

import org.springframework.stereotype.Service;
import com.sritel.customer.DTO.CustomerRequest;
import com.sritel.customer.repository.CustomerRepository; 
import com.sritel.customer.helper.CustomerMapper;  
import com.sritel.customer.entity.Customer;
import com.sritel.customer.DTO.CustomerResponse;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public String createCustomer(CustomerRequest request) {
       var customer = repository.save(mapper.toCustomer(request));
         return customer.getId();
    }

    public String updateCustomer(CustomerRequest request) {
        var customer = repository.findById(request.getSritelNo());
        if (customer.isPresent()) {
            mergeCustomer(customer.get(), request);
           repository.save(customer.get());
        }
        return "Customer not found";
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
       if(request.getName() != null) {
           customer.setName(request.getName());
           
       }
         if(request.getSritelNo() != null) {
              customer.setSritelNo(request.getSritelNo());
         }

    }

    public List<CustomerResponse> getAllCustomers() {
        return repository.findAll().stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }
    
}
