package com.example.demo.rental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.rental.entity.Rental;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {

    List<Rental> findByCustomerId(Integer customerId);

    List<Rental> findByStaffId(Integer staffId);

    List<Rental> findByInventoryId(Integer inventoryId);

    // Active rentals = no return date
    List<Rental> findByReturnDateIsNull();

    // Returned rentals
    List<Rental> findByReturnDateIsNotNull();

    // Active rentals by customer
    List<Rental> findByCustomerIdAndReturnDateIsNull(Integer customerId);

    // Overdue rentals: rental_date older than given date and not yet returned
    @Query("SELECT r FROM Rental r WHERE r.returnDate IS NULL AND r.rentalDate < :cutoff")
    List<Rental> findOverdueRentals(@Param("cutoff") LocalDateTime cutoff);

    // Check if inventory item is currently rented out
    boolean existsByInventoryIdAndReturnDateIsNull(Integer inventoryId);
}
