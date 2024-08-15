package com.stopnrest.hotelservice;

import com.stopnrest.hotelservice.dto.RoomDTO;
import com.stopnrest.hotelservice.model.Room;
import com.stopnrest.hotelservice.model.Hotel;
import com.stopnrest.hotelservice.repository.RoomRepository;
import com.stopnrest.hotelservice.repository.HotelRepository;
import com.stopnrest.hotelservice.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRooms() {
        Room room = new Room();
        RoomDTO roomDTO = new RoomDTO();
        when(roomRepository.findAll()).thenReturn(Arrays.asList(room));
        when(modelMapper.map(room, RoomDTO.class)).thenReturn(roomDTO);

        List<RoomDTO> result = roomService.getAllRooms();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    public void testGetRoomById() {
        Room room = new Room();
        RoomDTO roomDTO = new RoomDTO();
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(modelMapper.map(room, RoomDTO.class)).thenReturn(roomDTO);

        Optional<RoomDTO> result = roomService.getRoomById(1L);

        assertTrue(result.isPresent());
        assertEquals(roomDTO, result.get());
        verify(roomRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetRoomsByHotelId() {
        Room room = new Room();
        RoomDTO roomDTO = new RoomDTO();
        when(roomRepository.findByHotel_HotelId(1L)).thenReturn(Arrays.asList(room));
        when(modelMapper.map(room, RoomDTO.class)).thenReturn(roomDTO);

        List<RoomDTO> result = roomService.getRoomsByHotelId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roomRepository, times(1)).findByHotel_HotelId(1L);
    }

    @Test
    public void testCreateRoom() {
        RoomDTO roomDTO = new RoomDTO();
        Room room = new Room();
        Hotel hotel = new Hotel();
        when(modelMapper.map(roomDTO, Room.class)).thenReturn(room);
        when(hotelRepository.findById(roomDTO.getHotelId())).thenReturn(Optional.of(hotel));
        when(roomRepository.save(room)).thenReturn(room);
        when(modelMapper.map(room, RoomDTO.class)).thenReturn(roomDTO);

        RoomDTO result = roomService.createRoom(roomDTO);

        assertNotNull(result);
        verify(hotelRepository, times(1)).findById(roomDTO.getHotelId());
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    public void testCreateRoomHotelNotFound() {
        RoomDTO roomDTO = new RoomDTO();
        when(hotelRepository.findById(roomDTO.getHotelId())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            roomService.createRoom(roomDTO);
        });

        assertEquals("Hotel not found", thrown.getMessage());
        verify(roomRepository, times(0)).save(any(Room.class));
    }

    @Test
    public void testUpdateRoom() {
        RoomDTO roomDTO = new RoomDTO();
        Room room = new Room();
        Hotel hotel = new Hotel();
        when(roomRepository.existsById(1L)).thenReturn(true);
        when(hotelRepository.findById(roomDTO.getHotelId())).thenReturn(Optional.of(hotel));
        when(modelMapper.map(roomDTO, Room.class)).thenReturn(room);
        when(roomRepository.save(room)).thenReturn(room);
        when(modelMapper.map(room, RoomDTO.class)).thenReturn(roomDTO);

        RoomDTO result = roomService.updateRoom(1L, roomDTO);

        assertNotNull(result);
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    public void testUpdateRoomHotelNotFound() {
        RoomDTO roomDTO = new RoomDTO();
        when(roomRepository.existsById(1L)).thenReturn(true);
        when(hotelRepository.findById(roomDTO.getHotelId())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            roomService.updateRoom(1L, roomDTO);
        });

        assertEquals("Hotel not found", thrown.getMessage());
        verify(roomRepository, times(0)).save(any(Room.class));
    }

    @Test
    public void testUpdateRoomNotFound() {
        RoomDTO roomDTO = new RoomDTO();
        when(roomRepository.existsById(1L)).thenReturn(false);

        RoomDTO result = roomService.updateRoom(1L, roomDTO);

        assertNull(result);
        verify(roomRepository, times(0)).save(any(Room.class));
    }

    @Test
    public void testDeleteRoom() {
        when(roomRepository.existsById(1L)).thenReturn(true);

        boolean result = roomService.deleteRoom(1L);

        assertTrue(result);
        verify(roomRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteRoomNotFound() {
        when(roomRepository.existsById(1L)).thenReturn(false);

        boolean result = roomService.deleteRoom(1L);

        assertFalse(result);
        verify(roomRepository, times(0)).deleteById(anyLong());
    }

}
