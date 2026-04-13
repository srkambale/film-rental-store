package com.example.demo.customer.repository;

import com.example.demo.customer.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import java.util.List;
 
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
 
    // Used by PaymentService — get all payments for a customer (Integer)
    List<Payment> findByCustomer_CustomerId(Integer customerId);
 
    // Used by Member C's CustomerServiceImpl — same query but Long customerId
    List<Payment> findByCustomer_CustomerId(Long customerId);
 
    // Used by PaymentService — get all payments linked to a rental
    List<Payment> findByRental_RentalId(Integer rentalId);
}