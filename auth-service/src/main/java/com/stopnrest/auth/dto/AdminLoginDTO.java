package com.stopnrest.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminLoginDTO {
    private String userName;
    private String password;
}
