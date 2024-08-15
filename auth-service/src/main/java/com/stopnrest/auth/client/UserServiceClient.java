package com.stopnrest.auth.client;

import com.stopnrest.auth.dto.AdminRegistrationDTO;
import com.stopnrest.auth.dto.UserRegistrationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "http://localhost:8084")
public interface UserServiceClient {
    @PostMapping("/api/v1/users")
    ResponseEntity<String> createUser(@RequestBody UserRegistrationDTO user);
    @PostMapping("/api/v1/users")
    ResponseEntity<String> createUser(@RequestBody AdminRegistrationDTO user);
    @DeleteMapping("/api/v1/users/{username}")
    ResponseEntity<String> deleteUser(@PathVariable("username")String username, @RequestHeader("Authorization")String authHeader);
}
