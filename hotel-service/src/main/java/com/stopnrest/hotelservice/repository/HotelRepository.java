package com.stopnrest.hotelservice.repository;

import com.stopnrest.hotelservice.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByHotelLocationContainingIgnoreCase(String city);

    List<Hotel> findByHotelNameContainingIgnoreCaseOrHotelDescContainingIgnoreCase(String pattern, String pattern1);
    // Custom query methods (if needed) can be added here
}
