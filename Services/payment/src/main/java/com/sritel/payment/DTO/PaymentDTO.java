package com.sritel.payment.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDTO {
    private String orderId;
    private String amount;
    private String paymentStatus;
    private String hash;
    private String  billId;
}
