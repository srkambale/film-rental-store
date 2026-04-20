package com.example.demo.customer.repository;

import com.example.demo.customer.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByCustomerId(Integer customerId);

    @Query("SELECT p FROM Payment p WHERE p.customerId = :customerId AND p.paymentId = :paymentId")
    java.util.Optional<Payment> findByCustomerIdAndPaymentId(@org.springframework.data.repository.query.Param("customerId") Integer customerId, @org.springframework.data.repository.query.Param("paymentId") Integer paymentId);
}
