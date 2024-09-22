package com.sritel.sritel_services.repository;

import com.sritel.sritel_services.Response.CustomerServicesResponse;
import com.sritel.sritel_services.entity.CustomersService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CustomerServiceRepositary extends MongoRepository<CustomersService,String> {
    List<CustomersService> findByCustomerId(String userId);

    Optional<CustomersService> findByCustomerIdAndServiceId(String customerId, String serviceId);
}
