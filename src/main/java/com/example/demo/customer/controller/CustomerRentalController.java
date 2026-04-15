package com.example.demo.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.customer.dto.RentalRequestDto;
import com.example.demo.customer.dto.RentalResponseDto;
import com.example.demo.customer.service.CustomerRentalService;

@RestController
@RequestMapping("/api/v1/customer/rentals")
public class CustomerRentalController {

    private CustomerRentalService rentalService;

    @Autowired
    public CustomerRentalController(CustomerRentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    public RentalResponseDto createRental(@RequestBody RentalRequestDto request) {
        return rentalService.createRental(request);
    }

    @PutMapping("/{rentalId}/return")
    public RentalResponseDto returnFilm(@PathVariable Long rentalId) {
        return rentalService.returnFilm(rentalId);
    }
}
