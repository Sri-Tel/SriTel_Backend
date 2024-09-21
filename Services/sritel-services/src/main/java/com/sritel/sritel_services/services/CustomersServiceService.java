package com.sritel.sritel_services.services;

import com.sritel.sritel_services.Response.CustomerServicesResponse;
import com.sritel.sritel_services.entity.CustomersService;
import com.sritel.sritel_services.entity.Service;
import com.sritel.sritel_services.repository.CustomerServiceRepositary;
import com.sritel.sritel_services.repository.ServiceRepositary;
import com.sritel.sritel_services.requests.UpdateCustomerServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class CustomersServiceService {

    private final ServiceRepositary serviceRepositary;
    private final CustomerServiceRepositary customersServiceRepositary;


    public CustomersService addCustomerService(CustomersService customersService) {
        return  customersServiceRepositary.save(customersService);
    }

    public Service addService(Service service) {
        return serviceRepositary.save(service);
    }

    public List<CustomerServicesResponse> getServicesByUserId(String userId) {
        List<CustomersService> customerServices = customersServiceRepositary.findByCustomerId(userId);

        List<CustomerServicesResponse> responses = customerServices.stream().map(customersService -> {
            Service service = serviceRepositary.findById(customersService.getServiceId())
                    .orElse(null); // Handle the case if service is not found

            return CustomerServicesResponse.builder()
                    .serviceId(customersService.getServiceId())
                    .serviceName(service != null ? service.getServiceName() : null)
                    .serviceDescription(service != null ? service.getDescription() : null)
                    .serviceStatus(customersService.getServiceStatus().name()) // Assuming ServiceStatus is an enum
                    .serviceAmount(service != null ? service.getPrice() : null)
                    .serviceStartDate(customersService.getServiceStartDate())
                    .serviceEndDate(customersService.getServiceEndDate())
                    .build();
        }).toList();

        return responses;

    }

    @Transactional
    public CustomersService updateCustomerServiceStatus(UpdateCustomerServiceRequest request) {
        // Fetch the customer service based on customerId and serviceId
        Optional<CustomersService> optionalCustomerService = customersServiceRepositary.findByCustomerIdAndServiceId(request.getCustomerId(), request.getServiceId());

        if (optionalCustomerService.isPresent()) {
            CustomersService customerService = optionalCustomerService.get();

            // Update the service status
            customerService.setServiceStatus(request.getServiceStatus());

            // Save and return the updated customer service
            return customersServiceRepositary.save(customerService);
        } else {
            throw new RuntimeException("Customer service not found for the given customerId and serviceId");
        }
    }
}
