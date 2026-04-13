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

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomer(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PutMapping("/{id}")
    public CustomerDto updateCustomer(@PathVariable Long id,
                                      @RequestBody CustomerUpdateDto dto) {
        return customerService.updateCustomer(id, dto);
    }

    @PutMapping("/{id}/address")
    public AddressDto updateAddress(@PathVariable Long id,
                                   @RequestBody AddressDto dto) {
        return customerService.updateAddress(id, dto);
    }

    @GetMapping("/{id}/rentals")
    public List<RentalResponseDto> getRentals(@PathVariable Long id) {
        return customerService.getCustomerRentals(id);
    }

    @GetMapping("/{id}/payments")
    public List<PaymentDto> getPayments(@PathVariable Long id) {
        return customerService.getCustomerPayments(id);
    }
}
