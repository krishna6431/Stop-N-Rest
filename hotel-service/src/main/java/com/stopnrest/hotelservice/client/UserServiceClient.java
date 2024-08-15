package com.stopnrest.hotelservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "http://localhost:8084")
public interface UserServiceClient {
    @GetMapping("/api/v1/users/{username}")
    ResponseEntity<?> getUserByUsername(@RequestHeader("Authorization") String authHeader, @PathVariable String username);
}
