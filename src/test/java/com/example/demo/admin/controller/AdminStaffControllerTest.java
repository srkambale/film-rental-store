package com.example.demo.admin.controller;

import com.example.demo.admin.dto.AdminStaffRequest;
import com.example.demo.admin.service.AdminStaffService;
import com.example.demo.staff.entity.Staff;
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

@WebMvcTest(controllers = AdminStaffController.class)
@Import(AdminStaffControllerTest.TestSecurityConfig.class)
public class AdminStaffControllerTest {

    @MockBean
    private AdminStaffService adminStaffService;

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
        private AdminStaffService unitAdminStaffService;

        private AdminStaffController unitAdminStaffController;

        private Staff staff;
        private AdminStaffRequest staffRequest;

        @BeforeEach
        void setUp() {
            unitAdminStaffController = new AdminStaffController(unitAdminStaffService);

            staff = new Staff();
            staff.setStaffId(1);
            staff.setFirstName("Admin");
            staff.setLastName("User");

            staffRequest = new AdminStaffRequest();
            staffRequest.setFirstName("Admin");
            staffRequest.setLastName("User");
            staffRequest.setEmail("admin@example.com");
            staffRequest.setUsername("adminuser");
            staffRequest.setPassword("password");
            staffRequest.setStoreId(1);
            staffRequest.setRole("ADMIN");
        }

        @Test
        void testGetAllStaff() {
            when(unitAdminStaffService.getAllStaff()).thenReturn(Arrays.asList(staff));
            ResponseEntity<List<Staff>> response = unitAdminStaffController.getAllStaff();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().size());
            verify(unitAdminStaffService).getAllStaff();
        }

        @Test
        void testGetStaffById() {
            when(unitAdminStaffService.getStaffById(1)).thenReturn(staff);
            ResponseEntity<Staff> response = unitAdminStaffController.getStaffById(1);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().getStaffId());
            verify(unitAdminStaffService).getStaffById(1);
        }

        @Test
        void testSearchStaffByName() {
            when(unitAdminStaffService.searchByName("Admin")).thenReturn(Arrays.asList(staff));
            ResponseEntity<List<Staff>> response = unitAdminStaffController.searchStaffByName("Admin");
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().size());
            verify(unitAdminStaffService).searchByName("Admin");
        }

        @Test
        void testCreateStaff() {
            when(unitAdminStaffService.createStaff(any(AdminStaffRequest.class))).thenReturn(staff);
            ResponseEntity<Staff> response = unitAdminStaffController.createStaff(staffRequest);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(unitAdminStaffService).createStaff(any(AdminStaffRequest.class));
        }

        @Test
        void testUpdateStaff() {
            when(unitAdminStaffService.updateStaff(eq(1), any(AdminStaffRequest.class))).thenReturn(staff);
            ResponseEntity<Staff> response = unitAdminStaffController.updateStaff(1, staffRequest);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(unitAdminStaffService).updateStaff(eq(1), any(AdminStaffRequest.class));
        }

        @Test
        void testDeleteStaff() {
            doNothing().when(unitAdminStaffService).deleteStaff(1);
            ResponseEntity<Void> response = unitAdminStaffController.deleteStaff(1);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(unitAdminStaffService).deleteStaff(1);
        }
    }

    // =========================================================================
    // 2. API TESTS — inherits outer @WebMvcTest context
    // =========================================================================
    @Nested
    class ApiTests {

        private Staff staff;
        private AdminStaffRequest staffRequest;

        @BeforeEach
        void setUp() {
            staff = new Staff();
            staff.setStaffId(1);
            staff.setFirstName("Admin");
            staff.setLastName("User");

            staffRequest = new AdminStaffRequest();
            staffRequest.setFirstName("Admin");
            staffRequest.setLastName("User");
            staffRequest.setEmail("admin@example.com");
            staffRequest.setUsername("adminuser");
            staffRequest.setPassword("password");
            staffRequest.setStoreId(1);
            staffRequest.setRole("ADMIN");
        }

        @Test
        void GET_allAdminStaff_returns200() throws Exception {
            when(adminStaffService.getAllStaff()).thenReturn(Arrays.asList(staff));
            mockMvc.perform(get("/api/v1/admin/staff"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].staffId").value(1))
                    .andExpect(jsonPath("$[0].firstName").value("Admin"));
        }

        @Test
        void GET_adminStaffById_returns200() throws Exception {
            when(adminStaffService.getStaffById(1)).thenReturn(staff);
            mockMvc.perform(get("/api/v1/admin/staff/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.staffId").value(1));
        }

        @Test
        void GET_searchAdminStaff_returns200() throws Exception {
            when(adminStaffService.searchByName("Admin")).thenReturn(Arrays.asList(staff));
            mockMvc.perform(get("/api/v1/admin/staff/search").param("name", "Admin"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].firstName").value("Admin"));
        }

        @Test
        void POST_createAdminStaff_returns201() throws Exception {
            when(adminStaffService.createStaff(any(AdminStaffRequest.class))).thenReturn(staff);
            mockMvc.perform(post("/api/v1/admin/staff")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(staffRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.firstName").value("Admin"));
        }

        @Test
        void PUT_updateAdminStaff_returns200() throws Exception {
            when(adminStaffService.updateStaff(eq(1), any(AdminStaffRequest.class))).thenReturn(staff);
            mockMvc.perform(put("/api/v1/admin/staff/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(staffRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").value("Admin"));
        }

        @Test
        void DELETE_adminStaff_returns204() throws Exception {
            doNothing().when(adminStaffService).deleteStaff(1);
            mockMvc.perform(delete("/api/v1/admin/staff/1"))
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
