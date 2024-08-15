package com.stopnrest.payment.service;

import com.stopnrest.payment.clients.BookingServiceClient;
import com.stopnrest.payment.clients.UserClient;
import com.stopnrest.payment.dto.CreditCardDTO;
import com.stopnrest.payment.exception.CreditCardNotFoundException;
import com.stopnrest.payment.exception.PaymentCreationException;
import com.stopnrest.payment.model.Payment;
import com.stopnrest.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingServiceClient bookingServiceClient;

    @Autowired
    private UserClient userClient;

    public Optional<Payment> findByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    public Payment createPayment(String authHeader,String userName, Long bookingId, Double amountPaid) {
        log.info("Creating payment for user: {} and booking ID: {}", userName, bookingId);
        List<CreditCardDTO> creditCards = fetchCreditCards(userName,authHeader);
        String cardNumber = selectCardForPayment(creditCards);
        Payment savedPayment = processPayment(bookingId, amountPaid, cardNumber);
        bookingServiceClient.updateBooking(authHeader,bookingId);
        log.info("Payment created successfully with ID: {} and booking status updated", savedPayment.getPaymentId());
        return savedPayment;
    }

    private List<CreditCardDTO> fetchCreditCards(String userName,String authHeader) {
        try {
            return userClient.getCreditCards(userName,authHeader);
        } catch (Exception e) {
            throw new PaymentCreationException("Failed to fetch credit card details for user: " + userName);
        }
    }

    private String selectCardForPayment(List<CreditCardDTO> creditCards) {
        if (creditCards.isEmpty()) {
            throw new CreditCardNotFoundException("No credit card found for user");
        }
        return creditCards.get(0).getCardNumber();
    }

    private Payment processPayment(Long bookingId, Double amountPaid, String cardNumber) {
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setAmountPaid(amountPaid);
        payment.setCardNumber(cardNumber);
        payment.setPaymentStatus("success");

        return savePayment(payment);
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
}























