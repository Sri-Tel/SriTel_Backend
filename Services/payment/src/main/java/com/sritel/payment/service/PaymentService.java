package com.sritel.payment.service;

import com.sritel.payment.DTO.BillDTO;
import com.sritel.payment.DTO.PaymentDTO;
import com.sritel.payment.entity.Bill;
import com.sritel.payment.entity.Payment;
import com.sritel.payment.repository.PaymentRepository;
import com.sritel.payment.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BillRepository billRepository;

    public Payment processPayment(PaymentDTO paymentDTO) {
        // Find the associated bill by billId
        Optional<Bill> billOptional = billRepository.findById(paymentDTO.getBillId());
        if (billOptional.isEmpty()) {
            throw new RuntimeException("Bill not found");
        }

        Bill bill = billOptional.get();

        Payment payment = Payment.builder()
                .orderId(paymentDTO.getOrderId())
                .amount(Double.parseDouble(paymentDTO.getAmount()))  // Convert amount to double
                .hash(paymentDTO.getHash())
                .paymentStatus("PENDING")  // Set as pending initially
                .billId(bill.getBillId())  // Link the payment to the bill
                .build();

        // Save the payment in the database
        return paymentRepository.save(payment);
    }

    // Handle notification from PayHere on payment completion
    public void handlePayHereNotification(String orderId, String statusCode) {
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderId);
        if (paymentOptional.isEmpty()) {
            throw new RuntimeException("Payment not found");
        }

        Payment payment = paymentOptional.get();
        if ("2".equals(statusCode)) {  // Status code 2 indicates success
            payment.setPaymentStatus("SUCCESS");

            // Update the associated bill to "PAID"
            Optional<Bill> billOptional = billRepository.findById(payment.getBillId());
            if (billOptional.isPresent()) {
                Bill bill = billOptional.get();
                bill.setStatus("PAID");
                billRepository.save(bill);
            }
        } else {
            payment.setPaymentStatus("FAILED");
        }

        // Save the updated payment status
        paymentRepository.save(payment);
    }

    public PaymentDTO getPaymentByOrderId(String orderId) {
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderId);
        if (paymentOptional.isEmpty()) {
            throw new RuntimeException("Payment not found for order ID: " + orderId);
        }

        Payment payment = paymentOptional.get();
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setOrderId(payment.getOrderId());
        paymentDTO.setHash(payment.getHash());
        paymentDTO.setAmount(payment.getAmount().toString());

        return paymentDTO;
    }

    public BillDTO getBillById(String billId) {
        Optional<Bill> billOptional = billRepository.findById(billId);

        if (billOptional.isEmpty()) {
            throw new RuntimeException("Bill not found for ID: " + billId);
        }

        System.out.println("Bill found for ID: " + billId);

        Bill bill = billOptional.get();

        BillDTO billDTO = new BillDTO();
        billDTO.setBillId(bill.getBillId());
        billDTO.setUserId(bill.getUserId());

        BigDecimal amount = new BigDecimal(bill.getAmount()).setScale(6, RoundingMode.HALF_UP);
        billDTO.setAmount(amount.toString());

        billDTO.setStatus(bill.getStatus());
        billDTO.setBillingDate(bill.getBillingDate());
        billDTO.setDueDate(bill.getDueDate());

        return billDTO;
    }

    public void updateBillStatus(String billId, String status) {
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isEmpty()) {
            throw new RuntimeException("Bill not found for ID: " + billId);
        }

        Bill bill = billOptional.get();
        bill.setStatus(status);
        billRepository.save(bill);
    }

}
