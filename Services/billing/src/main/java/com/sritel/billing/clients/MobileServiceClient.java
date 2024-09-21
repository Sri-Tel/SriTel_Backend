package com.sritel.billing.clients;

import com.sritel.sritel_services.Response.CustomerServicesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "sritel-service")
public interface MobileServiceClient {

    @GetMapping("/api/v1/service/getServicesByUser/{userId}")
    List<CustomerServicesResponse> getServicesByUser(@PathVariable String userId);
}
