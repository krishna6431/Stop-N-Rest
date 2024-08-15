package com.stopnrest.payment.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "booking-service", url = "http://localhost:8084")
public interface BookingServiceClient {
    @PutMapping("/api/v1/bookings/update/booking/{bookingId}")
    ResponseEntity<?> updateBooking(@RequestHeader("Authorization")String authHeader, @PathVariable("bookingId") Long bookingId);

}
