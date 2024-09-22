package com.sritel.customer.helper;

import org.springframework.stereotype.Service;

import com.sritel.customer.entity.Customer;
import com.sritel.customer.DTO.CustomerRequest;
import com.sritel.customer.DTO.CustomerResponse;

@Service
public class CustomerMapper {
    
    public Customer toCustomer(CustomerRequest request) {
        if (request == null) {
            return null;
        }
        return Customer.builder()
                .email(request.getEmail())
                .sritelNo(request.getSritelNo())
                .build();
    }

    public CustomerResponse fromCustomer(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerResponse(customer.getId(), customer.getEmail(), customer.getSritelNo());
    }
}
