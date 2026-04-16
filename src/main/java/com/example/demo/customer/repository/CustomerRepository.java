package com.example.demo.customer.repository;

import com.example.demo.customer.model.Customer;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	@Query(value = "SELECT get_customer_balance(:customerId, NOW())", nativeQuery = true)
	BigDecimal getCustomerBalance(@Param("customerId") Integer customerId);

    List<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.address.city.city) LIKE LOWER(CONCAT('%', :location, '%')) OR LOWER(c.address.district) LIKE LOWER(CONCAT('%', :location, '%')) OR LOWER(c.address.city.country.country) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<Customer> findByLocationIgnoreCase(@org.springframework.data.repository.query.Param("location") String location);
}
