package com.sritel.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BillResponse {
    private String id;
    private String userId;
    private BigDecimal amount;
    private String invoiceNumber;
    private String status;
    private LocalDate billingDate;
    private LocalDate dueDate;
}
