package com.example.demo.auth.service;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.auth.entity.AuthCustomer;
import com.example.demo.auth.entity.AuthStaff;
import com.example.demo.auth.entity.UserAuth;
import com.example.demo.auth.repository.AuthCustomerRepository;
import com.example.demo.auth.repository.AuthStaffRepository;
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
    private final AuthCustomerRepository    AuthCustomerRepository;
    private final AuthStaffRepository       AuthStaffRepository;
    private final PasswordEncoder       passwordEncoder;
    private final JwtService            jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthService(UserAuthRepository userAuthRepository,
                       AuthCustomerRepository AuthCustomerRepository,
                       AuthStaffRepository AuthStaffRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       UserDetailsServiceImpl userDetailsService) {
        this.userAuthRepository  = userAuthRepository;
        this.AuthCustomerRepository  = AuthCustomerRepository;
        this.AuthStaffRepository     = AuthStaffRepository;
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

        // Match role case-insensitively against enum constants
        String roleInput = request.getRole();
        UserAuth.Role role = null;
        for (UserAuth.Role r : UserAuth.Role.values()) {
            if (r.name().equalsIgnoreCase(roleInput)) {
                role = r;
                break;
            }
        }
        if (role == null) {
            if ("authcustomer".equalsIgnoreCase(roleInput)) role = UserAuth.Role.CUSTOMER;
            else if ("authstaff".equalsIgnoreCase(roleInput)) role = UserAuth.Role.STAFF;
        }
        if (role == null) {
            throw new RuntimeException("Invalid role: " + roleInput + ". Valid values: ADMIN, STAFF, CUSTOMER");
        }

        if (role == UserAuth.Role.CUSTOMER) {
            return registerCustomer(request);
        } else {
            return registerStaff(request, role);
        }
    }

    // ── REGISTER AuthCustomer ──────────────────────────────────────
    private AuthResponse registerCustomer(RegisterRequest request) {

        if (userAuthRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Email already registered: " + request.getEmail());
        }

        // 1. Save to AuthCustomer table
        AuthCustomer AuthCustomer = new AuthCustomer();
        AuthCustomer.setFirstName(request.getFirstName());
        AuthCustomer.setLastName(request.getLastName());
        AuthCustomer.setEmail(request.getEmail());
        AuthCustomer.setStoreId((short) 1);
        AuthCustomer.setAddressId((short) 1);
        AuthCustomer.setActive(true);
        AuthCustomer.setCreateDate(LocalDateTime.now());
        AuthCustomer saved = AuthCustomerRepository.save(AuthCustomer);

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

    // ── REGISTER AuthStaff / ADMIN ─────────────────────────────────
    private AuthResponse registerStaff(RegisterRequest request,
                                       UserAuth.Role role) {

        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new RuntimeException(
                    "Username is required for AuthStaff/ADMIN");
        }

        if (userAuthRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException(
                    "Username already taken: " + request.getUsername());
        }

        if (request.getStoreId() == null) {
            throw new RuntimeException(
                    "storeId is required for AuthStaff/ADMIN");
        }

        // 1. Save to AuthStaff table
        AuthStaff AuthStaff = new AuthStaff();
        AuthStaff.setFirstName(request.getFirstName());
        AuthStaff.setLastName(request.getLastName());
        AuthStaff.setEmail(request.getEmail());
        AuthStaff.setUsername(request.getUsername());
        AuthStaff.setPassword(passwordEncoder.encode(request.getPassword()));
        AuthStaff.setStoreId(request.getStoreId());
        AuthStaff.setAddressId((short) 1);
        AuthStaff.setActive(true);
        AuthStaff saved = AuthStaffRepository.save(AuthStaff);

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
