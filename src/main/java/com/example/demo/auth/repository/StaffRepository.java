package com.example.demo.auth.repository;



import com.example.demo.auth.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Optional<Staff> findByUsername(String username);
    boolean existsByUsername(String username);
}
