package com.example.demo.rental.controller;

import com.example.demo.rental.dto.InventoryDto;
import com.example.demo.rental.service.InventoryService;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InventoryController.class)
@Import(InventoryControllerTest.TestSecurityConfig.class)
public class InventoryControllerTest {

    @MockBean
    private InventoryService inventoryService;

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
        private InventoryService unitInventoryService;

        private InventoryController unitInventoryController;

        private InventoryDto inventoryDto;

        @BeforeEach
        void setUp() {
            unitInventoryController = new InventoryController(unitInventoryService);

            inventoryDto = new InventoryDto(1, 1, 1);
        }

        @Test
        void testGetAllInventory() {
            when(unitInventoryService.getAllInventory()).thenReturn(Arrays.asList(inventoryDto));
            ResponseEntity<List<InventoryDto>> response = unitInventoryController.getAllInventory();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().size());
            verify(unitInventoryService).getAllInventory();
        }

        @Test
        void testGetInventoryById() {
            when(unitInventoryService.getInventoryById(1)).thenReturn(inventoryDto);
            ResponseEntity<InventoryDto> response = unitInventoryController.getInventoryById(1);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().getInventoryId());
            verify(unitInventoryService).getInventoryById(1);
        }

        @Test
        void testGetByFilm() {
            when(unitInventoryService.getInventoryByFilm(1)).thenReturn(Arrays.asList(inventoryDto));
            ResponseEntity<List<InventoryDto>> response = unitInventoryController.getByFilm(1);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(unitInventoryService).getInventoryByFilm(1);
        }

        @Test
        void testGetByStore() {
            when(unitInventoryService.getInventoryByStore(1)).thenReturn(Arrays.asList(inventoryDto));
            ResponseEntity<List<InventoryDto>> response = unitInventoryController.getByStore(1);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(unitInventoryService).getInventoryByStore(1);
        }

        @Test
        void testGetAvailableInventory() {
            when(unitInventoryService.getAvailableInventory(1, 1)).thenReturn(Arrays.asList(inventoryDto));
            ResponseEntity<List<InventoryDto>> response = unitInventoryController.getAvailableInventory(1, 1);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(unitInventoryService).getAvailableInventory(1, 1);
        }

        @Test
        void testAddInventory() {
            when(unitInventoryService.addInventory(any(InventoryDto.class))).thenReturn(inventoryDto);
            ResponseEntity<InventoryDto> response = unitInventoryController.addInventory(inventoryDto);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(unitInventoryService).addInventory(any(InventoryDto.class));
        }

        @Test
        void testDeleteInventory() {
            doNothing().when(unitInventoryService).deleteInventory(1);
            ResponseEntity<Void> response = unitInventoryController.deleteInventory(1);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(unitInventoryService).deleteInventory(1);
        }
    }

    // =========================================================================
    // 2. API TESTS — inherits outer @WebMvcTest context
    // =========================================================================
    @Nested
    class ApiTests {

        private InventoryDto inventoryDto;

        @BeforeEach
        void setUp() {
            inventoryDto = new InventoryDto(1, 1, 1);
        }

        @Test
        void GET_allInventory_returns200() throws Exception {
            when(inventoryService.getAllInventory()).thenReturn(Arrays.asList(inventoryDto));
            mockMvc.perform(get("/api/v1/inventory"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].inventoryId").value(1));
        }

        @Test
        void GET_inventoryById_returns200() throws Exception {
            when(inventoryService.getInventoryById(1)).thenReturn(inventoryDto);
            mockMvc.perform(get("/api/v1/inventory/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.inventoryId").value(1));
        }

        @Test
        void GET_inventoryByFilm_returns200() throws Exception {
            when(inventoryService.getInventoryByFilm(1)).thenReturn(Arrays.asList(inventoryDto));
            mockMvc.perform(get("/api/v1/inventory/film/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        }

        @Test
        void GET_inventoryByStore_returns200() throws Exception {
            when(inventoryService.getInventoryByStore(1)).thenReturn(Arrays.asList(inventoryDto));
            mockMvc.perform(get("/api/v1/inventory/store/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        }

        @Test
        void GET_availableInventory_returns200() throws Exception {
            when(inventoryService.getAvailableInventory(1, 1)).thenReturn(Arrays.asList(inventoryDto));
            mockMvc.perform(get("/api/v1/inventory/available")
                            .param("filmId", "1")
                            .param("storeId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].inventoryId").value(1));
        }

        @Test
        void POST_addInventory_returns201() throws Exception {
            when(inventoryService.addInventory(any(InventoryDto.class))).thenReturn(inventoryDto);
            mockMvc.perform(post("/api/v1/inventory")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inventoryDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.inventoryId").value(1));
        }

        @Test
        void DELETE_inventory_returns204() throws Exception {
            doNothing().when(inventoryService).deleteInventory(1);
            mockMvc.perform(delete("/api/v1/inventory/1"))
                    .andExpect(status().isNoContent());
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
