package com.sritel.sritel_services.Response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerServicesResponse {
    private String serviceId;
    private String serviceName;
    private String serviceDescription;
    private String serviceStatus;
    private BigDecimal serviceAmount;

    private LocalDate serviceStartDate;
    private LocalDate serviceEndDate;


}
