package com.example.demo.customer.controller;

import com.example.demo.customer.dto.PaymentRequestDto;
import com.example.demo.customer.dto.PaymentResponseDto;
import com.example.demo.customer.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// PaymentService and PaymentController use Integer (not Long) for all IDs
@WebMvcTest(controllers = PaymentController.class)
@Import(PaymentControllerTest.TestSecurityConfig.class)
public class PaymentControllerTest {

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================================================================
    // 1. UNIT TESTS — Pure Mockito, Integer IDs throughout
    // =========================================================================
    @Nested
    @ExtendWith(MockitoExtension.class)
    class UnitTests {

        @Mock
        private PaymentService unitPaymentService;

        private PaymentController unitPaymentController;

        private PaymentResponseDto responseDto;

        @BeforeEach
        void setUp() {
            unitPaymentController = new PaymentController(unitPaymentService);
            responseDto = new PaymentResponseDto();
            responseDto.setPaymentId(1);
            responseDto.setCustomerId(1); // Integer
            responseDto.setRentalId(1); // Integer
            responseDto.setAmount(new BigDecimal("10.00"));
            responseDto.setStatus("SUCCESS");
            responseDto.setMessage("Payment of 10.00 processed successfully for rental ID: 1");
        }

        @Test
        void testGetAllPayments_returnsOk() {
            when(unitPaymentService.getAllPayments()).thenReturn(Arrays.asList(responseDto));
            ResponseEntity<List<PaymentResponseDto>> response = unitPaymentController.getAllPayments();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().size());
            verify(unitPaymentService).getAllPayments();
        }

        @Test
        void testGetPaymentById_returnsOk() {
            // getPaymentById(Integer id) — Integer, not Long
            when(unitPaymentService.getPaymentById(1)).thenReturn(responseDto);
            ResponseEntity<PaymentResponseDto> response = unitPaymentController.getPaymentById(1);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().getPaymentId());
            verify(unitPaymentService).getPaymentById(1);
        }

        @Test
        void testGetMyPayments_returnsOk() {
            // getPaymentsByCustomer(Integer customerId)
            when(unitPaymentService.getPaymentsByCustomer(1)).thenReturn(Arrays.asList(responseDto));
            ResponseEntity<List<PaymentResponseDto>> response = unitPaymentController.getMyPayments(1);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(unitPaymentService).getPaymentsByCustomer(1);
        }

        @Test
        void testGetPaymentsByCustomer_returnsOk() {
            when(unitPaymentService.getPaymentsByCustomer(1)).thenReturn(Arrays.asList(responseDto));
            ResponseEntity<List<PaymentResponseDto>> response = unitPaymentController.getPaymentsByCustomer(1);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(unitPaymentService).getPaymentsByCustomer(1);
        }

        @Test
        void testGetPaymentByRental_returnsOk() {
            // getPaymentByRental(Integer rentalId)
            when(unitPaymentService.getPaymentByRental(1)).thenReturn(responseDto);
            ResponseEntity<PaymentResponseDto> response = unitPaymentController.getPaymentByRental(1);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(unitPaymentService).getPaymentByRental(1);
        }

        @Test
        void testGetCustomerBalance_returnsOk() {
            // getCustomerBalance(Integer customerId)
            when(unitPaymentService.getCustomerBalance(1)).thenReturn(new BigDecimal("50.00"));
            ResponseEntity<Map<String, Object>> response = unitPaymentController.getCustomerBalance(1);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(new BigDecimal("50.00"), response.getBody().get("totalSpent"));
            verify(unitPaymentService).getCustomerBalance(1);
        }

        @Test
        void testProcessPayment_returnsCreated() {
            PaymentRequestDto requestDto = new PaymentRequestDto();
            when(unitPaymentService.processPayment(any(PaymentRequestDto.class))).thenReturn(responseDto);
            ResponseEntity<PaymentResponseDto> response = unitPaymentController.processPayment(requestDto);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(unitPaymentService).processPayment(any(PaymentRequestDto.class));
        }
    }

    // =========================================================================
    // 2. API TESTS — MockMvc HTTP layer (inherits outer @WebMvcTest context)
    // =========================================================================
    @Nested
    class ApiTests {

        private PaymentResponseDto responseDto;

        @BeforeEach
        void setUp() {
            responseDto = new PaymentResponseDto();
            responseDto.setPaymentId(1);
            responseDto.setCustomerId(1); // Integer
            responseDto.setRentalId(1); // Integer
            responseDto.setAmount(new BigDecimal("10.00"));
            responseDto.setStatus("SUCCESS");
            responseDto.setMessage("Payment of 10.00 processed successfully for rental ID: 1");
        }

        // ── GET /api/v1/payments ──────────────────────────────────────────────
        @Test
        void GET_allPayments_returns200() throws Exception {
            when(paymentService.getAllPayments()).thenReturn(Arrays.asList(responseDto));
            mockMvc.perform(get("/api/v1/payments"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].paymentId").value(1))
                    .andExpect(jsonPath("$[0].status").value("SUCCESS"));
        }

        // ── GET /api/v1/payments/{id} — Integer path variable ────────────────
        @Test
        void GET_paymentById_returns200() throws Exception {
            when(paymentService.getPaymentById(1)).thenReturn(responseDto);
            mockMvc.perform(get("/api/v1/payments/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.paymentId").value(1))
                    .andExpect(jsonPath("$.amount").value(10.00));
        }

        // ── GET /api/v1/payments/my?customerId=1 — Integer request param ─────
        @Test
        void GET_myPayments_returns200() throws Exception {
            when(paymentService.getPaymentsByCustomer(1)).thenReturn(Arrays.asList(responseDto));
            mockMvc.perform(get("/api/v1/payments/my").param("customerId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        }

        // ── GET /api/v1/payments/customer/{customerId} ────────────────────────
        @Test
        void GET_paymentsByCustomer_returns200() throws Exception {
            when(paymentService.getPaymentsByCustomer(1)).thenReturn(Arrays.asList(responseDto));
            mockMvc.perform(get("/api/v1/payments/customer/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].status").value("SUCCESS"));
        }

        // ── GET /api/v1/payments/rentals/{rentalId} ───────────────────────────
        @Test
        void GET_paymentByRental_returns200() throws Exception {
            when(paymentService.getPaymentByRental(1)).thenReturn(responseDto);
            mockMvc.perform(get("/api/v1/payments/rentals/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("SUCCESS"))
                    .andExpect(jsonPath("$.message").value("Payment of 10.00 processed successfully for rental ID: 1"));
        }

        // ── GET /api/v1/payments/balance/{customerId} ─────────────────────────
        @Test
        void GET_customerBalance_returns200() throws Exception {
            when(paymentService.getCustomerBalance(1)).thenReturn(new BigDecimal("50.00"));
            mockMvc.perform(get("/api/v1/payments/balance/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalSpent").value(50.00))
                    .andExpect(jsonPath("$.currency").value("USD"));
        }

        // ── POST /api/v1/payments ─────────────────────────────────────────────
        @Test
        void POST_processPayment_returns201() throws Exception {
            PaymentRequestDto requestDto = new PaymentRequestDto();
            requestDto.setCustomerId(1); // Integer
            requestDto.setRentalId(1); // Integer
            requestDto.setAmount(new BigDecimal("10.00"));
            requestDto.setStaffId(1);

            when(paymentService.processPayment(any(PaymentRequestDto.class))).thenReturn(responseDto);

            mockMvc.perform(post("/api/v1/payments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("SUCCESS"))
                    .andExpect(jsonPath("$.paymentId").value(1));
        }
    }

    static class TestSecurityConfig {
        @org.springframework.boot.test.mock.mockito.MockBean
        private com.example.demo.auth.service.JwtService jwtService;

        @org.springframework.boot.test.mock.mockito.MockBean
        private com.example.demo.auth.service.UserDetailsServiceImpl userDetailsService;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }
}
