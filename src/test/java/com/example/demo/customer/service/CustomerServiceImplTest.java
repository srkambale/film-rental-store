package com.example.demo.customer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.example.demo.customer.dto.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.customer.model.*;
import com.example.demo.customer.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerRentalRepository rentalRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AddressRepository addressRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ================= HELPER METHOD =================
    private Customer createValidCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john@test.com");
        customer.setActive(true);

        Address address = new Address();
        address.setAddressId(10L);
        address.setAddress("Street 1");
        address.setDistrict("District 1");
        address.setPostalCode("12345");
        address.setPhone("9999999999");

        City city = new City();
        city.setCity("Pune");

        Country country = new Country();
        country.setCountry("India");

        city.setCountry(country);
        address.setCity(city);

        customer.setAddress(address);

        return customer;
    }

    // ================= TESTS =================

    // ✅ SUCCESS CASE
    @Test
    void testGetCustomerById_success() {
        Customer customer = createValidCustomer();

        when(customerRepository.findById(1))
                .thenReturn(Optional.of(customer));

        CustomerDto result = customerService.getCustomerById(1);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Pune", result.getAddress().getCity());
        assertEquals("India", result.getAddress().getCountry());
    }

    // ❌ NOT FOUND CASE
    @Test
    void testGetCustomerById_notFound() {
        when(customerRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.getCustomerById(1);
        });
    }

    // ✅ CREATE CUSTOMER SUCCESS
    @Test
    void testCreateCustomer_success() {

        CustomerCreateDto dto = new CustomerCreateDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@test.com");
        dto.setStoreId(1);
        dto.setAddressId(10L);

        // FULL ADDRESS SETUP (IMPORTANT)
        Address address = new Address();
        address.setAddressId(10L);
        address.setAddress("Street 1");

        City city = new City();
        city.setCity("Pune");

        Country country = new Country();
        country.setCountry("India");

        city.setCountry(country);
        address.setCity(city);

        when(addressRepository.findById(10L))
                .thenReturn(Optional.of(address));

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CustomerDto result = customerService.createCustomer(dto);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Pune", result.getAddress().getCity());
    }

    // ❌ CREATE CUSTOMER - ADDRESS NOT FOUND
    @Test
    void testCreateCustomer_addressNotFound() {

        CustomerCreateDto dto = new CustomerCreateDto();
        dto.setAddressId(10L);

        when(addressRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.createCustomer(dto);
        });
    }
}