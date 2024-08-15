package com.stopnrest.bookingservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "http://localhost:8084")
public interface UserServiceClient {
    @GetMapping("/api/v1/users/{username}")
    ResponseEntity<?> getUserByUsername(@RequestHeader("Authorization") String authHeader, @PathVariable("username") String username);

    @PutMapping("/api/v1/users/update/user/{userName}/{bookingId}")
    ResponseEntity<String> updateUserStatus(@RequestHeader("Authorization") String authHeader, @PathVariable("userName")String userName, @PathVariable("bookingId") Long bookingID);
}
