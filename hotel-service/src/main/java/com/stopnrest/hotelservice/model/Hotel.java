package com.stopnrest.hotelservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelId;

    @NotBlank(message = "Hotel name is mandatory")
    private String hotelName;

    @NotBlank(message = "Location is mandatory")
    private String hotelLocation;

    @NotBlank(message = "Description is mandatory")
    private String hotelDesc;

    @NotNull(message = "Rating cannot be null")
    private double hotelRating;

    @NotBlank(message = "Address is mandatory")
    private String hotelAddress;

    @NotBlank(message = "Contact number is mandatory")
    private String hotelContactNumber;

    @Email(message = "Email should be valid")
    private String hotelEmail;

    private String hotelWebsite;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> hotelRooms;
}
