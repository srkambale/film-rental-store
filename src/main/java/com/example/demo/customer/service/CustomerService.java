package com.example.demo.customer.service;

import java.util.List;

import com.example.demo.customer.dto.*;

public interface CustomerService {
	CustomerDto getCustomerById(Integer id);

    CustomerDto createCustomer(CustomerCreateDto dto);

    CustomerDto updateCustomer(Integer id, CustomerUpdateDto dto);

    AddressDto updateAddress(Integer customerId, AddressDto dto);

    List<RentalResponseDto> getCustomerRentals(Integer customerId);

    List<PaymentDto> getCustomerPayments(Integer customerId);

    List<CustomerDto> getCustomersByName(String name);

    List<CustomerDto> getCustomersByLocation(String location);

    CustomerDto patchCustomer(Integer id, CustomerUpdateDto dto);

    AddressDto patchAddress(Integer customerId, AddressDto dto);

    PaymentDto getCustomerPaymentById(Integer customerId, Integer paymentId);

    RentalResponseDto getCustomerRentalById(Integer customerId, Integer rentalId);
}
