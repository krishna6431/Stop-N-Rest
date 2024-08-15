package com.stopnrest.auth.controller;

import com.stopnrest.auth.client.UserServiceClient;
import com.stopnrest.auth.dto.UserLoginDTO;
import com.stopnrest.auth.dto.UserRegistrationDTO;
import com.stopnrest.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth/users")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserServiceClient userServiceClient;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        String response = authService.registerUser(userRegistrationDTO);
        if (userServiceClient.createUser(userRegistrationDTO).getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        return authService.loginUser(userLoginDTO);
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username,@RequestHeader("Authorization")String authHeader) {
        ResponseEntity<?> userServiceResponse = userServiceClient.deleteUser(username,authHeader);
        if(userServiceResponse.getStatusCode().is2xxSuccessful()){
            authService.deleteUser(username);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("SomeThing Went Wrong While Deletion");
    }

}
