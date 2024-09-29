package com.sritel.billing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sritel.billing.entity.Bills;
import com.sritel.billing.services.BillingService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor

public class Controller {

    @Autowired
    private BillingService billingService;


    @PostMapping
    public ResponseEntity<Bills> createBill(@RequestBody Bills billsRequest) {
        Bills response = billingService.createBilling(billsRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Bills>> getBills() {
        List<Bills> response = billingService.getAllBillings();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<List<Bills>> getByUserId(@PathVariable String user_id){
        List<Bills> response = billingService.getBillingsByUserId(user_id);
        System.out.println("response: " + response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/create/{user_id}")
    public void createBills(@PathVariable String userId){
        billingService.getBillingsByUserId(userId);
    }

    @PatchMapping("/updatePayment/{bill_id}")
    public ResponseEntity<String> updatePayment(@PathVariable String bill_id){
        System.out.println("bill_id: " + bill_id);
        billingService.updateBillPayment(bill_id);
        return ResponseEntity.ok("ok");
    }
}
