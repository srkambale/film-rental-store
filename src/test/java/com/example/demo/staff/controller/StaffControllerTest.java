package com.example.demo.staff.controller;

import com.example.demo.staff.entity.Staff;
import com.example.demo.staff.service.StaffService;
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

@WebMvcTest(controllers = StaffController.class)
@Import(StaffControllerTest.TestSecurityConfig.class)
public class StaffControllerTest {

    @MockBean
    private StaffService staffService;

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
        private StaffService unitStaffService;

        private StaffController unitStaffController;

        private Staff staff;

        @BeforeEach
        void setUp() {
            unitStaffController = new StaffController(unitStaffService);

            staff = new Staff();
            staff.setStaffId(1);
            staff.setFirstName("John");
            staff.setLastName("Doe");
            staff.setActive(true);
            staff.setUsername("johndoe");
            staff.setPassword("password123");
            staff.setEmail("john.doe@example.com");
        }

        @Test
        void testGetAllStaff() {
            when(unitStaffService.getAllStaff()).thenReturn(Arrays.asList(staff));
            List<Staff> result = unitStaffController.getAllStaff();
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("John", result.get(0).getFirstName());
            verify(unitStaffService).getAllStaff();
        }

        @Test
        void testGetStaffById() {
            when(unitStaffService.getStaffById(1)).thenReturn(staff);
            Staff result = unitStaffController.getStaffById(1);
            assertNotNull(result);
            assertEquals(1, result.getStaffId());
            verify(unitStaffService).getStaffById(1);
        }

        @Test
        void testCreateStaff() {
            when(unitStaffService.createStaff(any(Staff.class))).thenReturn(staff);
            Staff result = unitStaffController.createStaff(staff);
            assertNotNull(result);
            assertEquals("John", result.getFirstName());
            verify(unitStaffService).createStaff(any(Staff.class));
        }

        @Test
        void testUpdateStaff() {
            when(unitStaffService.updateStaff(eq(1), any(Staff.class))).thenReturn(staff);
            Staff result = unitStaffController.updateStaff(1, staff);
            assertNotNull(result);
            assertEquals("John", result.getFirstName());
            verify(unitStaffService).updateStaff(eq(1), any(Staff.class));
        }

        @Test
        void testDeleteStaff() {
            doNothing().when(unitStaffService).deleteStaff(1);
            unitStaffController.deleteStaff(1);
            verify(unitStaffService).deleteStaff(1);
        }
    }

    // =========================================================================
    // 2. API TESTS — inherits outer @WebMvcTest context
    // =========================================================================
    @Nested
    class ApiTests {

        private Staff staff;

        @BeforeEach
        void setUp() {
            staff = new Staff();
            staff.setStaffId(1);
            staff.setFirstName("John");
            staff.setLastName("Doe");
            staff.setActive(true);
            staff.setUsername("johndoe");
            staff.setPassword("password123");
            staff.setEmail("john.doe@example.com");
        }

        @Test
        void GET_allStaff_returns200() throws Exception {
            when(staffService.getAllStaff()).thenReturn(Arrays.asList(staff));
            mockMvc.perform(get("/api/v1/staff"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].staffId").value(1))
                    .andExpect(jsonPath("$[0].firstName").value("John"));
        }

        @Test
        void GET_staffById_returns200() throws Exception {
            when(staffService.getStaffById(1)).thenReturn(staff);
            mockMvc.perform(get("/api/v1/staff/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.staffId").value(1))
                    .andExpect(jsonPath("$.firstName").value("John"));
        }

        @Test
        void POST_createStaff_returns200() throws Exception {
            String jsonPayload = objectMapper.writeValueAsString(staff).replace("}", ",\"password\":\"password123\"}");
            when(staffService.createStaff(any(Staff.class))).thenReturn(staff);
            mockMvc.perform(post("/api/v1/staff")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPayload))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").value("John"));
        }

        @Test
        void PUT_updateStaff_returns200() throws Exception {
            String jsonPayload = objectMapper.writeValueAsString(staff).replace("}", ",\"password\":\"password123\"}");
            when(staffService.updateStaff(eq(1), any(Staff.class))).thenReturn(staff);
            mockMvc.perform(put("/api/v1/staff/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPayload))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").value("John"));
        }

        @Test
        void DELETE_staff_returns200() throws Exception {
            doNothing().when(staffService).deleteStaff(1);
            mockMvc.perform(delete("/api/v1/staff/1"))
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
