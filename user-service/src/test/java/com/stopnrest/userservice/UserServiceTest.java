package com.stopnrest.userservice;

import com.stopnrest.userservice.dto.CreditCardDTO;
import com.stopnrest.userservice.dto.StayInfoDTO;
import com.stopnrest.userservice.dto.UserDTO;
import com.stopnrest.userservice.model.CreditCard;
import com.stopnrest.userservice.model.StayInfo;
import com.stopnrest.userservice.model.User;
import com.stopnrest.userservice.repository.UserRepository;
import com.stopnrest.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDTO = new UserDTO();
        userDTO.setUserName("john_doe");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setMobileNumber("1234567890");
        userDTO.setRole("USER");

        user = new User();
        user.setUserName("john_doe");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setMobileNumber("1234567890");
        user.setRole("USER");
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());
        when(modelMapper.map(any(UserDTO.class), eq(User.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<String> response = userService.createUser(userDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("User created successfully", response.getBody());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));
        when(modelMapper.map(any(UserDTO.class), eq(User.class))).thenReturn(user);

        ResponseEntity<String> response = userService.updateUser("john_doe", userDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User updated successfully", response.getBody());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());

        ResponseEntity<String> response = userService.updateUser("john_doe", userDTO);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User with username john_doe not found", response.getBody());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));

        ResponseEntity<String> response = userService.deleteUser("john_doe");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User deleted successfully", response.getBody());
        verify(userRepository).delete(any(User.class));
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());

        ResponseEntity<String> response = userService.deleteUser("john_doe");

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User with username john_doe not found", response.getBody());
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void testGetUserByUsername_UserNotFound() {
        when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());

        ResponseEntity<String> response = (ResponseEntity<String>) userService.getUserByUsername("john_doe");

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User with username john_doe does not exist", response.getBody());
    }

    @Test
    void testUpdateUserStatus_Success() {
        StayInfo stayInfo = new StayInfo();
        stayInfo.setReservationID(123L);

        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));

        ResponseEntity<String> response = userService.updateUserStatus(123L, "john_doe");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Stay information updated successfully", response.getBody());
        verify(userRepository).save(any(User.class));
    }

}
