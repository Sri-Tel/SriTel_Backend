package com.sritel.payment.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "bill")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Bill {

    @Id
    private String billId;

    private String userId;
    private String amount;

    private String status = "PENDING";

    private LocalDateTime billingDate;
    private LocalDateTime dueDate;
}
