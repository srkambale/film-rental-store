package com.example.demo.auth.repository;



import com.example.demo.auth.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    Optional<UserAuth> findByStaff_Username(String username);
    Optional<UserAuth> findByCustomer_Email(String email);
}