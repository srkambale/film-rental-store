package com.example.demo.auth.service;

import com.example.demo.auth.entity.UserAuth;
import com.example.demo.auth.repository.UserAuthRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAuthRepository userAuthRepository;

    public UserDetailsServiceImpl(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier)
            throws UsernameNotFoundException {

        // Try username first (staff/admin)
        UserAuth userAuth = userAuthRepository.findByUsername(identifier)
                .orElseGet(() ->
                    // Then try email (customer)
                    userAuthRepository.findByEmail(identifier)
                            .orElseThrow(() ->
                                new UsernameNotFoundException(
                                    "User not found: " + identifier)
                            )
                );

        String role = "ROLE_" + userAuth.getRole().name();

        return new User(
                userAuth.getLoginIdentifier(),
                userAuth.getPassword(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}