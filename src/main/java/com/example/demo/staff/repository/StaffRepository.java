package com.example.demo.staff.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.staff.entity.Staff;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Integer> {

    Optional<Staff> findByUsername(String username);
}
