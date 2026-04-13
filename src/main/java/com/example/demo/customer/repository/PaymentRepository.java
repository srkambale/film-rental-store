package com.example.demo.customer.repository;

import com.example.demo.customer.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByCustomerId(Long customerId);

    @Query("SELECT p FROM Payment p WHERE p.customerId = :customerId AND p.paymentId = :paymentId")
    java.util.Optional<Payment> findByCustomerIdAndPaymentId(@org.springframework.data.repository.query.Param("customerId") Long customerId, @org.springframework.data.repository.query.Param("paymentId") Long paymentId);
}
