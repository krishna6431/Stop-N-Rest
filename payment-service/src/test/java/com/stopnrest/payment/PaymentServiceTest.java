package com.stopnrest.payment;


import com.stopnrest.payment.clients.BookingServiceClient;
import com.stopnrest.payment.clients.UserClient;
import com.stopnrest.payment.dto.CreditCardDTO;
import com.stopnrest.payment.exception.CreditCardNotFoundException;
import com.stopnrest.payment.exception.PaymentCreationException;
import com.stopnrest.payment.model.Payment;
import com.stopnrest.payment.repository.PaymentRepository;
import com.stopnrest.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BookingServiceClient bookingServiceClient;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByBookingId() {
        // Arrange
        Long bookingId = 1001L;
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        when(paymentRepository.findByBookingId(bookingId)).thenReturn(Optional.of(payment));

        // Act
        Optional<Payment> foundPayment = paymentService.findByBookingId(bookingId);

        // Assert
        assertThat(foundPayment).isPresent();
        assertThat(foundPayment.get().getBookingId()).isEqualTo(bookingId);
    }
}