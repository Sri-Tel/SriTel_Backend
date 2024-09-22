package com.sritel.customer.repository;
import com.sritel.customer.entity.Customer;
import com.sritel.customer.enums.UserGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    Customer findCustomerById(String email);

    long countByUserGroup(String group1);

    List<Customer> findByUserGroup(UserGroup userGroup);
}
