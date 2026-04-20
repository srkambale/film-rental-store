package com.example.demo.customer.service;

import com.example.demo.customer.dto.PaymentRequestDto;
import com.example.demo.customer.dto.PaymentResponseDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Contract for Payment business operations.
 */
public interface PaymentService {

    /**
     * Process (create) a new payment for an existing rental.
     */
    PaymentResponseDto processPayment(PaymentRequestDto request);

    /**
     * Retrieve all payments in the system.
     */
    List<PaymentResponseDto> getAllPayments();

    /**
     * Retrieve a single payment by its ID.
     */
    PaymentResponseDto getPaymentById(Integer paymentId);

    /**
     * Retrieve all payments made by a specific customer.
     */
    List<PaymentResponseDto> getPaymentsByCustomer(Integer customerId);

    /**
     * Retrieve all payments made by the logged in customer using their email.
     */
    List<PaymentResponseDto> getMyPayments(String email);

    /**
     * Retrieve the payment associated with a specific rental.
     */
    PaymentResponseDto getPaymentByRental(Integer rentalId);

    /**
     * Calculate the total amount spent by a customer (their balance).
     */
    BigDecimal getCustomerBalance(Integer customerId);
}
