package com.example.demo.auth.repository;



import com.example.demo.auth.entity.AuthCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthCustomerRepository extends JpaRepository<AuthCustomer, Integer> {
    Optional<AuthCustomer> findByEmail(String email);
    boolean existsByEmail(String email);
}
