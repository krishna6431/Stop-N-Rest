package com.stopnrest.hotelservice;

import com.stopnrest.hotelservice.dto.RoomTypeDTO;
import com.stopnrest.hotelservice.model.RoomType;
import com.stopnrest.hotelservice.repository.RoomRepository;
import com.stopnrest.hotelservice.repository.RoomTypeRepository;
import com.stopnrest.hotelservice.repository.HotelRepository;
import com.stopnrest.hotelservice.service.RoomTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoomTypeServiceTest {

    @InjectMocks
    private RoomTypeService roomTypeService;

    @Mock
    private RoomTypeRepository roomTypeRepository;

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
    public void testGetAllRoomTypes() {
        RoomType roomType = new RoomType();
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        when(roomTypeRepository.findAll()).thenReturn(Arrays.asList(roomType));
        when(modelMapper.map(roomType, RoomTypeDTO.class)).thenReturn(roomTypeDTO);

        List<RoomTypeDTO> result = roomTypeService.getAllRoomTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roomTypeRepository, times(1)).findAll();
    }

    @Test
    public void testGetRoomTypeById() {
        RoomType roomType = new RoomType();
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        when(roomTypeRepository.findById(1L)).thenReturn(Optional.of(roomType));
        when(modelMapper.map(roomType, RoomTypeDTO.class)).thenReturn(roomTypeDTO);

        ResponseEntity<RoomTypeDTO> result = roomTypeService.getRoomTypeById(1L);

        verify(roomTypeRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetRoomTypesByHotelId() {
        RoomType roomType = new RoomType();
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        List<Long> roomTypeIds = Arrays.asList(1L);
        when(roomRepository.findRoomTypeIdsByHotelId(1L)).thenReturn(roomTypeIds);
        when(roomTypeRepository.findRoomTypesByRoomTypeIds(roomTypeIds)).thenReturn(Arrays.asList(roomType));
        when(modelMapper.map(roomType, RoomTypeDTO.class)).thenReturn(roomTypeDTO);

        List<RoomTypeDTO> result = roomTypeService.getRoomTypesByHotelId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roomRepository, times(1)).findRoomTypeIdsByHotelId(1L);
        verify(roomTypeRepository, times(1)).findRoomTypesByRoomTypeIds(roomTypeIds);
    }

    @Test
    public void testCreateRoomType() {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        RoomType roomType = new RoomType();
        when(modelMapper.map(roomTypeDTO, RoomType.class)).thenReturn(roomType);
        when(roomTypeRepository.save(roomType)).thenReturn(roomType);
        when(modelMapper.map(roomType, RoomTypeDTO.class)).thenReturn(roomTypeDTO);

        RoomTypeDTO result = roomTypeService.createRoomType(roomTypeDTO).getBody();

        assertNotNull(result);
        verify(roomTypeRepository, times(1)).save(roomType);
    }

    @Test
    public void testUpdateRoomType() {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        RoomType roomType = new RoomType();
        when(roomTypeRepository.existsById(1L)).thenReturn(true);
        when(modelMapper.map(roomTypeDTO, RoomType.class)).thenReturn(roomType);
        when(roomTypeRepository.save(roomType)).thenReturn(roomType);
        when(modelMapper.map(roomType, RoomTypeDTO.class)).thenReturn(roomTypeDTO);

        RoomTypeDTO result = roomTypeService.updateRoomType(1L, roomTypeDTO).getBody();

        assertNotNull(result);
        assertEquals(roomTypeDTO, result);
        verify(roomTypeRepository, times(1)).save(roomType);
    }


    @Test
    public void testDeleteRoomType() {
        when(roomTypeRepository.existsById(1L)).thenReturn(true);

        boolean result = roomTypeService.deleteRoomType(1L).hasBody();

        assertTrue(result);
        verify(roomRepository, times(1)).deleteByRoomType_RoomTypeId(1L);
        verify(roomTypeRepository, times(1)).deleteById(1L);
    }

}

