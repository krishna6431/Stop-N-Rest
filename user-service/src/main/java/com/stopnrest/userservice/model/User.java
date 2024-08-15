package com.stopnrest.userservice.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Data
@Document(collection = "users")
public class User {

    @Id
    @NotBlank(message = "Username cannot be blank")
    private String userName;

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    @Indexed(unique = true)
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Mobile number must be a 10-digit number")
    @Indexed(unique = true)
    private String mobileNumber;

    private List<CreditCard> creditCards;

    private List<StayInfo> stayHistory;

    private Set<Long> hotelIds;

    @NotBlank(message = "Role cannot be blank")
    private String role;
}
