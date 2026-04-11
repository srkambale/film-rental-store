package com.example.demo.customer.service;

import java.util.List;

import com.example.demo.customer.dto.*;

public interface CustomerService {
	CustomerDto getCustomerById(Long id);

    CustomerDto updateCustomer(Long id, CustomerUpdateDto dto);

    AddressDto updateAddress(Long customerId, AddressDto dto);

    List<RentalResponseDto> getCustomerRentals(Long customerId);

    List<PaymentDto> getCustomerPayments(Long customerId);
}
