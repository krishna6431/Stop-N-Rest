package com.stopnrest.userservice.dto;

import lombok.Data;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;

@Data
public class UserPubDTO {
    @Id
    private String userName;
    private String firstName;
    private String lastName;

    private String email;
    private String mobileNumber;
    private List<CreditCardDTO> creditCards;
    private List<StayInfoDTO> stayHistory;

}
