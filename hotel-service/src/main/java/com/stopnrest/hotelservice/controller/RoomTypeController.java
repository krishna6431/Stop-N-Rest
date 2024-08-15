package com.stopnrest.hotelservice.controller;

import com.stopnrest.hotelservice.claims.JwtClaims;
import com.stopnrest.hotelservice.client.UserServiceClient;
import com.stopnrest.hotelservice.dto.RoomTypeDTO;
import com.stopnrest.hotelservice.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/roomtypes")
public class RoomTypeController {

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private JwtClaims jwtClaims;

    @Autowired
    private UserServiceClient userServiceClient;

    @GetMapping
    public ResponseEntity<List<RoomTypeDTO>> getAllRoomTypes() {
        List<RoomTypeDTO> roomTypes = roomTypeService.getAllRoomTypes();
        return ResponseEntity.ok(roomTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomTypeById(@PathVariable Long id) {
       return roomTypeService.getRoomTypeById(id);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<?> getRoomTypesByHotelId(@PathVariable Long hotelId) {
        List<RoomTypeDTO> roomTypes = roomTypeService.getRoomTypesByHotelId(hotelId);
        return ResponseEntity.ok(roomTypes);
    }

    @PostMapping("/p/")
    public ResponseEntity<?> createRoomType(@Valid @RequestBody RoomTypeDTO roomTypeDTO, @RequestHeader("Authorization") String authHeader) {
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        String role = credentials[0];

        if (!role.contains("ADMIN")) {
            return ResponseEntity.status(403).body("Unauthorized: You do not have permission to create a room type.");
        }

        RoomTypeDTO createdRoomType = roomTypeService.createRoomType(roomTypeDTO).getBody();
        return ResponseEntity.status(201).body("Room Type created successfully with ID " + createdRoomType.getRoomTypeId() + ".");
    }

    @PutMapping("/u/{id}")
    public ResponseEntity<?> updateRoomType(@PathVariable Long id, @Valid @RequestBody RoomTypeDTO roomTypeDTO, @RequestHeader("Authorization") String authHeader) {
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        String role = credentials[0];

        if (!role.contains("ADMIN")) {
            return ResponseEntity.status(403).body("Unauthorized: You do not have permission to update this room type.");
        }

        RoomTypeDTO updatedRoomType = roomTypeService.updateRoomType(id, roomTypeDTO).getBody();
        return updatedRoomType != null
                ? ResponseEntity.ok("Room Type updated successfully with ID " + id + ".")
                : ResponseEntity.status(404).body("Room Type with ID " + id + " not found.");
    }

    @DeleteMapping("/d/{id}")
    public ResponseEntity<?> deleteRoomType(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        String role = credentials[0];

        if (!role.contains("ADMIN")) {
            return ResponseEntity.status(403).body("Unauthorized: You do not have permission to delete this room type.");
        }

        boolean isDeleted = roomTypeService.deleteRoomType(id).hasBody();
        return isDeleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(404).body("Room Type with ID " + id + " not found.");
    }
}
