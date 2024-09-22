package com.sritel.smtp_mail_server.services;

import com.sritel.smtp_mail_server.dto.EmailDetailsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendSimpleMail(EmailDetailsDto details) {
        try {
            LOGGER.info("Sending a simple mail without attachment file");
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getEmailBody());
            mailMessage.setSubject(details.getEmailSubject());

            javaMailSender.send(mailMessage);
        } catch (Exception exception) {
            LOGGER.error("Error while sending the mail", exception);
            throw new RuntimeException("Error while sending the mail: " + exception.getMessage());
        }
    }
}
