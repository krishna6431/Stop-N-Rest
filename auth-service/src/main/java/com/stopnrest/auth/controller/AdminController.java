package com.stopnrest.auth.controller;

import com.stopnrest.auth.client.UserServiceClient;
import com.stopnrest.auth.dto.AdminDTO;
import com.stopnrest.auth.dto.AdminLoginDTO;
import com.stopnrest.auth.dto.AdminRegistrationDTO;
import com.stopnrest.auth.service.AdminService;
import com.stopnrest.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminRegistrationDTO adminRegistrationDTO) {
        if (userServiceClient.createUser(adminRegistrationDTO).getStatusCode().is2xxSuccessful()) {
            return adminService.registerAdmin(adminRegistrationDTO);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody AdminLoginDTO adminLoginDTO) {
        return adminService.loginAdmin(adminLoginDTO);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            boolean isValid = jwtService.validateToken(token);
            return isValid ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username,@RequestHeader("Authorization")String authHeader) {
        ResponseEntity<?> userServiceResponse = userServiceClient.deleteUser(username,authHeader);
        if(userServiceResponse.getStatusCode().is2xxSuccessful()){
            adminService.deleteUser(username);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("SomeThing Went Wrong While Deletion");
    }
}
