package com.example.demo.rental.controller;

import com.example.demo.rental.entity.Rental;
import com.example.demo.rental.service.RentalService;
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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RentalController.class)
@Import(RentalControllerTest.TestSecurityConfig.class)
public class RentalControllerTest {

    @MockBean
    private RentalService rentalService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================================================================
    // 1. UNIT TESTS
    // =========================================================================
    @Nested
    @ExtendWith(MockitoExtension.class)
    class UnitTests {

        @Mock
        private RentalService unitRentalService;

        private RentalController unitRentalController;

        private Rental rental;

        @BeforeEach
        void setUp() {
            unitRentalController = new RentalController(unitRentalService);

            rental = new Rental();
            rental.setRentalId(1);
            rental.setRentalDate(LocalDateTime.now());
            rental.setStaffId(1);
        }

        @Test
        void testCreateRental() {
            when(unitRentalService.createRental(1, 1, 1)).thenReturn(rental);
            Rental result = unitRentalController.createRental(1, 1, 1);
            assertNotNull(result);
            assertEquals(1, result.getRentalId());
            verify(unitRentalService).createRental(1, 1, 1);
        }

        @Test
        void testGetAllRentals() {
            when(unitRentalService.getAllRentals()).thenReturn(Arrays.asList(rental));
            List<Rental> result = unitRentalController.getAllRentals();
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(unitRentalService).getAllRentals();
        }

        @Test
        void testGetRentalById() {
            when(unitRentalService.getRentalById(1)).thenReturn(rental);
            Rental result = unitRentalController.getRentalById(1);
            assertNotNull(result);
            assertEquals(1, result.getRentalId());
            verify(unitRentalService).getRentalById(1);
        }

        @Test
        void testReturnMovie() {
            rental.setReturnDate(LocalDateTime.now());
            when(unitRentalService.returnMovie(1)).thenReturn(rental);
            Rental result = unitRentalController.returnMovie(1);
            assertNotNull(result);
            assertNotNull(result.getReturnDate());
            verify(unitRentalService).returnMovie(1);
        }

        @Test
        void testDeleteRental() {
            doNothing().when(unitRentalService).deleteRental(1);
            String result = unitRentalController.deleteRental(1);
            assertEquals("Rental deleted successfully", result);
            verify(unitRentalService).deleteRental(1);
        }
    }

    // =========================================================================
    // 2. API TESTS — inherits outer @WebMvcTest context
    // =========================================================================
    @Nested
    class ApiTests {

        private Rental rental;

        @BeforeEach
        void setUp() {
            rental = new Rental();
            rental.setRentalId(1);
            rental.setRentalDate(LocalDateTime.now());
            rental.setStaffId(1);
        }

        @Test
        void POST_createRental_returns200() throws Exception {
            when(rentalService.createRental(1, 1, 1)).thenReturn(rental);
            mockMvc.perform(post("/api/v1/rentals")
                            .param("inventoryId", "1")
                            .param("customerId", "1")
                            .param("staffId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.rentalId").value(1));
        }

        @Test
        void GET_allRentals_returns200() throws Exception {
            when(rentalService.getAllRentals()).thenReturn(Arrays.asList(rental));
            mockMvc.perform(get("/api/v1/rentals"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].rentalId").value(1));
        }

        @Test
        void GET_rentalById_returns200() throws Exception {
            when(rentalService.getRentalById(1)).thenReturn(rental);
            mockMvc.perform(get("/api/v1/rentals/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.rentalId").value(1));
        }

        @Test
        void PUT_returnMovie_returns200() throws Exception {
            rental.setReturnDate(LocalDateTime.now());
            when(rentalService.returnMovie(1)).thenReturn(rental);
            mockMvc.perform(put("/api/v1/rentals/1/return"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.rentalId").value(1));
        }

        @Test
        void DELETE_rental_returns200() throws Exception {
            doNothing().when(rentalService).deleteRental(1);
            mockMvc.perform(delete("/api/v1/rentals/1"))
                    .andExpect(status().isOk());
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
