package com.stopnrest.payment;

import com.stopnrest.payment.claims.JwtClaims;
import com.stopnrest.payment.controller.PaymentController;
import com.stopnrest.payment.exception.PaymentCreationException;
import com.stopnrest.payment.model.Payment;
import com.stopnrest.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private JwtClaims jwtClaims;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePayment_Successful() {
        // Arrange
        String authHeader = "Bearer token";
        Long bookingId = 1001L;
        String userName = "john_doe";
        Payment payment = new Payment();
        payment.setPaymentId(1L);
        payment.setBookingId(bookingId);
        payment.setCardNumber("1234567812345678");

        when(jwtClaims.extractCredentials(authHeader)).thenReturn(new String[]{"Bearer", userName});
        when(paymentService.findByBookingId(bookingId)).thenReturn(Optional.empty());
        when(paymentService.createPayment(authHeader, userName, bookingId, 100.0)).thenReturn(payment);

        // Act
        ResponseEntity<String> response = paymentController.createPayment(authHeader, bookingId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("Payment created successfully with ID: 1 and Card Number XXXX5678");

        verify(paymentService, times(1)).findByBookingId(bookingId);
        verify(paymentService, times(1)).createPayment(authHeader, userName, bookingId, 100.0);
    }

    @Test
    void testCreatePayment_PaymentAlreadyExists() {
        // Arrange
        String authHeader = "Bearer token";
        Long bookingId = 1001L;
        String userName = "john_doe";
        Payment existingPayment = new Payment();
        existingPayment.setPaymentId(1L);
        existingPayment.setBookingId(bookingId);

        when(jwtClaims.extractCredentials(authHeader)).thenReturn(new String[]{"Bearer", userName});
        when(paymentService.findByBookingId(bookingId)).thenReturn(Optional.of(existingPayment));

        // Act
        ResponseEntity<String> response = paymentController.createPayment(authHeader, bookingId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo("Payment already exists for booking ID: " + bookingId);

        verify(paymentService, times(1)).findByBookingId(bookingId);
        verify(paymentService, never()).createPayment(anyString(), anyString(), anyLong(), anyDouble());
    }

    @Test
    void testCreatePayment_FailedCreation() {
        // Arrange
        String authHeader = "Bearer token";
        Long bookingId = 1001L;
        String userName = "john_doe";

        when(jwtClaims.extractCredentials(authHeader)).thenReturn(new String[]{"Bearer", userName});
        when(paymentService.findByBookingId(bookingId)).thenReturn(Optional.empty());
        when(paymentService.createPayment(authHeader, userName, bookingId, 100.0))
                .thenThrow(new PaymentCreationException("Failed to create payment"));

        // Act
        ResponseEntity<String> response = paymentController.createPayment(authHeader, bookingId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Failed to create payment: Failed to create payment");

        verify(paymentService, times(1)).findByBookingId(bookingId);
        verify(paymentService, times(1)).createPayment(authHeader, userName, bookingId, 100.0);
    }

    @Test
    void testGetPaymentByBookingId_Found() {
        // Arrange
        Long bookingId = 1001L;
        Payment payment = new Payment();
        payment.setPaymentId(1L);
        payment.setBookingId(bookingId);

        when(paymentService.findByBookingId(bookingId)).thenReturn(Optional.of(payment));

        // Act
        ResponseEntity<?> response = paymentController.getPaymentByBookingId(bookingId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(payment);

        verify(paymentService, times(1)).findByBookingId(bookingId);
    }

    @Test
    void testGetPaymentByBookingId_NotFound() {
        // Arrange
        Long bookingId = 1001L;

        when(paymentService.findByBookingId(bookingId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = paymentController.getPaymentByBookingId(bookingId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Payment not found for Booking ID: " + bookingId);

        verify(paymentService, times(1)).findByBookingId(bookingId);
    }
}

