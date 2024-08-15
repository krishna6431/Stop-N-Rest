package com.stopnrest.hotelservice.controller;

import com.stopnrest.hotelservice.claims.JwtClaims;
import com.stopnrest.hotelservice.client.UserServiceClient;
import com.stopnrest.hotelservice.dto.RoomDTO;
import com.stopnrest.hotelservice.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private JwtClaims jwtClaims;

    @Autowired
    private UserServiceClient userServiceClient;

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        List<RoomDTO> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        Optional<RoomDTO> roomDTO = roomService.getRoomById(id);
        return roomDTO
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(new RoomDTO()));
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomDTO>> getRoomsByHotelId(@PathVariable Long hotelId) {
        List<RoomDTO> rooms = roomService.getRoomsByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/p/")
    public ResponseEntity<String> createRoom(@Valid @RequestBody RoomDTO roomDTO, @RequestHeader("Authorization") String authHeader) {
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        String username = credentials[1];

        ResponseEntity<?> responseEntity = userServiceClient.getUserByUsername(authHeader, username);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        List<Integer> hotelIds = (List<Integer>) responseBody.get("hotelIds");
        if(hotelIds == null){
            return ResponseEntity.status(403).body("You are not authorized to create a room for HotelId " + roomDTO.getHotelId());
        }
        if (!hotelIds.contains(roomDTO.getHotelId().intValue()) && !(hotelIds.get(0) == 0)) {
            return ResponseEntity.status(403).body("Unauthorized to create a room for HotelId " + roomDTO.getHotelId());
        }

        RoomDTO createdRoom = roomService.createRoom(roomDTO);
        return ResponseEntity.status(201).body("Room created successfully with ID " + createdRoom.getRoomId() + ".");
    }

    @PutMapping("/u/{id}")
    public ResponseEntity<String> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomDTO roomDTO, @RequestHeader("Authorization") String authHeader) {
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        String username = credentials[1];

        ResponseEntity<?> responseEntity = userServiceClient.getUserByUsername(authHeader, username);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        List<Integer> hotelIds = (List<Integer>) responseBody.get("hotelIds");
        if(hotelIds == null){
            return ResponseEntity.status(403).body("You are not authorized to update a room for HotelId " + roomDTO.getHotelId());
        }
        if (!hotelIds.contains(roomDTO.getHotelId().intValue()) && !(hotelIds.get(0) == 0)) {
            return ResponseEntity.status(403).body("Unauthorized to update a room for HotelId " + roomDTO.getHotelId());
        }

        RoomDTO updatedRoom = roomService.updateRoom(id, roomDTO);
        return updatedRoom != null
                ? ResponseEntity.ok("Room updated successfully with ID " + updatedRoom.getRoomId() + ".")
                : ResponseEntity.status(404).body("Room with ID " + id + " not found.");
    }

    @DeleteMapping("/d/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        String username = credentials[1];

        ResponseEntity<?> responseEntity = userServiceClient.getUserByUsername(authHeader, username);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        List<Integer> hotelIds = (List<Integer>) responseBody.get("hotelIds");
        if(hotelIds == null){
            return ResponseEntity.status(403).body("You are not authorized to create a room with RoomId " + id);
        }
        Optional<RoomDTO> optionalRoom = roomService.getRoomById(id);
        if (optionalRoom.isPresent()) {
            RoomDTO roomDTO = optionalRoom.get();
            if (!hotelIds.contains(roomDTO.getHotelId().intValue()) && !(hotelIds.get(0) == 0)) {
                return ResponseEntity.status(403).body("Unauthorized to delete a room for HotelId " + roomDTO.getHotelId());
            }

            if (roomService.deleteRoom(id)) {
                return ResponseEntity.status(200).body("Room with ID " + id + " deleted successfully.");
            } else {
                return ResponseEntity.status(404).body("Room with ID " + id + " not found.");
            }
        } else {
            return ResponseEntity.status(404).body("Room with ID " + id + " not found.");
        }
    }

    @GetMapping("/hotel/{hotelId}/{roomId}")
    public ResponseEntity<?> getAvailabilityOfRoom(@RequestHeader("Authorization") String authHeader, @PathVariable("hotelId") Long hotelId, @PathVariable("roomId") Long roomId) {
        return roomService.getRoomAvailableByHotel(hotelId, roomId);
    }

    @PutMapping("/update/room/{roomId}")
    public ResponseEntity<String> updateRoomStatus(@RequestHeader("Authorization") String authHeader, @PathVariable("roomId") Long roomId) {
        boolean statusUpdated = roomService.updateRoomStatus(roomId).hasBody();
        return statusUpdated
                ? ResponseEntity.ok("Room status updated successfully for room ID " + roomId + ".")
                : ResponseEntity.status(404).body("Room with ID " + roomId + " not found.");
    }
}
