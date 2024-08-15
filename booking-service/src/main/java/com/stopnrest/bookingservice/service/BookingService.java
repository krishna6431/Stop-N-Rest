package com.stopnrest.bookingservice.service;

import com.stopnrest.bookingservice.claims.JwtClaims;
import com.stopnrest.bookingservice.clients.HotelServiceClient;
import com.stopnrest.bookingservice.clients.UserServiceClient;
import com.stopnrest.bookingservice.entity.Booking;
import com.stopnrest.bookingservice.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserServiceClient userServiceClient;
    @Autowired
    private HotelServiceClient hotelServiceClient;
    @Autowired
    private JwtClaims jwtClaims;

    public ResponseEntity<?> createBooking(Booking booking , @RequestHeader("Authorization")String authHeader) {
        if(userServiceClient.getUserByUsername(authHeader,booking.getUserName()).getStatusCode().is2xxSuccessful()){
            if(hotelServiceClient.getRoomAvailableByHotel(authHeader,booking.getHotelId(), booking.getRoomId()).getStatusCode().is2xxSuccessful()){
                booking.setBookingStatus("PENDING");
                booking.setCheckIn(LocalDate.now());
                booking.setCheckOut(LocalDate.from(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(11, 0))));
                booking.setIsActive(true);
                return ResponseEntity.status(201).body("Booking Created..Now Please Pay Amount\n"+bookingRepository.save(booking));
            }
        }
        return ResponseEntity.status(404).body("Something Went Wrong");
    }

    public List<Booking> getAllBookings(@RequestHeader("Authorization")String authHeader) {
        System.out.println(authHeader);
        String str[] = jwtClaims.extractCredentials(authHeader);
        if(str[0].contains("SUPER_ADMIN") || str[0].contains("ADMIN")){
            return  bookingRepository.findAll();
        }
        else if(str[0].contains("PUBLIC")){
            return bookingRepository.findAllByUserName(str[1]);

        }
        return bookingRepository.findAll();
    }

    public ResponseEntity<?> getBookingById(@RequestHeader("Authorization")String authHeader, Long bookingId) {
        String str[] = jwtClaims.extractCredentials(authHeader);
        return ResponseEntity.status(200).body(bookingRepository.findByBookingIdAndUserName(bookingId,str[1]));
    }

    public void deleteBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    public ResponseEntity<?> updateBookingStatus(Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            booking.setBookingStatus("Booked"); // Replace with the actual status you want to set
            bookingRepository.save(booking);
            return ResponseEntity.status(HttpStatus.OK).body("Booking status updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found.");
        }
    }
    public Long getRoomID(Long bookingId){
        return bookingRepository.findById(bookingId).get().getRoomId();
    }
}
