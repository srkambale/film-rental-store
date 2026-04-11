package com.example.demo.customer.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.customer.dto.RentalRequestDto;
import com.example.demo.customer.dto.RentalResponseDto;
import com.example.demo.customer.exception.CustomerResourceNotFoundException;
import com.example.demo.customer.model.Customer;
import com.example.demo.customer.model.CustomerInventory;
import com.example.demo.customer.model.CustomerRental;
import com.example.demo.customer.repository.CustomerRepository;
import com.example.demo.customer.repository.CustomerInventoryRepository;
import com.example.demo.customer.repository.CustomerRentalRepository;

@Service
public class CustomerRentalServiceImpl implements CustomerRentalService {

    private CustomerRentalRepository rentalRepository;
    private CustomerRepository customerRepository;
    private CustomerInventoryRepository inventoryRepository;

    @Autowired
    public CustomerRentalServiceImpl(CustomerRentalRepository rentalRepository,
                             CustomerRepository customerRepository,
                             CustomerInventoryRepository inventoryRepository) {
        this.rentalRepository = rentalRepository;
        this.customerRepository = customerRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public RentalResponseDto createRental(RentalRequestDto request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerResourceNotFoundException("Customer not found"));

        CustomerInventory inventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(() -> new CustomerResourceNotFoundException("Inventory not found"));

        CustomerRental rental = new CustomerRental();
        rental.setCustomer(customer);
        rental.setInventory(inventory);
        rental.setRentalDate(LocalDateTime.now());
        rental.setStaffId(1L); // default staff, update as needed
        rental.setLastUpdate(LocalDateTime.now());

        CustomerRental saved = rentalRepository.save(rental);

        return mapToDto(saved);
    }

    @Override
    public List<RentalResponseDto> getRentalsByCustomer(Long customerId) {

        List<CustomerRental> rentals = rentalRepository.findByCustomerId(customerId);

        return rentals.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private RentalResponseDto mapToDto(CustomerRental r) {

        RentalResponseDto dto = new RentalResponseDto();

        dto.setRentalId(r.getRentalId());
        dto.setRentalDate(r.getRentalDate());
        dto.setReturnDate(r.getReturnDate());

        if (r.getInventory() != null && r.getInventory().getFilm() != null) {
            dto.setFilmTitle(r.getInventory().getFilm().getTitle());
        }

        dto.setStatus(r.getReturnDate() == null ? "RENTED" : "RETURNED");

        return dto;
    }
}
