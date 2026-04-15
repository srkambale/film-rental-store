package com.example.demo.customer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.example.demo.customer.dto.RentalRequestDto;
import com.example.demo.customer.model.*;
import com.example.demo.customer.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class CustomerRentalServiceImplTest {

    @InjectMocks
    private CustomerRentalServiceImpl rentalService;

    @Mock
    private CustomerRentalRepository rentalRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerInventoryRepository inventoryRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ TEST: createRental SUCCESS
    @Test
    void testCreateRental_success() {
        RentalRequestDto dto = new RentalRequestDto();
        dto.setCustomerId(1);
        dto.setInventoryId(2);

        Customer customer = new Customer();
        customer.setCustomerId(1);

        CustomerInventory inventory = new CustomerInventory();
        inventory.setInventoryId(2);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(inventoryRepository.findById(2)).thenReturn(Optional.of(inventory));
        when(rentalRepository.countActiveRentalsByInventoryId(2)).thenReturn(0L);

        when(rentalRepository.save(any(CustomerRental.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertNotNull(rentalService.createRental(dto));
    }

    // ❌ TEST: Film already rented
    @Test
    void testCreateRental_filmNotAvailable() {
        RentalRequestDto dto = new RentalRequestDto();
        dto.setCustomerId(1);
        dto.setInventoryId(2);

        when(customerRepository.findById(1)).thenReturn(Optional.of(new Customer()));
        when(inventoryRepository.findById(2)).thenReturn(Optional.of(new CustomerInventory()));
        when(rentalRepository.countActiveRentalsByInventoryId(2)).thenReturn(1L);

        assertThrows(RuntimeException.class, () -> {
            rentalService.createRental(dto);
        });
    }

    // ✅ TEST: returnFilm
    @Test
    void testReturnFilm_success() {
        CustomerRental rental = new CustomerRental();
        rental.setRentalId(1);

        when(rentalRepository.findById(1))
                .thenReturn(Optional.of(rental));

        when(rentalRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertNotNull(rentalService.returnFilm(1));
    }
}