package com.stopnrest.hotelservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class HotelDTO {

    private Long hotelId;
    private String hotelName;
    private String hotelLocation;
    private String hotelDesc;
    private double hotelRating;
    private String hotelAddress;
    private String hotelContactNumber;
    private String hotelEmail;
    private String hotelWebsite;
    private List<RoomDTO> hotelRooms;
}
