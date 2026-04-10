package com.example.demo.auth.service;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.auth.entity.Customer;
import com.example.demo.auth.entity.Staff;
import com.example.demo.auth.entity.UserAuth;
import com.example.demo.auth.repository.CustomerRepository;
import com.example.demo.auth.repository.StaffRepository;
import com.example.demo.auth.repository.UserAuthRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserAuthRepository    userAuthRepository;
    private final CustomerRepository    customerRepository;
    private final StaffRepository       staffRepository;
    private final PasswordEncoder       passwordEncoder;
    private final JwtService            jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthService(UserAuthRepository userAuthRepository,
                       CustomerRepository customerRepository,
                       StaffRepository staffRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       UserDetailsServiceImpl userDetailsService) {
        this.userAuthRepository  = userAuthRepository;
        this.customerRepository  = customerRepository;
        this.staffRepository     = staffRepository;
        this.passwordEncoder     = passwordEncoder;
        this.jwtService          = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService  = userDetailsService;
    }

    // ── LOGIN ──────────────────────────────────────────────────
    public AuthResponse login(LoginRequest request) {

        // Authenticate — Spring Security checks password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getIdentifier(),
                        request.getPassword()
                )
        );

        // Credentials valid — load user
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getIdentifier());

        // Get role from user_auth
        UserAuth userAuth = userAuthRepository
                .findByUsername(request.getIdentifier())
                .orElseGet(() ->
                        userAuthRepository
                                .findByEmail(request.getIdentifier())
                                .orElseThrow(() ->
                                        new RuntimeException("User not found"))
                );

        String role  = userAuth.getRole().name();
        String token = jwtService.generateToken(userDetails, role);

        return new AuthResponse(token, role, request.getIdentifier());
    }

    // ── REGISTER ───────────────────────────────────────────────
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        UserAuth.Role role =
                UserAuth.Role.valueOf(request.getRole().toUpperCase());

        if (role == UserAuth.Role.CUSTOMER) {
            return registerCustomer(request);
        } else {
            return registerStaff(request, role);
        }
    }

    // ── REGISTER CUSTOMER ──────────────────────────────────────
    private AuthResponse registerCustomer(RegisterRequest request) {

        if (userAuthRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Email already registered: " + request.getEmail());
        }

        // 1. Save to customer table
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setStoreId((short) 1);
        customer.setAddressId((short) 1);
        customer.setActive(true);
        customer.setCreateDate(LocalDateTime.now());
        Customer saved = customerRepository.save(customer);

        // 2. Save to user_auth table
        // Username = firstname.lastname for customers
        String username = request.getFirstName().toLowerCase()
                        + "." + request.getLastName().toLowerCase();

        UserAuth userAuth = new UserAuth();
        userAuth.setUsername(username);
        userAuth.setEmail(request.getEmail());
        userAuth.setPassword(passwordEncoder.encode(request.getPassword()));
        userAuth.setRole(UserAuth.Role.CUSTOMER);
        userAuth.setRefId(saved.getCustomerId());
        userAuthRepository.save(userAuth);

        // 3. Generate JWT
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails, "CUSTOMER");

        return new AuthResponse(token, "CUSTOMER", request.getEmail());
    }

    // ── REGISTER STAFF / ADMIN ─────────────────────────────────
    private AuthResponse registerStaff(RegisterRequest request,
                                       UserAuth.Role role) {

        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new RuntimeException(
                    "Username is required for STAFF/ADMIN");
        }

        if (userAuthRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException(
                    "Username already taken: " + request.getUsername());
        }

        if (request.getStoreId() == null) {
            throw new RuntimeException(
                    "storeId is required for STAFF/ADMIN");
        }

        // 1. Save to staff table
        Staff staff = new Staff();
        staff.setFirstName(request.getFirstName());
        staff.setLastName(request.getLastName());
        staff.setEmail(request.getEmail());
        staff.setUsername(request.getUsername());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setStoreId(request.getStoreId());
        staff.setAddressId((short) 1);
        staff.setActive(true);
        Staff saved = staffRepository.save(staff);

        // 2. Save to user_auth table
        UserAuth userAuth = new UserAuth();
        userAuth.setUsername(request.getUsername());
        userAuth.setEmail(request.getEmail());
        userAuth.setPassword(passwordEncoder.encode(request.getPassword()));
        userAuth.setRole(role);
        userAuth.setRefId(saved.getStaffId());
        userAuthRepository.save(userAuth);

        // 3. Generate JWT
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails, role.name());

        return new AuthResponse(token, role.name(), request.getUsername());
    }
}