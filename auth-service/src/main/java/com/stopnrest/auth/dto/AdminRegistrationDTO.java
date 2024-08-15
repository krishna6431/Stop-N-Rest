package com.stopnrest.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class AdminRegistrationDTO {
    private String userName;
    private String password;
    private String email;
    private String mobileNumber;
    private String role;
    private Set<Long> hotelIds;
}
