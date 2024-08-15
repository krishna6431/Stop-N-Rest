package com.stopnrest.hotelservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomDTO {

    private Long roomId;
    private Long hotelId;
    private Long roomTypeId;
    private String roomNumber;
    private double price;
    private boolean roomAvailable;
}
