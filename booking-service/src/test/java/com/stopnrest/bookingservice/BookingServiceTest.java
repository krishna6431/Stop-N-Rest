package com.stopnrest.bookingservice;

import com.stopnrest.bookingservice.claims.JwtClaims;
import com.stopnrest.bookingservice.clients.HotelServiceClient;
import com.stopnrest.bookingservice.clients.UserServiceClient;
import com.stopnrest.bookingservice.entity.Booking;
import com.stopnrest.bookingservice.repository.BookingRepository;
import com.stopnrest.bookingservice.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private HotelServiceClient hotelServiceClient;

    @Mock
    private JwtClaims jwtClaims;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBooking_Success() {
        Booking booking = new Booking();
        booking.setUserName("user1");
        booking.setHotelId(1L);
        booking.setRoomId(1L);

        when(userServiceClient.getUserByUsername(anyString(), anyString())).thenReturn(ResponseEntity.ok().build());
        when(hotelServiceClient.getRoomAvailableByHotel(anyString(), anyLong(), anyLong())).thenReturn(ResponseEntity.ok().build());
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        ResponseEntity<?> response = bookingService.createBooking(booking, "Bearer token");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Booking Created..Now Please Pay Amount\n" + booking, response.getBody());
    }

    @Test
    public void testCreateBooking_Failure() {
        Booking booking = new Booking();
        booking.setUserName("user1");
        booking.setHotelId(1L);
        booking.setRoomId(1L);

        when(userServiceClient.getUserByUsername(anyString(), anyString())).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        ResponseEntity<?> response = bookingService.createBooking(booking, "Bearer token");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Something Went Wrong", response.getBody());
    }

    @Test
    public void testGetAllBookings_Admin() {
        when(jwtClaims.extractCredentials(anyString())).thenReturn(new String[]{"ADMIN", "user1"});
        when(bookingRepository.findAll()).thenReturn(List.of(new Booking()));

        List<Booking> bookings = bookingService.getAllBookings("Bearer token");

        assertEquals(1, bookings.size());
        verify(bookingRepository).findAll();
    }

    @Test
    public void testGetAllBookings_Public() {
        when(jwtClaims.extractCredentials(anyString())).thenReturn(new String[]{"PUBLIC", "user1"});
        when(bookingRepository.findAllByUserName(anyString())).thenReturn(List.of(new Booking()));

        List<Booking> bookings = bookingService.getAllBookings("Bearer token");

        assertEquals(1, bookings.size());
        verify(bookingRepository).findAllByUserName("user1");
    }

    @Test
    public void testGetBookingById() {
        Booking booking = new Booking();
        booking.setBookingId(1L);

        when(jwtClaims.extractCredentials(anyString())).thenReturn(new String[]{"PUBLIC", "user1"});
        when(bookingRepository.findByBookingIdAndUserName(anyLong(), anyString())).thenReturn(booking);

        ResponseEntity<?> response = bookingService.getBookingById("Bearer token", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(booking, response.getBody());
    }

    @Test
    public void testDeleteBooking() {
        doNothing().when(bookingRepository).deleteById(anyLong());

        bookingService.deleteBooking(1L);

        verify(bookingRepository).deleteById(1L);
    }

    @Test
    public void testUpdateBookingStatus_Success() {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setBookingStatus("Pending");

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        ResponseEntity<?> response = bookingService.updateBookingStatus(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Booking status updated successfully.", response.getBody());
        assertEquals("Booked", booking.getBookingStatus());
    }

    @Test
    public void testUpdateBookingStatus_NotFound() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookingService.updateBookingStatus(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Booking not found.", response.getBody());
    }

    @Test
    public void testGetRoomID() {
        Booking booking = new Booking();
        booking.setRoomId(1L);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Long roomId = bookingService.getRoomID(1L);

        assertEquals(1L, roomId);
    }
}
