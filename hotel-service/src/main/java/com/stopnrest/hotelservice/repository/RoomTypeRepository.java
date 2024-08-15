package com.stopnrest.hotelservice.repository;

import com.stopnrest.hotelservice.dto.RoomTypeDTO;
import com.stopnrest.hotelservice.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    @Query("SELECT rt FROM RoomType rt WHERE rt.roomTypeId IN :roomTypeId")
    List<RoomType> findRoomTypesByRoomTypeIds(@Param("roomTypeId") List<Long> roomTypeIds);
}
