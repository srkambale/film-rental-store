package com.example.demo.customer.service;

import com.example.demo.customer.dto.PaymentRequestDto;
import com.example.demo.customer.dto.PaymentResponseDto;
import com.example.demo.customer.exception.CustomerResourceNotFoundException;
import com.example.demo.customer.model.Customer;
import com.example.demo.customer.model.CustomerRental;
import com.example.demo.customer.model.Payment;
import com.example.demo.customer.repository.CustomerRentalRepository;
import com.example.demo.customer.repository.CustomerRepository;
import com.example.demo.customer.repository.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerRentalRepository rentalRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("Should process payment successfully")
    void processPayment_Success() {
        // 1. Arrange
        PaymentRequestDto request = new PaymentRequestDto();
        request.setCustomerId(1L);
        request.setRentalId(100L);
        request.setAmount(new BigDecimal("50.00"));

        Customer mockCustomer = new Customer();
        CustomerRental mockRental = new CustomerRental();
        mockRental.setRentalId(100L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
        when(rentalRepository.fetchByCustomerIdAndRentalId(1L, 100L)).thenReturn(Optional.of(mockRental));
        when(paymentRepository.findAll()).thenReturn(Collections.emptyList()); // No duplicate
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        // 2. Act
        PaymentResponseDto response = paymentService.processPayment(request);

        // 3. Assert
        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should throw exception when customer is not found")
    void processPayment_CustomerNotFound() {
        // Arrange
        PaymentRequestDto request = new PaymentRequestDto();
        request.setCustomerId(1L);
        request.setRentalId(100L);
        request.setAmount(new BigDecimal("10.00"));

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerResourceNotFoundException.class, () -> {
            paymentService.processPayment(request);
        });

        // Verify save was never called
        verify(paymentRepository, never()).save(any());
    }
}