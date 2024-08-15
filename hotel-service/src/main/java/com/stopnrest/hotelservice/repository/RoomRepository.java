package com.stopnrest.hotelservice.repository;

import com.stopnrest.hotelservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT DISTINCT r.roomType.roomTypeId FROM Room r WHERE r.hotel.hotelId = :hotelId")
    List<Long> findRoomTypeIdsByHotelId(Long hotelId);

    void deleteByRoomType_RoomTypeId(Long id);

    List<Room> findByHotel_HotelId(Long hotelId);
}
