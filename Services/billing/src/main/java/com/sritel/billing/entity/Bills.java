package com.sritel.billing.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "bill")
public class Bills {
    @Id
    private String id;
    private String userId;
    private BigDecimal amount;
    private String invoiceNumber;
    private String status;
    private LocalDate billingDate;
    private LocalDate dueDate;
}
