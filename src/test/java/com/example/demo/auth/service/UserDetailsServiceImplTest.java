package com.example.demo.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.example.demo.auth.entity.UserAuth;
import com.example.demo.auth.repository.UserAuthRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserAuthRepository userAuthRepository;

    private UserAuth userAuth;

    @BeforeEach
    void setUp() {
        userAuth = new UserAuth();
        userAuth.setUsername("mayank");
        userAuth.setEmail("mayank@mail.com");
        userAuth.setPassword("encodedPassword");
        userAuth.setRole(UserAuth.Role.CUSTOMER);

        // ❗ IMPORTANT: mock loginIdentifier behavior
        // Assuming this method returns email or username internally
        // If it's a method, we cannot mock it directly → so ensure it returns email
    }

    // =========================
    // ✅ SUCCESS (USERNAME)
    // =========================
    @Test
    void testLoadUserByUsername_Success_Username() {

        when(userAuthRepository.findByUsername("mayank"))
                .thenReturn(Optional.of(userAuth));

        // 👇 IMPORTANT: define expected identifier
        // If loginIdentifier = username
        // adjust if needed
        String expectedIdentifier = userAuth.getLoginIdentifier();

        UserDetails result =
                userDetailsService.loadUserByUsername("mayank");

        assertNotNull(result);
        assertEquals(expectedIdentifier, result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertTrue(result.getAuthorities().size() > 0);
    }

    // =========================
    // ✅ SUCCESS (EMAIL)
    // =========================
    @Test
    void testLoadUserByUsername_Success_Email() {

        when(userAuthRepository.findByUsername("mayank@mail.com"))
                .thenReturn(Optional.empty());

        when(userAuthRepository.findByEmail("mayank@mail.com"))
                .thenReturn(Optional.of(userAuth));

        String expectedIdentifier = userAuth.getLoginIdentifier();

        UserDetails result =
                userDetailsService.loadUserByUsername("mayank@mail.com");

        assertNotNull(result);
        assertEquals(expectedIdentifier, result.getUsername());
    }

    // =========================
    // ❌ USER NOT FOUND
    // =========================
    @Test
    void testLoadUserByUsername_NotFound() {

        when(userAuthRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        when(userAuthRepository.findByEmail("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("unknown");
        });
    }

    // =========================
    // ⚠️ ROLE CHECK
    // =========================
    @Test
    void testLoadUserByUsername_RoleMapping() {

        userAuth.setRole(UserAuth.Role.ADMIN);

        when(userAuthRepository.findByUsername("mayank"))
                .thenReturn(Optional.of(userAuth));

        UserDetails result =
                userDetailsService.loadUserByUsername("mayank");

        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }
}