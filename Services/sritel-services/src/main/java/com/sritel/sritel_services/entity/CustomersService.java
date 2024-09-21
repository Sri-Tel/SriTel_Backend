package com.sritel.sritel_services.entity;

import com.sritel.sritel_services.enums.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "customersServices")
public class CustomersService {

    @Id
    private String id;
    private String customerId;
    private String serviceId;


    @Builder.Default
    private LocalDate serviceStartDate = LocalDate.now();
    
    private LocalDate serviceEndDate;

    private ServiceStatus serviceStatus;

}
