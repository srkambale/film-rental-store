package com.example.demo.auth.service;



import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() throws Exception {

        jwtService = new JwtService();

        // ✅ Manually set @Value fields
        setField(jwtService, "jwtSecret", "mysecretkeymysecretkeymysecretkey12345");
        setField(jwtService, "jwtExpirationMs", 1000 * 60 * 60); // 1 hour

        userDetails = User.builder()
                .username("mayank")
                .password("password")
                .roles("CUSTOMER")
                .build();
    }

    // 🔧 Utility method to inject private fields
    private void setField(Object target, String fieldName, Object value)
            throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    // =========================
    // ✅ GENERATE TOKEN
    // =========================
    @Test
    void testGenerateToken() {

        String token = jwtService.generateToken(userDetails, "CUSTOMER");

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    // =========================
    // ✅ EXTRACT USERNAME
    // =========================
    @Test
    void testExtractUsername() {

        String token = jwtService.generateToken(userDetails, "CUSTOMER");

        String username = jwtService.extractUsername(token);

        assertEquals("mayank", username);
    }

    // =========================
    // ✅ EXTRACT ROLE
    // =========================
    @Test
    void testExtractRole() {

        String token = jwtService.generateToken(userDetails, "CUSTOMER");

        String role = jwtService.extractRole(token);

        assertEquals("CUSTOMER", role);
    }

    // =========================
    // ✅ TOKEN VALIDATION
    // =========================
    @Test
    void testIsTokenValid_True() {

        String token = jwtService.generateToken(userDetails, "CUSTOMER");

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    // =========================
    // ❌ INVALID USER
    // =========================
    @Test
    void testIsTokenValid_False_WrongUser() {

        String token = jwtService.generateToken(userDetails, "CUSTOMER");

        UserDetails otherUser = User.builder()
                .username("other")
                .password("pass")
                .roles("CUSTOMER")
                .build();

        boolean isValid = jwtService.isTokenValid(token, otherUser);

        assertFalse(isValid);
    }

    // =========================
    // ⏱️ TOKEN EXPIRY TEST
    // =========================
    @Test
    void testTokenExpired() throws Exception {

        setField(jwtService, "jwtExpirationMs", 1);

        String token = jwtService.generateToken(userDetails, "CUSTOMER");

        Thread.sleep(5);

        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
            jwtService.isTokenValid(token, userDetails);
        });
    }
}