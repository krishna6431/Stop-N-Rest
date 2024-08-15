package com.stopnrest.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle specific exception (e.g., PaymentCreationException)
    @ExceptionHandler(PaymentCreationException.class)
    public ResponseEntity<String> handlePaymentCreationException(PaymentCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create payment: " + ex.getMessage());
    }


    @ExceptionHandler(CreditCardNotFoundException.class)
    public ResponseEntity<String> handleCreditCardNotFoundException(CreditCardNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}

