package com.example.demo.customer.controller;

import com.example.demo.customer.dto.RentalRequestDto;
import com.example.demo.customer.dto.RentalResponseDto;
import com.example.demo.customer.service.CustomerRentalService;
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
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// CustomerRentalController and CustomerRentalService use Integer (not Long) for rentalId
@WebMvcTest(controllers = CustomerRentalController.class)
@Import(CustomerRentalControllerTest.TestSecurityConfig.class)
public class CustomerRentalControllerTest {

    @MockBean
    private CustomerRentalService rentalService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================================================================
    // 1. UNIT TESTS — Pure Mockito, Integer IDs
    // =========================================================================
    @Nested
    @ExtendWith(MockitoExtension.class)
    class UnitTests {

        @Mock
        private CustomerRentalService unitRentalService;

        @InjectMocks
        private CustomerRentalController unitController;

        private RentalResponseDto responseDto;

        @BeforeEach
        void setUp() {
            responseDto = new RentalResponseDto();
            responseDto.setRentalId(1);      // Integer, not Long
            responseDto.setStatus("RENTED");
        }

        @Test
        void testCreateRental() {
            RentalRequestDto requestDto = new RentalRequestDto();
            when(unitRentalService.createRental(any(RentalRequestDto.class))).thenReturn(responseDto);
            RentalResponseDto result = unitController.createRental(requestDto);
            assertNotNull(result);
            assertEquals(Integer.valueOf(1), result.getRentalId());
            verify(unitRentalService).createRental(any(RentalRequestDto.class));
        }

        @Test
        void testReturnFilm() {
            // returnFilm(Integer rentalId) — Integer, not Long
            when(unitRentalService.returnFilm(1)).thenReturn(responseDto);
            RentalResponseDto result = unitController.returnFilm(1);
            assertNotNull(result);
            assertEquals(Integer.valueOf(1), result.getRentalId());
            verify(unitRentalService).returnFilm(1);
        }
    }

    // =========================================================================
    // 2. API TESTS — MockMvc HTTP layer (inherits outer @WebMvcTest context)
    // =========================================================================
    @Nested
    class ApiTests {

        private RentalResponseDto responseDto;

        @BeforeEach
        void setUp() {
            responseDto = new RentalResponseDto();
            responseDto.setRentalId(1);      // Integer
            responseDto.setStatus("RENTED");
        }

        // ── POST /api/v1/customer/rentals ─────────────────────────────────────
        @Test
        void POST_createRental_returns200() throws Exception {
            RentalRequestDto requestDto = new RentalRequestDto();
            when(rentalService.createRental(any(RentalRequestDto.class))).thenReturn(responseDto);

            mockMvc.perform(post("/api/v1/customer/rentals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.rentalId").value(1))
                    .andExpect(jsonPath("$.status").value("RENTED"));
        }

        // ── PUT /api/v1/customer/rentals/{rentalId}/return — Integer path var ─
        @Test
        void PUT_returnFilm_returns200() throws Exception {
            responseDto.setStatus("RETURNED");
            // returnFilm(@PathVariable Integer rentalId)
            when(rentalService.returnFilm(1)).thenReturn(responseDto);

            mockMvc.perform(put("/api/v1/customer/rentals/1/return"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.rentalId").value(1))
                    .andExpect(jsonPath("$.status").value("RETURNED"));
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
