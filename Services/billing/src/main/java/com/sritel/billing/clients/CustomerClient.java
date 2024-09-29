package com.sritel.billing.clients;


import com.sritel.billing.dto.Customer;
import com.sritel.billing.enums.UserGroup;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name = "customer-service")
public interface CustomerClient {

    @GetMapping("/api/v1/customer/{userId}")
    Customer getCustomerById(@PathVariable("userId") String userId);

    @GetMapping("api/v1/customer/getCustomerBySritelNo/{sritelNo}")
    Customer getCustomerBySritelNo(@PathVariable("sritelNo") String sritelNo);

    @GetMapping("/api/v1/customer")
    List<Customer> getCustomers();

    @GetMapping("/api/v1/customer/userGroup/{userGroup}")
    List<Customer> getUsersByGroup(@PathVariable UserGroup userGroup);
}
