package com.sritel.billing.dto;



import com.sritel.billing.enums.UserGroup;
import lombok.*;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Customer {

    @Id
    private String id;
    private String email;
    private String sritelNo;
    private UserGroup userGroup;

}
