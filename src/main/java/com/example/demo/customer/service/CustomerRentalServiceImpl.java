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

        long activeRentals = rentalRepository.countActiveRentalsByInventoryId(inventory.getInventoryId());
        if (activeRentals > 0) {
            throw new com.example.demo.customer.exception.CustomerBadRequestException("Film is currently not available in the store as it is already rented out.");
        }

        CustomerRental rental = new CustomerRental();
        rental.setCustomer(customer);
        rental.setInventory(inventory);
        rental.setRentalDate(LocalDateTime.now());
        rental.setStaffId(1); // default staff, update as needed
        rental.setLastUpdate(LocalDateTime.now());

        CustomerRental saved = rentalRepository.save(rental);

        return mapToDto(saved);
    }

    @Override
    public List<RentalResponseDto> getRentalsByCustomer(Integer customerId) {

        List<CustomerRental> rentals = rentalRepository.findByCustomerId(customerId);

        return rentals.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RentalResponseDto returnFilm(Integer rentalId) {
        CustomerRental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new CustomerResourceNotFoundException("Rental not found"));

        if (rental.getReturnDate() != null) {
            throw new com.example.demo.customer.exception.CustomerBadRequestException("Film is already returned.");
        }

        rental.setReturnDate(LocalDateTime.now());
        rental.setLastUpdate(LocalDateTime.now());
        CustomerRental updated = rentalRepository.save(rental);

        return mapToDto(updated);
    }

    private RentalResponseDto mapToDto(CustomerRental r) {

        RentalResponseDto dto = new RentalResponseDto();

        dto.setRentalId(r.getRentalId());
        dto.setRentalDate(r.getRentalDate());

        if (r.getReturnDate() == null) {
            dto.setStatus("RENTED");
            if (r.getInventory() != null && r.getInventory().getFilm() != null && r.getInventory().getFilm().getRentalDuration() != null) {
                LocalDateTime expectedReturn = r.getRentalDate().plusDays(r.getInventory().getFilm().getRentalDuration());
                dto.setReturnDate(expectedReturn);
                dto.setIsOverdue(LocalDateTime.now().isAfter(expectedReturn));
            } else {
                dto.setIsOverdue(false);
            }
        } else {
            dto.setStatus("RETURNED");
            dto.setReturnDate(r.getReturnDate());
            dto.setIsOverdue(false);
        }

        if (r.getInventory() != null && r.getInventory().getFilm() != null) {
            dto.setFilmTitle(r.getInventory().getFilm().getTitle());
        }

        return dto;
    }
}
