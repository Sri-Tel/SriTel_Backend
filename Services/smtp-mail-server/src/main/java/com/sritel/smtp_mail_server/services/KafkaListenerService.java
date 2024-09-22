package com.sritel.smtp_mail_server.services;

import com.sritel.smtp_mail_server.dto.EmailDetailsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.sritel.smtp_mail_server.services.SendMailService;

@Service
public class KafkaListenerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMailService.class);

    @Autowired
    private SendMailService sendMailService;

    @KafkaListener(topics = "billing-topic", groupId = "email-notification-group")
    public void listenBillingTopic(String message) {
        // Assume message format: "User: user_email has paid amount"
        String[] parts = message.split(" ");
        String email = parts[1];
        String amount = parts[4];

        EmailDetailsDto emailDetails = new EmailDetailsDto();
        emailDetails.setRecipient(email);
        emailDetails.setEmailSubject("Payment Confirmation");
        emailDetails.setEmailBody("Dear User, your payment of " + amount + " has been received successfully.");

        sendMailService.sendSimpleMail(emailDetails);
//        LOGGER.info(emailDetails.getEmailBody());
    }
}
