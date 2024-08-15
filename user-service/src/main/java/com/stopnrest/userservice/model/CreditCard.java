package com.stopnrest.userservice.model;

import lombok.Data;

@Data
public class CreditCard {
    private String cardNumber;
    private String expireDate;
    private String name;
    private String cardType;
}

