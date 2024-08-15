package com.stopnrest.hotelservice.service;

import com.stopnrest.hotelservice.dto.RoomTypeDTO;
import com.stopnrest.hotelservice.model.RoomType;
import com.stopnrest.hotelservice.repository.RoomRepository;
import com.stopnrest.hotelservice.repository.RoomTypeRepository;
import com.stopnrest.hotelservice.repository.HotelRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomTypeService {

    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<RoomTypeDTO> getAllRoomTypes() {
        return roomTypeRepository.findAll().stream()
                .map(roomType -> modelMapper.map(roomType, RoomTypeDTO.class))
                .collect(Collectors.toList());
    }

    public ResponseEntity<RoomTypeDTO> getRoomTypeById(Long id) {
        Optional<RoomTypeDTO> roomTypeDTO = roomTypeRepository.findById(id)
                .map(roomType -> modelMapper.map(roomType, RoomTypeDTO.class));
        return roomTypeDTO
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RoomTypeDTO()));
    }

    public List<RoomTypeDTO> getRoomTypesByHotelId(Long hotelId) {
        List<Long> roomTypeIds = roomRepository.findRoomTypeIdsByHotelId(hotelId);
        List<RoomType> roomTypes = roomTypeRepository.findRoomTypesByRoomTypeIds(roomTypeIds);
        return roomTypes.stream()
                .map(roomType -> modelMapper.map(roomType, RoomTypeDTO.class))
                .collect(Collectors.toList());
    }

    public ResponseEntity<RoomTypeDTO> createRoomType(RoomTypeDTO roomTypeDTO) {
        RoomType roomType = modelMapper.map(roomTypeDTO, RoomType.class);
        RoomType savedRoomType = roomTypeRepository.save(roomType);
        RoomTypeDTO savedRoomTypeDTO = modelMapper.map(savedRoomType, RoomTypeDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoomTypeDTO);
    }

    public ResponseEntity<RoomTypeDTO> updateRoomType(Long id, RoomTypeDTO roomTypeDTO) {
        if (roomTypeRepository.existsById(id)) {
            RoomType roomType = modelMapper.map(roomTypeDTO, RoomType.class);
            roomType.setRoomTypeId(id);
            RoomType updatedRoomType = roomTypeRepository.save(roomType);
            RoomTypeDTO updatedRoomTypeDTO = modelMapper.map(updatedRoomType, RoomTypeDTO.class);
            return ResponseEntity.ok(updatedRoomTypeDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RoomTypeDTO());

        }
    }

    @Transactional
    public ResponseEntity<String> deleteRoomType(Long id) {
        if (roomTypeRepository.existsById(id)) {
            roomRepository.deleteByRoomType_RoomTypeId(id);
            roomTypeRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Room Type with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room Type with ID " + id + " not found.");
        }
    }
}
