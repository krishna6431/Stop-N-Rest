package com.stopnrest.hotelservice;

import com.stopnrest.hotelservice.controller.HotelController;
import com.stopnrest.hotelservice.dto.HotelDTO;
import com.stopnrest.hotelservice.service.HotelService;
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

public class HotelControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private HotelController hotelController;

    @Mock
    private HotelService hotelService;

    @Mock
    private JwtClaims jwtClaims;

    @Mock
    private UserServiceClient userServiceClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(hotelController).build();
    }

    @Test
    public void testGetAllHotels() throws Exception {
        HotelDTO hotelDTO = new HotelDTO();
        List<HotelDTO> hotels = Arrays.asList(hotelDTO);
        when(hotelService.getAllHotels()).thenReturn(hotels);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hotels"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists());

        verify(hotelService, times(1)).getAllHotels();
    }

    @Test
    public void testGetHotelById() throws Exception {
        HotelDTO hotelDTO = new HotelDTO();
        when(hotelService.getHotelById(anyLong())).thenReturn(Optional.of(hotelDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hotels/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());

        verify(hotelService, times(1)).getHotelById(anyLong());
    }

    @Test
    public void testGetHotelByIdNotFound() throws Exception {
        when(hotelService.getHotelById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hotels/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(hotelService, times(1)).getHotelById(anyLong());
    }
}