package com.sritel.customer.DTO;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerRequest {
    
    @NotNull(message = "Name cannot be null")
    private String email;
    @NotNull(message = "SritelNo cannot be null")
    private String sritelNo;
    
}

