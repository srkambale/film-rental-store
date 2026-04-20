package com.example.demo.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.customer.dto.AddressDto;
import com.example.demo.customer.dto.CustomerDto;
import com.example.demo.customer.dto.CustomerUpdateDto;
import com.example.demo.customer.dto.PaymentDto;
import com.example.demo.customer.dto.RentalResponseDto;
import com.example.demo.customer.service.CustomerService;

@CrossOrigin(origins = "http://10.30.74.131:8082")
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomer(@PathVariable Integer id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping("")
    public CustomerDto createCustomer(
            @jakarta.validation.Valid @RequestBody com.example.demo.customer.dto.CustomerCreateDto dto) {
        return customerService.createCustomer(dto);
    }

    @PutMapping("/{id}")
    public CustomerDto updateCustomer(@PathVariable Integer id,
            @RequestBody CustomerUpdateDto dto) {
        return customerService.updateCustomer(id, dto);
    }

    @PutMapping("/{id}/address")
    public AddressDto updateAddress(@PathVariable Integer id,
            @RequestBody AddressDto dto) {
        return customerService.updateAddress(id, dto);
    }

    @GetMapping("/{id}/rentals")
    public List<RentalResponseDto> getRentals(@PathVariable Integer id) {
        return customerService.getCustomerRentals(id);
    }

    @GetMapping("/{id}/payments")
    public List<PaymentDto> getPayments(@PathVariable Integer id) {
        return customerService.getCustomerPayments(id);
    }

    @GetMapping("/search")
    public List<CustomerDto> getCustomersByName(@RequestParam("name") String name) {
        return customerService.getCustomersByName(name);
    }

    @GetMapping("/location")
    public List<CustomerDto> getCustomersByLocation(@RequestParam("location") String location) {
        return customerService.getCustomersByLocation(location);
    }

    @PatchMapping("/{id}")
    public CustomerDto patchCustomer(@PathVariable Integer id, @RequestBody CustomerUpdateDto dto) {
        return customerService.patchCustomer(id, dto);
    }

    @PatchMapping("/{id}/address")
    public AddressDto patchAddress(@PathVariable Integer id, @RequestBody AddressDto dto) {
        return customerService.patchAddress(id, dto);
    }

    @GetMapping("/{id}/payment/{paymentId}")
    public PaymentDto getPaymentById(@PathVariable Integer id, @PathVariable Integer paymentId) {
        return customerService.getCustomerPaymentById(id, paymentId);
    }

    @GetMapping("/{id}/rentals/{rentalId}")
    public RentalResponseDto getRentalById(@PathVariable Integer id, @PathVariable Integer rentalId) {
        return customerService.getCustomerRentalById(id, rentalId);
    }
}
