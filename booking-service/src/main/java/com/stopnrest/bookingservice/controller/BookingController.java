package com.stopnrest.bookingservice.controller;

import com.stopnrest.bookingservice.claims.JwtClaims;
import com.stopnrest.bookingservice.clients.HotelServiceClient;
import com.stopnrest.bookingservice.clients.UserServiceClient;
import com.stopnrest.bookingservice.entity.Booking;
import com.stopnrest.bookingservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @Autowired
    BookingService bookingService;
    @Autowired
    HotelServiceClient hotelServiceClient;
    @Autowired
    UserServiceClient userServiceClient;
    @Autowired
    JwtClaims jwtClaims;
    @PostMapping("/p/")
    public ResponseEntity<?> createBooking(@RequestBody Booking booking,@RequestHeader("Authorization")String authHeader) {
        return bookingService.createBooking(booking,authHeader);
    }

    @GetMapping
    public List<Booking> getAllBookings(@RequestHeader("Authorization")String authHeader) {
        return bookingService.getAllBookings(authHeader);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBookingById(@RequestHeader("Authorization")String authHeader,@PathVariable Long bookingId) {
        return bookingService.getBookingById(authHeader,bookingId);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/booking/{bookingId}")
    public ResponseEntity<?>updateBooking(@RequestHeader("Authorization")String authHeader,@PathVariable("bookingId") Long bookingId){
        String str[] = jwtClaims.extractCredentials(authHeader);
        userServiceClient.updateUserStatus(authHeader,str[1],bookingId);
        hotelServiceClient.updateRoomStatus(authHeader,bookingService.getRoomID(bookingId));
        return ResponseEntity.status(200).body(bookingService.updateBookingStatus(bookingId));
    }

}
