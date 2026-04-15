package com.example.demo.rental.service;

import com.example.demo.customer.model.Customer;
import com.example.demo.customer.repository.CustomerRepository;
import com.example.demo.rental.entity.Inventory;
import com.example.demo.rental.entity.Rental;
import com.example.demo.rental.repository.InventoryRepository;
import com.example.demo.rental.repository.RentalRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RentalService rentalService;

    @Test
    void testCreateRental() {
        Inventory inventory = new Inventory();
        inventory.setInventoryId(1);

        Customer customer = new Customer();
        customer.setCustomerId((int) 1L);

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(rentalRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Rental result = rentalService.createRental(1, 1, 100);

        assertNotNull(result);
        assertEquals(100, result.getStaffId());
        assertNotNull(result.getRentalDate());
    }

    @Test
    void testGetRentalById() {
        Rental rental = new Rental();
        rental.setRentalId(1);

        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));

        Rental result = rentalService.getRentalById(1);

        assertEquals(1, result.getRentalId());
    }

    @Test
    void testReturnMovie() {
        Rental rental = new Rental();
        rental.setRentalId(1);

        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any())).thenReturn(rental);

        Rental result = rentalService.returnMovie(1);

        assertNotNull(result.getReturnDate());
    }
}