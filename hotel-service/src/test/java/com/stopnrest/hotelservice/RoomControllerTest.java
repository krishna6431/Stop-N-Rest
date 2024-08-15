package com.stopnrest.hotelservice;

import com.stopnrest.hotelservice.controller.RoomController;
import com.stopnrest.hotelservice.dto.RoomDTO;
import com.stopnrest.hotelservice.service.RoomService;
import com.stopnrest.hotelservice.claims.JwtClaims;
import com.stopnrest.hotelservice.client.UserServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class RoomControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private RoomController roomController;

    @Mock
    private RoomService roomService;

    @Mock
    private JwtClaims jwtClaims;

    @Mock
    private UserServiceClient userServiceClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    public void testGetAllRooms() throws Exception {
        RoomDTO roomDTO = new RoomDTO();
        List<RoomDTO> rooms = Arrays.asList(roomDTO);
        when(roomService.getAllRooms()).thenReturn(rooms);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rooms"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists());

        verify(roomService, times(1)).getAllRooms();
    }

    @Test
    public void testGetRoomById() throws Exception {
        RoomDTO roomDTO = new RoomDTO();
        when(roomService.getRoomById(anyLong())).thenReturn(Optional.of(roomDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rooms/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());

        verify(roomService, times(1)).getRoomById(anyLong());
    }

    @Test
    public void testGetRoomByIdNotFound() throws Exception {
        when(roomService.getRoomById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rooms/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(roomService, times(1)).getRoomById(anyLong());
    }

    @Test
    public void testGetRoomsByHotelId() throws Exception {
        RoomDTO roomDTO = new RoomDTO();
        List<RoomDTO> rooms = Arrays.asList(roomDTO);
        when(roomService.getRoomsByHotelId(anyLong())).thenReturn(rooms);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rooms/hotel/{hotelId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists());

        verify(roomService, times(1)).getRoomsByHotelId(anyLong());
    }
}