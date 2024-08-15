package com.stopnrest.auth.service;

import com.stopnrest.auth.dto.AdminDTO;
import com.stopnrest.auth.dto.AdminLoginDTO;
import com.stopnrest.auth.dto.AdminRegistrationDTO;
import com.stopnrest.auth.model.Admin;
import com.stopnrest.auth.model.User;
import com.stopnrest.auth.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public ResponseEntity<?> registerAdmin(AdminRegistrationDTO adminRegistrationDTO) {
        if (adminRepository.existsByUserName(adminRegistrationDTO.getUserName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists. Please choose a different username.");
        }

        Admin admin = new Admin();
        admin.setUserName(adminRegistrationDTO.getUserName());
        admin.setPassword(passwordEncoder.encode(adminRegistrationDTO.getPassword()));
        admin.setEmail(adminRegistrationDTO.getEmail());
        admin.setMobileNumber(adminRegistrationDTO.getMobileNumber());
        admin.setHotelIds(adminRegistrationDTO.getHotelIds());
        admin.setRole(adminRegistrationDTO.getRole());
        adminRepository.save(admin);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Admin registered successfully.");
    }

    public ResponseEntity<?> loginAdmin(AdminLoginDTO adminLoginDTO) {
        Admin admin = adminRepository.findByUserName(adminLoginDTO.getUserName());
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Admin with the provided username not found.");
        }

        if (!passwordEncoder.matches(adminLoginDTO.getPassword(), admin.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid password.");
        }
        String token = "Bearer " + jwtService.generateToken(admin.getUserName(), "ADMIN");
        return ResponseEntity.status(HttpStatus.OK)
                .body("Login successful. Your JWT token: " + token);
    }

    public boolean deleteUser(String username) {
        Admin user = adminRepository.findByUserName(username);
        if (user != null) {
            adminRepository.delete(user);
            return true;
        } else {
            return false;
        }
    }

}
