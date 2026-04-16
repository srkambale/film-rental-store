package com.example.demo.rental.service;

import com.example.demo.customer.model.Customer;
import com.example.demo.customer.repository.CustomerRepository;
import com.example.demo.rental.entity.Inventory;
import com.example.demo.rental.entity.Rental;
import com.example.demo.rental.repository.InventoryRepository;
import com.example.demo.rental.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final InventoryRepository inventoryRepository;
    private final CustomerRepository customerRepository;

    public RentalService(RentalRepository rentalRepository,
                         InventoryRepository inventoryRepository,
                         CustomerRepository customerRepository) {
        this.rentalRepository = rentalRepository;
        this.inventoryRepository = inventoryRepository;
        this.customerRepository = customerRepository;
    }

    // ✅ CREATE RENTAL
    public Rental createRental(Integer inventoryId, Integer customerId, Integer staffId) {

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("Inventory not found"));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("Customer not found"));

        Rental rental = new Rental();
        rental.setInventory(inventory);
        rental.setCustomer(customer);
        rental.setStaffId(staffId);
        rental.setRentalDate(LocalDateTime.now());

        return rentalRepository.save(rental);
    }

    // ✅ GET ALL
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    // ✅ GET BY ID
    public Rental getRentalById(Integer id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("Rental not found"));
    }

    // ✅ RETURN MOVIE
    public Rental returnMovie(Integer rentalId) {
        Rental rental = getRentalById(rentalId);
        rental.setReturnDate(LocalDateTime.now());
        return rentalRepository.save(rental);
    }

    // ✅ DELETE
    public void deleteRental(Integer id) {
        rentalRepository.deleteById(id);
    }
}