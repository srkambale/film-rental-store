package com.example.demo.customer.controller;

import com.example.demo.customer.dto.PaymentRequestDto;
import com.example.demo.customer.dto.PaymentResponseDto;
import com.example.demo.customer.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerMockitoTest {

    @Mock
    private PaymentService paymentService; // Mock the dependency

    @InjectMocks
    private PaymentController paymentController; // Inject mock into the controller

    @Test
    void processPayment_ShouldReturnCreatedStatus() {
        // 1. Arrange
        PaymentRequestDto request = new PaymentRequestDto();
        request.setAmount(new BigDecimal("100.00"));
        
        PaymentResponseDto mockResponse = new PaymentResponseDto();
        mockResponse.setStatus("SUCCESS");
        
        when(paymentService.processPayment(request)).thenReturn(mockResponse);

        // 2. Act
        // We call the method directly like a normal Java method
        ResponseEntity<PaymentResponseDto> response = paymentController.processPayment(request);

        // 3. Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode()); // Verifies the 201 status
        assertEquals("SUCCESS", response.getBody().getStatus());
    }

    @Test
    void getCustomerBalance_ShouldReturnMap() {
        // 1. Arrange
        Long customerId = 1L;
        BigDecimal mockBalance = new BigDecimal("250.00");
        when(paymentService.getCustomerBalance(customerId)).thenReturn(mockBalance);

        // 2. Act
        ResponseEntity<Map<String, Object>> response = paymentController.getCustomerBalance(customerId);

        // 3. Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBalance, response.getBody().get("totalSpent"));
        assertEquals(customerId, response.getBody().get("customerId"));
    }
}