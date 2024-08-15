package com.stopnrest.hotelservice.controller;

import com.stopnrest.hotelservice.claims.JwtClaims;
import com.stopnrest.hotelservice.client.UserServiceClient;
import com.stopnrest.hotelservice.dto.HotelDTO;
import com.stopnrest.hotelservice.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private JwtClaims jwtClaims;

    @Autowired
    private UserServiceClient userServiceClient;

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllHotels() {
        List<HotelDTO> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHotelById(@PathVariable Long id) {
        Optional<HotelDTO> hotelDTO = hotelService.getHotelById(id);
        return hotelDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{pattern}")
    public ResponseEntity<List<HotelDTO>> searchHotelsByPattern(@PathVariable String pattern) {
        List<HotelDTO> hotels = hotelService.searchHotelsByPattern(pattern);
        return hotels.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(hotels);
    }

    @PostMapping("/p/")
    public ResponseEntity<?> createHotel(@Valid @RequestBody HotelDTO hotelDTO, @RequestHeader("Authorization") String authHeader) {
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        String role = credentials[0];
        String username = credentials[1];

        ResponseEntity<?> responseEntity = userServiceClient.getUserByUsername(authHeader, username);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        List<Integer> hotelIds = (List<Integer>) responseBody.get("hotelIds");
        if(hotelIds == null){
            return ResponseEntity.status(403).body("You are not authorized to create a hotel with HotelId " + hotelDTO.getHotelId());
        }
        if (!hotelIds.contains(hotelDTO.getHotelId().intValue()) && !(hotelIds.get(0) == 0)) {
            return ResponseEntity.status(403).body("You are not authorized to create a hotel with HotelId " + hotelDTO.getHotelId());
        }

        HotelDTO createdHotel = hotelService.createHotel(hotelDTO);
        return ResponseEntity.status(201).body(createdHotel);
    }

    @PutMapping("/u/{id}")
    public ResponseEntity<?> updateHotel(@PathVariable Long id, @Valid @RequestBody HotelDTO hotelDTO, @RequestHeader("Authorization") String authHeader) {
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        String role = credentials[0];
        String username = credentials[1];

        ResponseEntity<?> responseEntity = userServiceClient.getUserByUsername(authHeader, username);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        List<Integer> hotelIds = (List<Integer>) responseBody.get("hotelIds");
        if(hotelIds == null){
            return ResponseEntity.status(403).body("You are not authorized to update a hotel with HotelId " + id);
        }
        if (!hotelIds.contains(hotelDTO.getHotelId().intValue()) && !(hotelIds.get(0) == 0)) {
            return ResponseEntity.status(403).body("You are not authorized to update a hotel with HotelId " + id);
        }

        HotelDTO updatedHotel = hotelService.updateHotel(id, hotelDTO);
        return updatedHotel != null ? ResponseEntity.ok(updatedHotel) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/d/{id}")
    public ResponseEntity<?> deleteHotel(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        String role = credentials[0];
        String username = credentials[1];

        ResponseEntity<?> responseEntity = userServiceClient.getUserByUsername(authHeader, username);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        List<Integer> hotelIds = (List<Integer>) responseBody.get("hotelIds");
        if(hotelIds == null){
            return ResponseEntity.status(403).body("You are not authorized to delete a hotel with HotelId " + id);
        }
        if (!hotelIds.contains(id.intValue()) && !(hotelIds.get(0) == 0)) {
            return ResponseEntity.status(403).body("You are not authorized to delete a hotel with HotelId " + id);
        }

        if (hotelService.deleteHotel(id)) {
            return ResponseEntity.status(200).body("Hotel Deleted Successfully");
        } else {
            return ResponseEntity.status(404).body("Hotel Id Not Found");
        }
    }
}
