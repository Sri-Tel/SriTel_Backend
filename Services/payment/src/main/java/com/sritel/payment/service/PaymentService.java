package com.sritel.payment.service;

import com.sritel.payment.DTO.BillDTO;
import com.sritel.payment.DTO.PaymentDTO;
import com.sritel.payment.entity.Bill;
import com.sritel.payment.entity.Payment;
import com.sritel.payment.repository.PaymentRepository;
import com.sritel.payment.repository.BillRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .amount(Double.parseDouble(paymentDTO.getAmount()))
                .hash(paymentDTO.getHash())
                .paymentStatus("PENDING")
                .billId(bill.getBillId())
                .build();

        return paymentRepository.save(payment);
    }

    // Handle notification from PayHere on payment completion
    public void handlePayHereNotification(String orderId, String statusCode) {
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderId);
        if (paymentOptional.isEmpty()) {
            throw new RuntimeException("Payment not found");
        }

        Payment payment = paymentOptional.get();
        if ("2".equals(statusCode)) {
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
        ObjectId objectId;
        try {
            objectId = new ObjectId(billId);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Bill ID format: " + billId);
        }

        Optional<Bill> billOptional = billRepository.findById(objectId.toString());

        if (billOptional.isEmpty()) {
            throw new RuntimeException("Bill not found for ID: " + billId);
        }

        Bill bill = billOptional.get();

        BillDTO billDTO = new BillDTO();
        billDTO.setBillId(bill.getBillId());
        billDTO.setUserId(bill.getUserId());
        billDTO.setAmount(bill.getAmount());
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

    public List<BillDTO> getAllBills() {
        List<Bill> bills = billRepository.findAll();

        return bills.stream().map(bill -> BillDTO.builder()
                        .billId(bill.getBillId())
                        .userId(bill.getUserId())
                        .amount(bill.getAmount())
                        .status(bill.getStatus())
                        .billingDate(bill.getBillingDate())
                        .dueDate(bill.getDueDate())
                        .build())
                .collect(Collectors.toList());
    }


    //Create a payment DTO from amount and billDTO
    public PaymentDTO createPaymentDTO(String amount, BillDTO billDTO) {
        return PaymentDTO.builder()
                .orderId(Long.toString(System.currentTimeMillis()))
                .amount(amount)
                .paymentStatus("PENDING")
                .billId(billDTO.getBillId())
                .build();
    }

    //Create a Payment object from the PaymentDTO
    public Payment createPayment(PaymentDTO paymentDTO) {
        return Payment.builder()
                .orderId(paymentDTO.getOrderId())
                .amount(Double.parseDouble(paymentDTO.getAmount()))
                .paymentStatus(paymentDTO.getPaymentStatus())
                .billId(paymentDTO.getBillId())
                .build();
    }

    //Save the payment details to the database
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
}
