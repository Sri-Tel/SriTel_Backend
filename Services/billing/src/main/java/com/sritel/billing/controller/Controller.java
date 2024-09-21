package com.sritel.billing.controller;

import com.sritel.billing.entity.Bills;
import com.sritel.billing.services.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
}
