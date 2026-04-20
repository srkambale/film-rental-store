package com.example.demo.auth.controller;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.auth.service.AuthService;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@Import(AuthControllerTest.TestSecurityConfig.class)
public class AuthControllerTest {

    @MockBean
    private AuthService authService;

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
        private AuthService unitAuthService;

        private AuthController unitAuthController;

        private AuthResponse authResponse;

        @BeforeEach
        void setUp() {
            unitAuthController = new AuthController(unitAuthService);

            authResponse = new AuthResponse();
            authResponse.setToken("mock-jwt-token");
            authResponse.setIdentifier("testuser@example.com");
            authResponse.setRole("CUSTOMER");
        }

        @Test
        void testRegister() {
            RegisterRequest request = new RegisterRequest();
            when(unitAuthService.register(any(RegisterRequest.class))).thenReturn(authResponse);
            ResponseEntity<AuthResponse> response = unitAuthController.register(request);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals("mock-jwt-token", response.getBody().getToken());
            verify(unitAuthService).register(any(RegisterRequest.class));
        }

        @Test
        void testLogin() {
            LoginRequest request = new LoginRequest();
            when(unitAuthService.login(any(LoginRequest.class))).thenReturn(authResponse);
            ResponseEntity<AuthResponse> response = unitAuthController.login(request);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(unitAuthService).login(any(LoginRequest.class));
        }

        @Test
        void testTestHash() {
            ResponseEntity<String> response = unitAuthController.testHash();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().contains("FreshHash"));
        }

        @Test
        void testGetCurrentUser() {
            UserDetails userDetails = new User("testuser", "password",
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
            ResponseEntity<?> response = unitAuthController.getCurrentUser(userDetails);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().toString().contains("testuser"));
        }
    }

    // =========================================================================
    // 2. API TESTS — inherits outer @WebMvcTest context
    // =========================================================================
    @Nested
    class ApiTests {

        private AuthResponse authResponse;

        @BeforeEach
        void setUp() {
            authResponse = new AuthResponse();
            authResponse.setToken("mock-jwt-token");
            authResponse.setIdentifier("testuser@example.com");
            authResponse.setRole("CUSTOMER");
        }

        // ── POST /api/v1/auth/register ────────────────────────────────────────
        // RegisterRequest has: firstName, lastName, email, password, role
        // It does NOT have a setIdentifier() method
        @Test
        void POST_register_returns201() throws Exception {
            RegisterRequest request = new RegisterRequest();
            request.setFirstName("Jane");
            request.setLastName("Doe");
            request.setEmail("testuser@example.com");
            request.setPassword("Test123");
            request.setRole("CUSTOMER");

            when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                    .andExpect(jsonPath("$.identifier").value("testuser@example.com"));
        }

        // ── POST /api/v1/auth/login ───────────────────────────────────────────
        // LoginRequest has: identifier, password
        @Test
        void POST_login_returns200() throws Exception {
            LoginRequest request = new LoginRequest();
            request.setIdentifier("testuser@example.com");
            request.setPassword("Test123");

            when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("mock-jwt-token"));
        }

        // ── GET /api/v1/auth/test-hash ────────────────────────────────────────
        @Test
        void GET_testHash_returns200() throws Exception {
            mockMvc.perform(get("/api/v1/auth/test-hash"))
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
