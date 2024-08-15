package com.stopnrest.bookingservice.repository;

import com.stopnrest.bookingservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByUserName(String userName);

    Object findByBookingIdAndUserName(Long bookingId, String userName);
}
