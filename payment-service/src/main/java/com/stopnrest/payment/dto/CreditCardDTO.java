package com.stopnrest.payment.dto;

import lombok.Data;

@Data
public class CreditCardDTO {
    private String cardNumber;
    private String expireDate;
    private String name;
    private String cardType;
}
    // Getters and Setters
//    public String getCardNumber() {
//        return cardNumber;
//    }
//
//    public void setCardNumber(String cardNumber) {
//        this.cardNumber = cardNumber;
//    }
//
//    public String getExpireDate() {
//        return expireDate;
//    }
//
//    public void setExpireDate(String expireDate) {
//        this.expireDate = expireDate;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getCardType() {
//        return cardType;
//    }
//
//    public void setCardType(String cardType) {
//        this.cardType = cardType;
//    }
//}
//
