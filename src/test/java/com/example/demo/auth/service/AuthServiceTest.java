package com.example.demo.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.auth.entity.AuthCustomer;
import com.example.demo.auth.entity.AuthStaff;
import com.example.demo.auth.entity.UserAuth;
import com.example.demo.auth.repository.AuthCustomerRepository;
import com.example.demo.auth.repository.AuthStaffRepository;
import com.example.demo.auth.repository.UserAuthRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserAuthRepository userAuthRepository;

    @Mock
    private AuthCustomerRepository authCustomerRepository;

    @Mock
    private AuthStaffRepository authStaffRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserDetails userDetails;

    // =========================
    // ✅ LOGIN TEST
    // =========================

    @Test
    void testLogin_Success_WithUsername() {

        LoginRequest request = new LoginRequest();
        request.setIdentifier("mayank");
        request.setPassword("1234");

        UserAuth userAuth = new UserAuth();
        userAuth.setUsername("mayank");
        userAuth.setRole(UserAuth.Role.CUSTOMER);

        when(userDetailsService.loadUserByUsername("mayank"))
                .thenReturn(userDetails);

        when(userAuthRepository.findByUsername("mayank"))
                .thenReturn(Optional.of(userAuth));

        when(jwtService.generateToken(userDetails, "CUSTOMER"))
                .thenReturn("token123");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("token123", response.getToken());
        assertEquals("CUSTOMER", response.getRole());
        assertEquals("mayank", response.getIdentifier());

        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void testLogin_UserFoundByEmail() {

        LoginRequest request = new LoginRequest();
        request.setIdentifier("test@mail.com");
        request.setPassword("1234");

        UserAuth userAuth = new UserAuth();
        userAuth.setEmail("test@mail.com");
        userAuth.setRole(UserAuth.Role.STAFF);

        when(userDetailsService.loadUserByUsername("test@mail.com"))
                .thenReturn(userDetails);

        when(userAuthRepository.findByUsername("test@mail.com"))
                .thenReturn(Optional.empty());

        when(userAuthRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(userAuth));

        when(jwtService.generateToken(userDetails, "STAFF"))
                .thenReturn("emailToken");

        AuthResponse response = authService.login(request);

        assertEquals("emailToken", response.getToken());
        assertEquals("STAFF", response.getRole());
    }

    @Test
    void testLogin_UserNotFound() {

        LoginRequest request = new LoginRequest();
        request.setIdentifier("unknown");
        request.setPassword("1234");

        when(userDetailsService.loadUserByUsername("unknown"))
                .thenReturn(userDetails);

        when(userAuthRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        when(userAuthRepository.findByEmail("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> authService.login(request));
    }

    // =========================
    // ✅ REGISTER CUSTOMER
    // =========================

    @Test
    void testRegisterCustomer_Success() {

        RegisterRequest request = new RegisterRequest();
        request.setRole("CUSTOMER");
        request.setFirstName("Mayank");
        request.setLastName("Agrawal");
        request.setEmail("mayank@mail.com");
        request.setPassword("1234");

        when(userAuthRepository.existsByEmail("mayank@mail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("1234"))
                .thenReturn("encoded");

        AuthCustomer savedCustomer = new AuthCustomer();
        savedCustomer.setCustomerId(1);
        when(authCustomerRepository.save(any()))
                .thenReturn(savedCustomer);

        when(userDetailsService.loadUserByUsername("mayank@mail.com"))
                .thenReturn(userDetails);

        when(jwtService.generateToken(userDetails, "CUSTOMER"))
                .thenReturn("custToken");

        AuthResponse response = authService.register(request);

        assertEquals("custToken", response.getToken());
        assertEquals("CUSTOMER", response.getRole());

        verify(authCustomerRepository).save(any());
        verify(userAuthRepository).save(any());
    }

    @Test
    void testRegisterCustomer_EmailExists() {

        RegisterRequest request = new RegisterRequest();
        request.setRole("CUSTOMER");
        request.setEmail("test@mail.com");

        when(userAuthRepository.existsByEmail("test@mail.com"))
                .thenReturn(true);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    // =========================
    // ✅ REGISTER STAFF
    // =========================

    @Test
    void testRegisterStaff_Success() {

        RegisterRequest request = new RegisterRequest();
        request.setRole("STAFF");
        request.setUsername("staff1");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("staff@mail.com");
        request.setPassword("1234");
        request.setStoreId((short) 1);

        when(userAuthRepository.existsByUsername("staff1"))
                .thenReturn(false);

        when(passwordEncoder.encode("1234"))
                .thenReturn("encoded");

        AuthStaff savedStaff = new AuthStaff();
        savedStaff.setStaffId(10);
        when(authStaffRepository.save(any()))
                .thenReturn(savedStaff);

        when(userDetailsService.loadUserByUsername("staff1"))
                .thenReturn(userDetails);

        when(jwtService.generateToken(userDetails, "STAFF"))
                .thenReturn("staffToken");

        AuthResponse response = authService.register(request);

        assertEquals("staffToken", response.getToken());
        assertEquals("STAFF", response.getRole());

        verify(authStaffRepository).save(any());
        verify(userAuthRepository).save(any());
    }

    @Test
    void testRegisterStaff_UsernameMissing() {

        RegisterRequest request = new RegisterRequest();
        request.setRole("STAFF");
        request.setUsername(null);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @Test
    void testRegisterStaff_UsernameExists() {

        RegisterRequest request = new RegisterRequest();
        request.setRole("STAFF");
        request.setUsername("staff1");
        request.setStoreId((short) 1);

        when(userAuthRepository.existsByUsername("staff1"))
                .thenReturn(true);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    @Test
    void testRegisterStaff_StoreIdMissing() {

        RegisterRequest request = new RegisterRequest();
        request.setRole("STAFF");
        request.setUsername("staff1");

        when(userAuthRepository.existsByUsername("staff1"))
                .thenReturn(false);

        assertThrows(Exception.class, () -> authService.register(request));
    }

    // =========================
    // ❌ INVALID ROLE
    // =========================

    @Test
    void testRegister_InvalidRole() {

        RegisterRequest request = new RegisterRequest();
        request.setRole("INVALID");

        assertThrows(Exception.class, () -> authService.register(request));
    }
}