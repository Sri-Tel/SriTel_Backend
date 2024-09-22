package com.sritel.smtp_mail_server.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetailsDto {

    private String recipient;
    private String emailBody;
    private String emailSubject;
}
