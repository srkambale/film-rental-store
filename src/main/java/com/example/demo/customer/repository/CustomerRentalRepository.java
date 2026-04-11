package com.example.demo.customer.repository;

import com.example.demo.customer.model.CustomerRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRentalRepository extends JpaRepository<CustomerRental, Long> {

    @Query("SELECT r FROM CustomerRental r WHERE r.customer.customerId = :customerId")
    List<CustomerRental> findByCustomerId(Long customerId);
}
