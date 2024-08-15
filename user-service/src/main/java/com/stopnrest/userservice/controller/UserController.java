package com.stopnrest.userservice.controller;

import com.stopnrest.userservice.claims.JwtClaims;
import com.stopnrest.userservice.dto.CreditCardDTO;
import com.stopnrest.userservice.dto.StayInfoDTO;
import com.stopnrest.userservice.dto.UserDTO;
import com.stopnrest.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtClaims jwtClaims;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PutMapping("/{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username, @RequestBody UserDTO userDTO, @RequestHeader("Authorization") String authHeader) {
        authHeader = authHeader.replace("Bearer ", "");
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        if (!credentials[1].equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to update user");
        }
        return userService.updateUser(username, userDTO);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username, @RequestHeader("Authorization") String authHeader) {
        authHeader = authHeader.replace("Bearer ", "");
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        if (!credentials[1].equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to delete user");
        }
        return userService.deleteUser(username);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@RequestHeader("Authorization") String authHeader, @PathVariable String username) {
        authHeader = authHeader.replace("Bearer ", "");
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        if (!credentials[1].equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to view user details");
        }
        return userService.getUserByUsername(username);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/{username}/credit-cards")
    public ResponseEntity<?> addCreditCard(@PathVariable String username, @RequestBody CreditCardDTO creditCardDTO, @RequestHeader("Authorization") String authHeader) {
        authHeader = authHeader.replace("Bearer ", "");
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        if (!credentials[1].equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to add credit card");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addCreditCard(username, creditCardDTO));
    }

    @DeleteMapping("/{username}/credit-cards")
    public ResponseEntity<String> removeCreditCard(@PathVariable String username, @RequestBody CreditCardDTO creditCardRemovalDTO, @RequestHeader("Authorization") String authHeader) {
        authHeader = authHeader.replace("Bearer ", "");
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        if (!credentials[1].equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to remove credit card");
        }
        return userService.removeCreditCard(username, creditCardRemovalDTO);
    }

    @GetMapping("/{username}/credit-cards")
    public ResponseEntity<?> getAllCreditCards(@PathVariable String username, @RequestHeader("Authorization") String authHeader) {
        authHeader = authHeader.replace("Bearer ", "");
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        if (!credentials[1].equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to view credit cards");
        }
        return userService.getAllCreditCards(username);
    }

    @GetMapping("/{username}/stay-info")
    public ResponseEntity<?> getAllStayInfo(@PathVariable String username, @RequestHeader("Authorization") String authHeader) {
        authHeader = authHeader.replace("Bearer ", "");
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        if (!credentials[1].equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to view stay info");
        }
        return userService.getAllStayInfo(username);
    }

    @PutMapping("/update/user/{userName}/{bookingId}")
    public ResponseEntity<?> updateUserStatus(@RequestHeader("Authorization") String authHeader, @PathVariable("userName") String userName, @PathVariable("bookingId") Long bookingId) {
        authHeader = authHeader.replace("Bearer ", "");
        String[] credentials = jwtClaims.extractCredentials(authHeader);
        if (!credentials[1].equals(userName)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to update user status");
        }
        return userService.updateUserStatus(bookingId, userName);
    }
}
