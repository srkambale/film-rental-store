package com.example.demo.rental.service;

import com.example.demo.rental.entity.Rental;
import com.example.demo.rental.repository.RentalRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OverdueServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private OverdueService overdueService;

    @Test
    void testGetOverdueRentals() {
        Rental rental = new Rental();
        rental.setRentalId(1);
        rental.setRentalDate(LocalDateTime.now().minusDays(10));

        when(rentalRepository.findOverdueRentals(any())).thenReturn(List.of(rental));

        var result = overdueService.getOverdueRentals();

        assertEquals(1, result.size());
    }

    @Test
    void testCountOverdueRentals() {
        when(rentalRepository.findOverdueRentals(any())).thenReturn(List.of(new Rental(), new Rental()));

        long count = overdueService.countOverdueRentals();

        assertEquals(2, count);
    }
}