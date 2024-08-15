package com.stopnrest.payment.controller;

import com.stopnrest.payment.claims.JwtClaims;
import com.stopnrest.payment.exception.PaymentCreationException;
import com.stopnrest.payment.model.Payment;
import com.stopnrest.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    JwtClaims jwtClaims;



    @PostMapping("/p/{bookingId}")
    public ResponseEntity<String> createPayment(@RequestHeader("Authorization")String authHeader, @PathVariable Long bookingId) {
        Optional<Payment> existingPayment = paymentService.findByBookingId(bookingId);
        String userName = jwtClaims.extractCredentials(authHeader)[1];
        if (existingPayment.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Payment already exists for booking ID: " + bookingId);
        }

        try {
            Payment savedPayment = paymentService.createPayment(authHeader,userName, bookingId,100.0);
            String maskedCardNumber = "XXXX" + savedPayment.getCardNumber().substring(12);
            String successMessage = "Payment created successfully with ID: " + savedPayment.getPaymentId() + " and Card Number " + maskedCardNumber;

            log.info("Payment created successfully for booking ID: {} by user: {}", bookingId, userName);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(successMessage);
        } catch (PaymentCreationException e) {
            log.error("Failed to create payment for booking ID: {} by user: {}", bookingId, userName, e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create payment: " + e.getMessage());
        }
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> getPaymentByBookingId(@PathVariable Long bookingId) {
        Optional<Payment> payment = paymentService.findByBookingId(bookingId);
        if (payment.isPresent()) {
            return ResponseEntity.ok(payment.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Payment not found for Booking ID: " + bookingId);
        }
    }
}

