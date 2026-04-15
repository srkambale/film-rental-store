package com.example.demo.admin.repository;

import com.example.demo.staff.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminStaffRepository extends JpaRepository<Staff, Integer> {
    
    List<Staff> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}
