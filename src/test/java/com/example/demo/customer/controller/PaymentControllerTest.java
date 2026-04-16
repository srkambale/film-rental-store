package com.example.demo.customer.controller;

import com.example.demo.customer.dto.PaymentRequestDto;
import com.example.demo.customer.dto.PaymentResponseDto;
import com.example.demo.customer.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private PaymentResponseDto responseDto;

    @BeforeEach
    void setUp() {
        responseDto = new PaymentResponseDto();
        responseDto.setPaymentId(1);
        responseDto.setAmount(new BigDecimal("10.00"));
        responseDto.setStatus("SUCCESS");
    }

    @Test
    void testGetAllPayments() {
        List<PaymentResponseDto> list = Arrays.asList(responseDto);
        when(paymentService.getAllPayments()).thenReturn(list);

        ResponseEntity<List<PaymentResponseDto>> response = paymentController.getAllPayments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(paymentService, times(1)).getAllPayments();
    }

    @Test
    void testGetPaymentsByCustomer() {
        List<PaymentResponseDto> list = Arrays.asList(responseDto);
        when(paymentService.getPaymentsByCustomer(1)).thenReturn(list);

        ResponseEntity<List<PaymentResponseDto>> response = paymentController.getPaymentsByCustomer(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(paymentService, times(1)).getPaymentsByCustomer(1);
    }

    @Test
    void testGetCustomerBalance() {
        when(paymentService.getCustomerBalance(1)).thenReturn(new BigDecimal("50.00"));

        ResponseEntity<Map<String, Object>> response = paymentController.getCustomerBalance(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new BigDecimal("50.00"), response.getBody().get("totalSpent"));
        verify(paymentService, times(1)).getCustomerBalance(1);
    }

    @Test
    void testProcessPayment() {
        PaymentRequestDto requestDto = new PaymentRequestDto();
        when(paymentService.processPayment(any(PaymentRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<PaymentResponseDto> response = paymentController.processPayment(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(paymentService, times(1)).processPayment(any(PaymentRequestDto.class));
    }
}
