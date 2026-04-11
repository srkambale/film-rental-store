package com.example.demo.customer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.customer.repository.*;
import com.example.demo.customer.dto.CustomerDto;
import com.example.demo.customer.dto.*;
import com.example.demo.customer.exception.CustomerResourceNotFoundException;
import com.example.demo.customer.model.*;


@Service
public class CustomerServiceImpl implements CustomerService {

	private CustomerRepository customerRepository;
    private CustomerRentalRepository rentalRepository;
    private PaymentRepository paymentRepository;


    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CustomerRentalRepository rentalRepository,
                               PaymentRepository paymentRepository) {
        this.customerRepository = customerRepository;
        this.rentalRepository = rentalRepository;
        this.paymentRepository = paymentRepository;
    }

	    @Override
	    public CustomerDto getCustomerById(Long id) {
	        Customer customer = customerRepository.findById(id)
	                .orElseThrow(() -> new CustomerResourceNotFoundException("Customer not found"));

	        return mapToDto(customer);
	    }

	    @Override
	    public CustomerDto updateCustomer(Long id, CustomerUpdateDto dto) {
	        Customer customer = customerRepository.findById(id)
	                .orElseThrow(() -> new CustomerResourceNotFoundException("Customer not found"));

	        customer.setFirstName(dto.getFirstName());
	        customer.setLastName(dto.getLastName());
	        customer.setEmail(dto.getEmail());

	        return mapToDto(customerRepository.save(customer));
	    }

	    @Override
	    public AddressDto updateAddress(Long customerId, AddressDto dto) {
	        Customer customer = customerRepository.findById(customerId)
	                .orElseThrow(() -> new CustomerResourceNotFoundException("Customer not found"));

	        Address address = customer.getAddress();
	        address.setAddress(dto.getAddress());
	        address.setDistrict(dto.getDistrict());
	        address.setPostalCode(dto.getPostalCode());
	        address.setPhone(dto.getPhone());

	        return mapToAddressDto(address);
	    }

	    @Override
	    public List<RentalResponseDto> getCustomerRentals(Long customerId) {
	        List<CustomerRental> rentals = rentalRepository.findByCustomerId(customerId);

	        return rentals.stream()
	                .map(this::mapToRentalDto)
	                .toList();
	    }

	    @Override
	    public List<PaymentDto> getCustomerPayments(Long customerId) {
	        List<Payment> payments = paymentRepository.findByCustomerId(customerId);

	        return payments.stream()
	                .map(this::mapToPaymentDto)
	                .toList();
	    }

	    // ================== MAPPERS ==================

	    private CustomerDto mapToDto(Customer c) {
	        CustomerDto dto = new CustomerDto();
	        dto.setCustomerId(c.getCustomerId());
	        dto.setFirstName(c.getFirstName());
	        dto.setLastName(c.getLastName());
	        dto.setEmail(c.getEmail());
	        dto.setActive(c.getActive());
	        dto.setAddress(mapToAddressDto(c.getAddress()));
	        return dto;
	    }

	    private AddressDto mapToAddressDto(Address a) {
	        AddressDto dto = new AddressDto();
	        dto.setAddressId(a.getAddressId());
	        dto.setAddress(a.getAddress());
	        dto.setDistrict(a.getDistrict());
	        dto.setPostalCode(a.getPostalCode());
	        dto.setPhone(a.getPhone());
	        dto.setCity(a.getCity().getCity());
	        dto.setCountry(a.getCity().getCountry().getCountry());
	        return dto;
	    }

	    private RentalResponseDto mapToRentalDto(CustomerRental r) {
	        RentalResponseDto dto = new RentalResponseDto();
	        dto.setRentalId(r.getRentalId());
	        dto.setRentalDate(r.getRentalDate());
	        dto.setReturnDate(r.getReturnDate());
	        dto.setFilmTitle(r.getInventory().getFilm().getTitle());
	        dto.setStatus(r.getReturnDate() == null ? "RENTED" : "RETURNED");
	        return dto;
	    }

	    private PaymentDto mapToPaymentDto(Payment p) {
	        PaymentDto dto = new PaymentDto();
	        dto.setPaymentId(p.getPaymentId());
	        dto.setAmount(p.getAmount());
	        dto.setPaymentDate(p.getPaymentDate());
	        dto.setRentalId(p.getRental().getRentalId());
	        return dto;
	    }
	}
    
