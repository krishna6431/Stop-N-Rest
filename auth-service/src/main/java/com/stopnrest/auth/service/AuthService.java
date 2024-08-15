package com.stopnrest.auth.service;

import com.stopnrest.auth.dto.UserLoginDTO;
import com.stopnrest.auth.dto.UserRegistrationDTO;
import com.stopnrest.auth.model.User;
import com.stopnrest.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String registerUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.existsByUserName(userRegistrationDTO.getUserName())) {
            return "Username already exists. Please choose a different username.";
        }

        User user = new User();
        user.setUserName(userRegistrationDTO.getUserName());
        String hashedPassword = passwordEncoder.encode(userRegistrationDTO.getPassword());
        user.setPassword(hashedPassword);
        user.setEmail(userRegistrationDTO.getEmail());
        user.setMobileNumber(userRegistrationDTO.getMobileNumber());
        userRepository.save(user);
        return "User registered successfully.";
    }

    public ResponseEntity<String> loginUser(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByUserName(userLoginDTO.getUserName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with the provided username not found.");
        }

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid password.");
        }
        String token = "Bearer " + jwtService.generateToken(user.getUserName(), "PUBLIC");
        return ResponseEntity.status(HttpStatus.OK)
                .body("Login successful. Your JWT token: " + token);
    }

    public boolean deleteUser(String username) {
        User user = userRepository.findByUserName(username);
        if (user != null) {
            userRepository.delete(user);
            return true;
        } else {
            return false;
        }
    }

}
