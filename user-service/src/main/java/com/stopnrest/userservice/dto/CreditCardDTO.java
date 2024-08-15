package com.stopnrest.userservice.dto;

import lombok.Data;

@Data
public class CreditCardDTO {
    private String cardNumber;
    private String expireDate;
    private String name;
    private String cardType;
}
