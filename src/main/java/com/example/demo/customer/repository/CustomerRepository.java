package com.example.demo.customer.repository;

import com.example.demo.customer.model.Customer;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	@Query(value = "SELECT get_customer_balance(:customerId, NOW())", nativeQuery = true)
	BigDecimal getCustomerBalance(@Param("customerId") Long customerId);
}
