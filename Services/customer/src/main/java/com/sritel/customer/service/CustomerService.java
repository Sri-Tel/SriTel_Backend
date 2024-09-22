package com.sritel.customer.service;

import com.sritel.customer.enums.UserGroup;
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



    public Customer createCustomer(Customer customer) {
        // Automatically assign the user to a group
        UserGroup assignedGroup = assignUserGroup();
        customer.setUserGroup(assignedGroup);

        // Save the new customer to the database
        return repository.save(customer);
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
       if(request.getEmail() != null) {
           customer.setEmail(request.getEmail());
           
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

    public CustomerResponse getCustomerByID(String email) {
        Customer customer = repository.findCustomerById(email);
        return mapper.fromCustomer(customer);
    }

    private UserGroup assignUserGroup() {
        long group1Count = repository.countByUserGroup("GROUP1");
        long group2Count = repository.countByUserGroup("GROUP2");

        if (group1Count <= group2Count) {
            return UserGroup.GROUP1;  // Assign to GROUP1 if it's smaller or equal
        } else {
            return UserGroup.GROUP2;  // Otherwise, assign to GROUP2
        }
    }

    public List<Customer> getCustomerByUserGroup(UserGroup userGroup) {
        return repository.findByUserGroup(userGroup);
    }
}
