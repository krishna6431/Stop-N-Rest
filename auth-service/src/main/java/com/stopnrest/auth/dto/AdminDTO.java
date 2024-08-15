package com.stopnrest.auth.dto;

import com.stopnrest.auth.model.Admin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {
    private Long id;
    private String userName;
    private String email;
    private String mobileNumber;
    private String role;
    private Set<Long> hotelIds;

    public AdminDTO(Admin admin, Set<Long> hotels) {
        this.id = admin.getId();
        this.userName = admin.getUserName();
        this.email = admin.getEmail();
        this.mobileNumber = admin.getMobileNumber();
        this.role = admin.getRole();
        this.hotelIds = hotels;
    }
}
