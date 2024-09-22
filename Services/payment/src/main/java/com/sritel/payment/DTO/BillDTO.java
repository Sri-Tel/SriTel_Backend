package com.sritel.payment.DTO;


import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class BillDTO {
    private String billId;
    private String amount;
    private String status;
    private String userId;
    private LocalDateTime billingDate;
    private LocalDateTime dueDate;
}