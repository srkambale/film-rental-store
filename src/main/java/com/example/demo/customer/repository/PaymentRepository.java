package com.example.demo.customer.repository;

import com.example.demo.customer.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	 @Query("SELECT p FROM Payment p WHERE p.customer.customerId = :customerId")
	    List<Payment> findByCustomerId(Long customerId);
}
