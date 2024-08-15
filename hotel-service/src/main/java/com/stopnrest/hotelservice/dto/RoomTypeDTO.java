package com.stopnrest.hotelservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomTypeDTO {

    private Long roomTypeId;
    private String type;
    private String description;
    private String features;
    private Integer capacity;
}
