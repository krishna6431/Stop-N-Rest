package com.stopnrest.hotelservice.service;

import com.stopnrest.hotelservice.dto.HotelDTO;
import com.stopnrest.hotelservice.dto.RoomDTO;
import com.stopnrest.hotelservice.model.Hotel;
import com.stopnrest.hotelservice.repository.HotelRepository;
import com.stopnrest.hotelservice.repository.RoomRepository;
import com.stopnrest.hotelservice.repository.RoomTypeRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<HotelDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotel -> {
                    HotelDTO hotelDTO = modelMapper.map(hotel, HotelDTO.class);
                    // Populate rooms for each hotel
                    hotelDTO.setHotelRooms(
                            hotel.getHotelRooms().stream()
                                    .map(room -> modelMapper.map(room, RoomDTO.class))
                                    .collect(Collectors.toList())
                    );
                    return hotelDTO;
                })
                .collect(Collectors.toList());
    }

    public Optional<HotelDTO> getHotelById(Long id) {
        return hotelRepository.findById(id).map(hotel -> {
            HotelDTO hotelDTO = modelMapper.map(hotel, HotelDTO.class);
            // Populate rooms for the hotel
            hotelDTO.setHotelRooms(
                    hotel.getHotelRooms().stream()
                            .map(room -> modelMapper.map(room, RoomDTO.class))
                            .collect(Collectors.toList())
            );
            return hotelDTO;
        });
    }

    public HotelDTO createHotel(HotelDTO hotelDTO) {
        Hotel hotel = modelMapper.map(hotelDTO, Hotel.class);
        Hotel savedHotel = hotelRepository.save(hotel);
        return modelMapper.map(savedHotel, HotelDTO.class);
    }

    public HotelDTO updateHotel(Long id, HotelDTO hotelDTO) {
        if (hotelRepository.existsById(id)) {
            Hotel hotel = modelMapper.map(hotelDTO, Hotel.class);
            hotel.setHotelId(id);
            hotel.setHotelRooms(hotel.getHotelRooms());
            Hotel updatedHotel = hotelRepository.save(hotel);
            return modelMapper.map(updatedHotel, HotelDTO.class);
        }
        return null;
    }

    public List<HotelDTO> findHotelsByCity(String city) {
        List<Hotel> hotels = hotelRepository.findByHotelLocationContainingIgnoreCase(city);
        return hotels.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<HotelDTO> searchHotelsByPattern(String pattern) {
        List<Hotel> hotels = hotelRepository.findByHotelNameContainingIgnoreCaseOrHotelDescContainingIgnoreCase(pattern, pattern);
        return hotels.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteHotel(Long id) {
        if (hotelRepository.existsById(id)){
            hotelRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private HotelDTO convertToDTO(Hotel hotel) {
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelId(hotel.getHotelId());
        hotelDTO.setHotelName(hotel.getHotelName());
        hotelDTO.setHotelLocation(hotel.getHotelLocation());
        hotelDTO.setHotelDesc(hotel.getHotelDesc());
        hotelDTO.setHotelRating(hotel.getHotelRating());
        hotelDTO.setHotelAddress(hotel.getHotelAddress());
        hotelDTO.setHotelContactNumber(hotel.getHotelContactNumber());
        hotelDTO.setHotelEmail(hotel.getHotelEmail());
        hotelDTO.setHotelWebsite(hotel.getHotelWebsite());
        return hotelDTO;
    }
}
