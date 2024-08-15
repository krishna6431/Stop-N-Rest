package com.stopnrest.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegistrationDTO {
    private String userName;
    private String password;
    private String email;
    private String mobileNumber;
}
