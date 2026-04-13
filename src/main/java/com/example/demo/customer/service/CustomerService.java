package com.example.demo.customer.service;

import java.util.List;

import com.example.demo.customer.dto.*;

public interface CustomerService {
	CustomerDto getCustomerById(Long id);

    CustomerDto createCustomer(CustomerCreateDto dto);

    CustomerDto updateCustomer(Long id, CustomerUpdateDto dto);

    AddressDto updateAddress(Long customerId, AddressDto dto);

    List<RentalResponseDto> getCustomerRentals(Long customerId);

    List<PaymentDto> getCustomerPayments(Long customerId);

    List<CustomerDto> getCustomersByName(String name);

    List<CustomerDto> getCustomersByLocation(String location);

    CustomerDto patchCustomer(Long id, CustomerUpdateDto dto);

    AddressDto patchAddress(Long customerId, AddressDto dto);

    PaymentDto getCustomerPaymentById(Long customerId, Long paymentId);

    RentalResponseDto getCustomerRentalById(Long customerId, Long rentalId);
}
