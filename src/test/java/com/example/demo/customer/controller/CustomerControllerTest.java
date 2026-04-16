package com.example.demo.customer.controller;

import com.example.demo.customer.dto.*;
import com.example.demo.customer.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        customerDto = new CustomerDto();
        customerDto.setCustomerId(1);
        customerDto.setFirstName("Jane");
        customerDto.setLastName("Doe");
    }

    @Test
    void testGetCustomer() {
        when(customerService.getCustomerById(1)).thenReturn(customerDto);

        CustomerDto result = customerController.getCustomer(1);

        assertNotNull(result);
        assertEquals(1, result.getCustomerId());
        verify(customerService, times(1)).getCustomerById(1);
    }

    @Test
    void testCreateCustomer() {
        CustomerCreateDto createDto = new CustomerCreateDto();
        when(customerService.createCustomer(any(CustomerCreateDto.class))).thenReturn(customerDto);

        CustomerDto result = customerController.createCustomer(createDto);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        verify(customerService, times(1)).createCustomer(any(CustomerCreateDto.class));
    }

    @Test
    void testUpdateCustomer() {
        CustomerUpdateDto updateDto = new CustomerUpdateDto();
        when(customerService.updateCustomer(eq(1), any(CustomerUpdateDto.class))).thenReturn(customerDto);

        CustomerDto result = customerController.updateCustomer(1, updateDto);

        assertNotNull(result);
        verify(customerService, times(1)).updateCustomer(1, updateDto);
    }

    @Test
    void testGetRentals() {
        List<RentalResponseDto> rentals = Arrays.asList(new RentalResponseDto());
        when(customerService.getCustomerRentals(1)).thenReturn(rentals);

        List<RentalResponseDto> result = customerController.getRentals(1);

        assertEquals(1, result.size());
        verify(customerService, times(1)).getCustomerRentals(1);
    }

    @Test
    void testGetPayments() {
        List<PaymentDto> payments = Arrays.asList(new PaymentDto());
        when(customerService.getCustomerPayments(1)).thenReturn(payments);

        List<PaymentDto> result = customerController.getPayments(1);

        assertEquals(1, result.size());
        verify(customerService, times(1)).getCustomerPayments(1);
    }

    @Test
    void testGetCustomersByName() {
        List<CustomerDto> list = Arrays.asList(customerDto);
        when(customerService.getCustomersByName("Jane")).thenReturn(list);

        List<CustomerDto> result = customerController.getCustomersByName("Jane");

        assertEquals(1, result.size());
        verify(customerService, times(1)).getCustomersByName("Jane");
    }
}
