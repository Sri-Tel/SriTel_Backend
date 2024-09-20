package com.sritel.payment.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDTO {
    private String orderId;
    private String amount;
    private String hash;
    private String  billId;
}
