package com.example.demo.auth.repository;



import com.example.demo.auth.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
}