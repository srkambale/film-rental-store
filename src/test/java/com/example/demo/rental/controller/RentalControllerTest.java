package com.example.demo.rental.controller;

import com.example.demo.rental.entity.Rental;
import com.example.demo.rental.service.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RentalControllerTest {

    @Mock
    private RentalService rentalService;

    @InjectMocks
    private RentalController rentalController;

    private Rental rental;

    @BeforeEach
    void setUp() {
        rental = new Rental();
        rental.setRentalId(1);
        rental.setRentalDate(LocalDateTime.now());
        rental.setStaffId(1);
    }

    @Test
    void testCreateRental() {
        when(rentalService.createRental(1, 1, 1)).thenReturn(rental);

        Rental result = rentalController.createRental(1, 1, 1);

        assertNotNull(result);
        assertEquals(1, result.getRentalId());
        verify(rentalService, times(1)).createRental(1, 1, 1);
    }

    @Test
    void testGetAllRentals() {
        List<Rental> rentals = Arrays.asList(rental);
        when(rentalService.getAllRentals()).thenReturn(rentals);

        List<Rental> result = rentalController.getAllRentals();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(rentalService, times(1)).getAllRentals();
    }

    @Test
    void testGetRentalById() {
        when(rentalService.getRentalById(1)).thenReturn(rental);

        Rental result = rentalController.getRentalById(1);

        assertNotNull(result);
        assertEquals(1, result.getRentalId());
        verify(rentalService, times(1)).getRentalById(1);
    }

    @Test
    void testReturnMovie() {
        rental.setReturnDate(LocalDateTime.now());
        when(rentalService.returnMovie(1)).thenReturn(rental);

        Rental result = rentalController.returnMovie(1);

        assertNotNull(result);
        assertNotNull(result.getReturnDate());
        verify(rentalService, times(1)).returnMovie(1);
    }

    @Test
    void testDeleteRental() {
        doNothing().when(rentalService).deleteRental(1);

        String result = rentalController.deleteRental(1);

        assertEquals("Rental deleted successfully", result);
        verify(rentalService, times(1)).deleteRental(1);
    }
}
