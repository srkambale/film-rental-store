package com.example.demo.customer.controller;

import com.example.demo.customer.dto.*;
import com.example.demo.customer.service.CustomerService;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerController.class)
@Import(CustomerControllerTest.TestSecurityConfig.class)
public class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================================================================
    // 1. UNIT TESTS — Pure Mockito, no Spring context
    // CustomerController and CustomerService both use Integer (not Long) for IDs
    // =========================================================================
    @Nested
    @ExtendWith(MockitoExtension.class)
    class UnitTests {

        @Mock
        private CustomerService unitCustomerService;

        private CustomerController unitCustomerController;

        private CustomerDto customerDto;

        @BeforeEach
        void setUp() {
            unitCustomerController = new CustomerController(unitCustomerService);

            customerDto = new CustomerDto();
            customerDto.setCustomerId(1);       // Integer
            customerDto.setFirstName("Jane");
            customerDto.setLastName("Doe");
        }

        @Test
        void testGetCustomer() {
            // getCustomerById(Integer id)
            when(unitCustomerService.getCustomerById(1)).thenReturn(customerDto);
            CustomerDto result = unitCustomerController.getCustomer(1);
            assertNotNull(result);
            assertEquals(Integer.valueOf(1), result.getCustomerId());
            verify(unitCustomerService).getCustomerById(1);
        }

        @Test
        void testCreateCustomer() {
            CustomerCreateDto createDto = new CustomerCreateDto();
            when(unitCustomerService.createCustomer(any(CustomerCreateDto.class))).thenReturn(customerDto);
            CustomerDto result = unitCustomerController.createCustomer(createDto);
            assertNotNull(result);
            assertEquals("Jane", result.getFirstName());
            verify(unitCustomerService).createCustomer(any(CustomerCreateDto.class));
        }

        @Test
        void testUpdateCustomer() {
            CustomerUpdateDto updateDto = new CustomerUpdateDto();
            // updateCustomer(Integer id, CustomerUpdateDto dto)
            when(unitCustomerService.updateCustomer(eq(1), any(CustomerUpdateDto.class))).thenReturn(customerDto);
            CustomerDto result = unitCustomerController.updateCustomer(1, updateDto);
            assertNotNull(result);
            verify(unitCustomerService).updateCustomer(eq(1), any(CustomerUpdateDto.class));
        }

        @Test
        void testGetRentals() {
            // getCustomerRentals(Integer customerId)
            List<RentalResponseDto> rentals = Arrays.asList(new RentalResponseDto());
            when(unitCustomerService.getCustomerRentals(1)).thenReturn(rentals);
            List<RentalResponseDto> result = unitCustomerController.getRentals(1);
            assertEquals(1, result.size());
            verify(unitCustomerService).getCustomerRentals(1);
        }

        @Test
        void testGetPayments() {
            // getCustomerPayments(Integer customerId)
            List<PaymentDto> payments = Arrays.asList(new PaymentDto());
            when(unitCustomerService.getCustomerPayments(1)).thenReturn(payments);
            List<PaymentDto> result = unitCustomerController.getPayments(1);
            assertEquals(1, result.size());
            verify(unitCustomerService).getCustomerPayments(1);
        }

        @Test
        void testGetCustomersByName() {
            List<CustomerDto> list = Arrays.asList(customerDto);
            when(unitCustomerService.getCustomersByName("Jane")).thenReturn(list);
            List<CustomerDto> result = unitCustomerController.getCustomersByName("Jane");
            assertEquals(1, result.size());
            verify(unitCustomerService).getCustomersByName("Jane");
        }
    }

    // =========================================================================
    // 2. API TESTS — MockMvc HTTP layer (inherits outer @WebMvcTest context)
    // =========================================================================
    @Nested
    class ApiTests {

        private CustomerDto customerDto;

        @BeforeEach
        void setUp() {
            customerDto = new CustomerDto();
            customerDto.setCustomerId(1);
            customerDto.setFirstName("Jane");
            customerDto.setLastName("Doe");
        }

        // ── GET /api/v1/customer/{id} ─────────────────────────────────────────
        @Test
        void GET_customer_byId_returns200() throws Exception {
            // Controller: getCustomer(@PathVariable Integer id)
            when(customerService.getCustomerById(1)).thenReturn(customerDto);

            mockMvc.perform(get("/api/v1/customer/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.customerId").value(1))
                    .andExpect(jsonPath("$.firstName").value("Jane"));
        }

        // ── POST /api/v1/customer ─────────────────────────────────────────────
        @Test
        void POST_createCustomer_returns200() throws Exception {
            CustomerCreateDto createDto = new CustomerCreateDto();
            createDto.setFirstName("Jane");
            createDto.setLastName("Doe");
            createDto.setStoreId(1);
            createDto.setAddressId(1L);
            when(customerService.createCustomer(any(CustomerCreateDto.class))).thenReturn(customerDto);

            mockMvc.perform(post("/api/v1/customer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").value("Jane"));
        }

        // ── GET /api/v1/customer/{id}/rentals ─────────────────────────────────
        @Test
        void GET_customerRentals_returns200() throws Exception {
            // Controller: getRentals(@PathVariable Integer id)
            when(customerService.getCustomerRentals(1)).thenReturn(Arrays.asList(new RentalResponseDto()));

            mockMvc.perform(get("/api/v1/customer/1/rentals"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        }

        // ── GET /api/v1/customer/{id}/payments ────────────────────────────────
        @Test
        void GET_customerPayments_returns200() throws Exception {
            // Controller: getPayments(@PathVariable Integer id)
            when(customerService.getCustomerPayments(1)).thenReturn(Arrays.asList(new PaymentDto()));

            mockMvc.perform(get("/api/v1/customer/1/payments"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        }

        // ── GET /api/v1/customer/search?name=Jane ─────────────────────────────
        @Test
        void GET_searchByName_returns200() throws Exception {
            when(customerService.getCustomersByName("Jane")).thenReturn(Arrays.asList(customerDto));

            mockMvc.perform(get("/api/v1/customer/search").param("name", "Jane"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].firstName").value("Jane"));
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
