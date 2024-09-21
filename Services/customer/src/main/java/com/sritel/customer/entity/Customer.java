package com.sritel.customer.entity;
import com.sritel.customer.enums.UserGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "customer")
public class Customer {

    @Id
    private String id;
    private String name;
    private String sritelNo;
    private UserGroup userGroup;
    
    
}
