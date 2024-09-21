package com.sritel.payment.controller;

import com.sritel.payment.DTO.BillDTO;
import com.sritel.payment.DTO.PaymentDTO;
import com.sritel.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private String merchantId = "1226457";
    private String merchantSecret = "Mzk1NTA4MDI3NjUzNDU4ODA2MDIyMjEyMDMyMzIyMDUxMjI5MzY5";

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Get bill by billId
    @GetMapping("/bill/{billId}")
    public ResponseEntity<?> getBillById(@PathVariable String billId) {
        try {
            BillDTO billDTO = paymentService.getBillById(billId);
            return ResponseEntity.ok(billDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bill not found");
        }
    }

    // Get all bills
    @GetMapping("/bills")
    public ResponseEntity<List<BillDTO>> getAllBills() {
        List<BillDTO> bills = paymentService.getAllBills();
        return ResponseEntity.ok(bills);
    }

    // Process payment
    @PostMapping("/processPayment")
    public ResponseEntity<?> processPayment(@RequestBody Map<String, String> paymentData) {

        String billId = paymentData.get("billId");
        String amount = paymentData.get("amount");

        if (billId == null || amount == null || billId.isEmpty() || amount.isEmpty()) {
            return ResponseEntity.badRequest().body("Bill ID or Amount is missing.");
        }

        BillDTO billDTO = new BillDTO();
        try {
            billDTO = paymentService.getBillById(billId);
        } catch (Exception e) {
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
        if (billDTO == null) {
            return ResponseEntity.ok(" Bill not found");
        }

        BigDecimal billAmount = new BigDecimal(billDTO.getAmount()).setScale(6, RoundingMode.HALF_UP);
        BigDecimal paymentAmount = new BigDecimal(amount).setScale(6, RoundingMode.HALF_UP);

        if (paymentAmount.compareTo(billAmount) != 0) {
            if (paymentAmount.compareTo(billAmount) > 0) {
                paymentService.updateBillStatus(billId, "PAID");
                return ResponseEntity.ok("Bill successfully paid with amount: " + amount);
                //Add the overpayment to the user's account
            } else {
                paymentService.updateBillStatus(billId, "PARTIALLY_PAID");
                return ResponseEntity.ok("Bill partially paid with amount: " + amount);
                //Add the partial payment to the user's account
            }
        }
        else {
            paymentService.updateBillStatus(billId, "PAID");
            return ResponseEntity.ok("Bill successfully paid with amount: " + amount);
        }
    }

    @PostMapping("/calculateHash")
    public ResponseEntity<PaymentDTO> calculateHash(@RequestBody Map<String, Object> requestData) {

        double amount = Double.parseDouble(requestData.get("amount").toString());
        double amounts = amount;
        String orderId = Long.toString(System.currentTimeMillis());
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedAmount = df.format(amount);

        String hash = getMd5(merchantId + orderId + formattedAmount + "LKR" + getMd5(merchantSecret));

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setOrderId(orderId);
        paymentDTO.setHash(hash);
        paymentDTO.setAmount(String.format("%.2f", amounts));

        return ResponseEntity.ok(paymentDTO);
    }


    // Helper method to compute MD5 hash
    private String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
