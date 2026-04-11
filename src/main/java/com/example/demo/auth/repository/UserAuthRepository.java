package com.example.demo.auth.repository;

import com.example.demo.auth.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

    // Staff/Admin login by username
    Optional<UserAuth> findByUsername(String username);

    // Customer login by email
    Optional<UserAuth> findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
