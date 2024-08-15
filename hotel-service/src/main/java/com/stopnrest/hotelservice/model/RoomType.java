package com.stopnrest.hotelservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "room_types")
@NoArgsConstructor
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;

    @NotBlank(message = "Type is mandatory")
    private String type;

    private String description;

    private String features;

    @NotNull(message = "Capacity cannot be null")
    private Integer capacity;
}
