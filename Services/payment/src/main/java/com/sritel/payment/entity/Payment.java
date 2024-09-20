package com.sritel.payment.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Payment {

    @Id
    private String paymentId;

    private String orderId;
    private Double amount;
    private String paymentStatus = "PENDING";
    private String hash;

    private String billId;
}
