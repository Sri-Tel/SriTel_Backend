package com.sritel.sritel_services.requests;

import com.sritel.sritel_services.enums.ServiceStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateCustomerServiceRequest {
    private String customerId;
    private String serviceId;
    private ServiceStatus serviceStatus;
}
