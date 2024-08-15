package com.stopnrest.hotelservice.service;

import com.stopnrest.hotelservice.dto.RoomDTO;
import com.stopnrest.hotelservice.model.Room;
import com.stopnrest.hotelservice.repository.RoomRepository;
import com.stopnrest.hotelservice.repository.HotelRepository;
import com.stopnrest.hotelservice.model.Hotel;
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
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(room -> modelMapper.map(room, RoomDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<RoomDTO> getRoomById(Long id) {
        return roomRepository.findById(id)
                .map(room -> modelMapper.map(room, RoomDTO.class));
    }

    public List<RoomDTO> getRoomsByHotelId(Long hotelId) {
        return roomRepository.findByHotel_HotelId(hotelId).stream()
                .map(room -> modelMapper.map(room, RoomDTO.class))
                .collect(Collectors.toList());
    }

    public RoomDTO createRoom(RoomDTO roomDTO) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(roomDTO.getHotelId());
        if (!hotelOptional.isPresent()) {
            throw new RuntimeException("Hotel not found");
        }

        Room room = modelMapper.map(roomDTO, Room.class);
        room.setHotel(hotelOptional.get());

        Room savedRoom = roomRepository.save(room);
        return modelMapper.map(savedRoom, RoomDTO.class);
    }

    public RoomDTO updateRoom(Long id, RoomDTO roomDTO) {
        if (roomRepository.existsById(id)) {
            Optional<Hotel> hotelOptional = hotelRepository.findById(roomDTO.getHotelId());
            if (!hotelOptional.isPresent()) {
                throw new RuntimeException("Hotel not found");
            }

            Room room = modelMapper.map(roomDTO, Room.class);
            room.setRoomId(id);
            room.setHotel(hotelOptional.get()); // Set the hotel entity

            Room updatedRoom = roomRepository.save(room);
            return modelMapper.map(updatedRoom, RoomDTO.class);
        }
        return null;
    }

    public boolean deleteRoom(Long id) {
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public ResponseEntity<String> getRoomAvailableByHotel(Long hotelId, Long roomId) {
        List<RoomDTO> rooms = getRoomsByHotelId(hotelId);
        if (rooms.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel with ID " + hotelId + " not found.");
        }
        Optional<RoomDTO> room = rooms.stream()
                .filter(r -> r.getRoomId().equals(roomId))
                .findFirst();

        if (room.isPresent()) {
            if (room.get().isRoomAvailable()) {
                return ResponseEntity.ok("Room is available.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room with ID " + roomId + " is not available.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room with ID " + roomId + " not found.");
        }
    }

    public ResponseEntity<String> updateRoomStatus(Long roomId) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            boolean newStatus = !room.getRoomAvailable();
            room.setRoomAvailable(newStatus);
            roomRepository.save(room);
            return ResponseEntity.ok("Room status updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room with ID " + roomId + " not found.");
        }
    }
}
