package com.sritel.pub_sub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sritel.pub_sub.services.*;

import java.util.Map;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestBody Map<String, String> billingDetails) {
        String message = "User: " + billingDetails.get("email") + " has paid " + billingDetails.get("amount");
        kafkaProducerService.sendBillingDetails(message);
        return ResponseEntity.ok("Message published successfully!");
    }
}
