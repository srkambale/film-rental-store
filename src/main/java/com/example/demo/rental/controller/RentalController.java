package com.example.demo.rental.controller;

import com.example.demo.rental.entity.Rental;
import com.example.demo.rental.service.RentalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rentals")
public class RentalController {

    private final RentalService service;

    public RentalController(RentalService service) {
        this.service = service;
    }

// 1. Fix: RentalController - @RequestParam Customer is invalid, use Integer
    @PostMapping
    public Rental createRental(
            @RequestParam Integer inventoryId,
            @RequestParam Integer customerId,
            @RequestParam Integer staffId) {

        return service.createRental(inventoryId, customerId, staffId);
    }

    // ✅ GET ALL
    @GetMapping
    public List<Rental> getAllRentals() {
        return service.getAllRentals();
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public Rental getRentalById(@PathVariable Integer id) {
        return service.getRentalById(id);
    }

    // ✅ RETURN MOVIE
    @PutMapping("/{id}/return")
    public Rental returnMovie(@PathVariable Integer id) {
        return service.returnMovie(id);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public String deleteRental(@PathVariable Integer id) {
        service.deleteRental(id);
        return "Rental deleted successfully";
    }
}