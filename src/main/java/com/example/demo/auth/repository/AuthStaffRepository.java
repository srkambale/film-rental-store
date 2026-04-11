package com.example.demo.auth.repository;



import com.example.demo.auth.entity.AuthStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthStaffRepository extends JpaRepository<AuthStaff, Integer> {
    Optional<AuthStaff> findByUsername(String username);
    boolean existsByUsername(String username);
}
