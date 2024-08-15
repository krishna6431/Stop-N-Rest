package com.stopnrest.userservice;

import com.stopnrest.userservice.claims.JwtClaims;
import com.stopnrest.userservice.controller.UserController;
import com.stopnrest.userservice.dto.CreditCardDTO;
import com.stopnrest.userservice.dto.UserDTO;
import com.stopnrest.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtClaims jwtClaims;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateUser_Authorized() {
        String username = "john_doe";
        UserDTO userDTO = new UserDTO();
        String token = "valid_token";
        String[] credentials = {"john_doe@example.com", username};

        when(jwtClaims.extractCredentials(token)).thenReturn(credentials);
        when(userService.updateUser(username, userDTO)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<String> responseEntity = userController.updateUser(username, userDTO, "Bearer " + token);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).updateUser(username, userDTO);
    }

    @Test
    void testUpdateUser_Unauthorized() {
        String username = "john_doe";
        UserDTO userDTO = new UserDTO();
        String token = "valid_token";
        String[] credentials = {"john_doe@example.com", "another_user"};

        when(jwtClaims.extractCredentials(token)).thenReturn(credentials);

        ResponseEntity<String> responseEntity = userController.updateUser(username, userDTO, "Bearer " + token);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        verify(userService, never()).updateUser(anyString(), any(UserDTO.class));
    }

    @Test
    void testDeleteUser_Authorized() {
        String username = "john_doe";
        String token = "valid_token";
        String[] credentials = {"john_doe@example.com", username};

        when(jwtClaims.extractCredentials(token)).thenReturn(credentials);
        when(userService.deleteUser(username)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<String> responseEntity = userController.deleteUser(username, "Bearer " + token);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).deleteUser(username);
    }

    @Test
    void testDeleteUser_Unauthorized() {
        String username = "john_doe";
        String token = "valid_token";
        String[] credentials = {"john_doe@example.com", "another_user"};

        when(jwtClaims.extractCredentials(token)).thenReturn(credentials);

        ResponseEntity<String> responseEntity = userController.deleteUser(username, "Bearer " + token);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        verify(userService, never()).deleteUser(anyString());
    }

    @Test
    void testGetUserByUsername_Authorized() {
        String username = "john_doe";
        String token = "valid_token";
        String[] credentials = {"john_doe@example.com", username};

        when(jwtClaims.extractCredentials(token)).thenReturn(credentials);
        when(userService.getUserByUsername(username)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> responseEntity = userController.getUserByUsername("Bearer " + token, username);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).getUserByUsername(username);
    }

    @Test
    void testGetUserByUsername_Unauthorized() {
        String username = "john_doe";
        String token = "valid_token";
        String[] credentials = {"john_doe@example.com", "another_user"};

        when(jwtClaims.extractCredentials(token)).thenReturn(credentials);

        ResponseEntity<?> responseEntity = userController.getUserByUsername("Bearer " + token, username);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        verify(userService, never()).getUserByUsername(anyString());
    }

    @Test
    void testGetAllUsers() {
        List<UserDTO> users = Arrays.asList(new UserDTO(), new UserDTO());

        when(userService.getAllUsers()).thenReturn(new ResponseEntity<>(users, HttpStatus.OK));

        ResponseEntity<List<UserDTO>> responseEntity = userController.getAllUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }


    @Test
    void testRemoveCreditCard_Authorized() {
        String username = "john_doe";
        CreditCardDTO creditCardDTO = new CreditCardDTO();
        String token = "valid_token";
        String[] credentials = {"john_doe@example.com", username};

        when(jwtClaims.extractCredentials(token)).thenReturn(credentials);
        when(userService.removeCreditCard(username, creditCardDTO)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<String> responseEntity = userController.removeCreditCard(username, creditCardDTO, "Bearer " + token);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).removeCreditCard(username, creditCardDTO);
    }

    @Test
    void testGetAllCreditCards_Unauthorized() {
        String username = "john_doe";
        String token = "valid_token";
        String[] credentials = {"john_doe@example.com", "another_user"};

        when(jwtClaims.extractCredentials(token)).thenReturn(credentials);

        ResponseEntity<?> responseEntity = userController.getAllCreditCards(username, "Bearer " + token);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        verify(userService, never()).getAllCreditCards(anyString());
    }

    @Test
    void testGetAllStayInfo_Unauthorized() {
        String username = "john_doe";
        String token = "valid_token";
        String[] credentials = {"john_doe@example.com", "another_user"};

        when(jwtClaims.extractCredentials(token)).thenReturn(credentials);

        ResponseEntity<?> responseEntity = userController.getAllStayInfo(username, "Bearer " + token);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        verify(userService, never()).getAllStayInfo(anyString());
    }

}

