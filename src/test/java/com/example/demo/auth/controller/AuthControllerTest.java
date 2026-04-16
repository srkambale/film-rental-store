package com.example.demo.auth.controller;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        authResponse = new AuthResponse();
        authResponse.setToken("mock-jwt-token");
        authResponse.setIdentifier("testuser");
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest();
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mock-jwt-token", response.getBody().getToken());
        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest();
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    void testTestHash() {
        ResponseEntity<String> response = authController.testHash();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("FreshHash"));
    }

    @Test
    void testGetCurrentUser() {
        UserDetails userDetails = new User("testuser", "password", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        ResponseEntity<?> response = authController.getCurrentUser(userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("testuser"));
    }
}
