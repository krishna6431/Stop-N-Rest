package com.stopnrest.bookingservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "hotel-service", url = "http://localhost:8084")
public interface HotelServiceClient {
    @GetMapping("/api/v1/rooms/hotel/{hotelId}/{roomId}")
    ResponseEntity<String> getRoomAvailableByHotel(@RequestHeader("Authorization")String authHeader, @PathVariable Long hotelId, @PathVariable Long roomId);
    @PutMapping("/api/v1/rooms/update/room/{roomId}")
    ResponseEntity<String> updateRoomStatus(@RequestHeader("Authorization")String authHeader, @PathVariable("roomId")Long roomId);

}
