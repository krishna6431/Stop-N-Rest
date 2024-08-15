package com.stopnrest.hotelservice;

import com.stopnrest.hotelservice.dto.HotelDTO;
import com.stopnrest.hotelservice.dto.RoomDTO;
import com.stopnrest.hotelservice.model.Hotel;
import com.stopnrest.hotelservice.model.Room;
import com.stopnrest.hotelservice.repository.HotelRepository;
import com.stopnrest.hotelservice.repository.RoomRepository;
import com.stopnrest.hotelservice.repository.RoomTypeRepository;
import com.stopnrest.hotelservice.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class HotelServiceTest {

    @InjectMocks
    private HotelService hotelService;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomTypeRepository roomTypeRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateHotel() {
        HotelDTO hotelDTO = new HotelDTO();
        Hotel hotel = new Hotel();
        Hotel savedHotel = new Hotel();
        when(modelMapper.map(hotelDTO, Hotel.class)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(savedHotel);
        when(modelMapper.map(savedHotel, HotelDTO.class)).thenReturn(hotelDTO);

        HotelDTO result = hotelService.createHotel(hotelDTO);

        assertNotNull(result);
        verify(hotelRepository, times(1)).save(hotel);
    }

    @Test
    public void testUpdateHotel() {
        HotelDTO hotelDTO = new HotelDTO();
        Hotel hotel = new Hotel();
        hotel.setHotelId(1L);
        when(hotelRepository.existsById(1L)).thenReturn(true);
        when(modelMapper.map(hotelDTO, Hotel.class)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(modelMapper.map(hotel, HotelDTO.class)).thenReturn(hotelDTO);

        HotelDTO result = hotelService.updateHotel(1L, hotelDTO);

        assertNotNull(result);
        verify(hotelRepository, times(1)).save(hotel);
    }

    @Test
    public void testUpdateHotelNotFound() {
        HotelDTO hotelDTO = new HotelDTO();
        when(hotelRepository.existsById(1L)).thenReturn(false);

        HotelDTO result = hotelService.updateHotel(1L, hotelDTO);

        assertNull(result);
        verify(hotelRepository, times(0)).save(any(Hotel.class));
    }

    @Test
    public void testDeleteHotel() {
        when(hotelRepository.existsById(1L)).thenReturn(true);

        boolean result = hotelService.deleteHotel(1L);

        assertTrue(result);
        verify(hotelRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteHotelNotFound() {
        when(hotelRepository.existsById(1L)).thenReturn(false);

        boolean result = hotelService.deleteHotel(1L);

        assertFalse(result);
        verify(hotelRepository, times(0)).deleteById(anyLong());
    }
}

