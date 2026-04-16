package com.example.demo.customer.controller;

import com.example.demo.customer.dto.RentalRequestDto;
import com.example.demo.customer.dto.RentalResponseDto;
import com.example.demo.customer.service.CustomerRentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerRentalControllerTest {

    @Mock
    private CustomerRentalService rentalService;

    @InjectMocks
    private CustomerRentalController customerRentalController;

    private RentalResponseDto responseDto;

    @BeforeEach
    void setUp() {
        responseDto = new RentalResponseDto();
        responseDto.setRentalId(1);
    }

    @Test
    void testCreateRental() {
        RentalRequestDto requestDto = new RentalRequestDto();
        when(rentalService.createRental(any(RentalRequestDto.class))).thenReturn(responseDto);

        RentalResponseDto result = customerRentalController.createRental(requestDto);

        assertNotNull(result);
        assertEquals(1, result.getRentalId());
        verify(rentalService, times(1)).createRental(any(RentalRequestDto.class));
    }

    @Test
    void testReturnFilm() {
        when(rentalService.returnFilm(1)).thenReturn(responseDto);

        RentalResponseDto result = customerRentalController.returnFilm(1);

        assertNotNull(result);
        assertEquals(1, result.getRentalId());
        verify(rentalService, times(1)).returnFilm(1);
    }
}
