package com.example.demo.customer.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.customer.repository.*;
import com.example.demo.customer.dto.CustomerDto;
import com.example.demo.customer.dto.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.customer.model.*;


@Service
public class CustomerServiceImpl implements CustomerService {

	private CustomerRepository customerRepository;
    private CustomerRentalRepository rentalRepository;
    private PaymentRepository paymentRepository;
    private AddressRepository addressRepository;


    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CustomerRentalRepository rentalRepository,
                               PaymentRepository paymentRepository,
                               AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.rentalRepository = rentalRepository;
        this.paymentRepository = paymentRepository;
        this.addressRepository = addressRepository;
    }

	    @Override
	    public CustomerDto getCustomerById(Integer id) {
	        Customer customer = customerRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

	        return mapToDto(customer);
	    }

	    @Override
	    public CustomerDto createCustomer(CustomerCreateDto dto) {
	        Address address = addressRepository.findById(dto.getAddressId())
	                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id " + dto.getAddressId()));
	        
	        Customer customer = new Customer();
	        customer.setFirstName(dto.getFirstName());
	        customer.setLastName(dto.getLastName());
	        customer.setEmail(dto.getEmail());
	        customer.setStoreId(dto.getStoreId());
	        customer.setAddress(address);
	        customer.setActive(true);
	        customer.setCreateDate(java.time.LocalDateTime.now());
	        customer.setLastUpdate(java.time.LocalDateTime.now());
	        
	        Customer savedCustomer = customerRepository.save(customer);
	        return mapToDto(savedCustomer);
	    }

	    @Override
	    public CustomerDto updateCustomer(Integer id, CustomerUpdateDto dto) {
	        Customer customer = customerRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

	        customer.setFirstName(dto.getFirstName());
	        customer.setLastName(dto.getLastName());
	        customer.setEmail(dto.getEmail());

	        return mapToDto(customerRepository.save(customer));
	    }

	    @Override
	    public AddressDto updateAddress(Integer customerId, AddressDto dto) {
	        Customer customer = customerRepository.findById(customerId)
	                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

	        Address address = customer.getAddress();
	        address.setAddress(dto.getAddress());
	        address.setDistrict(dto.getDistrict());
	        address.setPostalCode(dto.getPostalCode());
	        address.setPhone(dto.getPhone());

	        return mapToAddressDto(address);
	    }

	    @Override
	    public List<RentalResponseDto> getCustomerRentals(Integer customerId) {
	        List<CustomerRental> rentals = rentalRepository.findByCustomerId(customerId);

	        return rentals.stream()
	                .map(this::mapToRentalDto)
	                .toList();
	    }

	    @Override
	    public List<PaymentDto> getCustomerPayments(Integer customerId) {
	        List<Payment> payments = paymentRepository.findByCustomerId(customerId);

	        return payments.stream()
	                .map(this::mapToPaymentDto)
	                .toList();
	    }

	    @Override
	    public List<CustomerDto> getCustomersByName(String name) {
	        List<Customer> customers = customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
	        
	        return customers.stream()
	                .map(this::mapToDto)
	                .toList();
	    }

	    @Override
	    public List<CustomerDto> getCustomersByLocation(String location) {
	        List<Customer> customers = customerRepository.findByLocationIgnoreCase(location);
	        return customers.stream()
	                .map(this::mapToDto)
	                .toList();
	    }

	    @Override
	    public CustomerDto patchCustomer(Integer id, CustomerUpdateDto dto) {
	        Customer customer = customerRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

	        if (dto.getFirstName() != null) customer.setFirstName(dto.getFirstName());
	        if (dto.getLastName() != null) customer.setLastName(dto.getLastName());
	        if (dto.getEmail() != null) customer.setEmail(dto.getEmail());

	        return mapToDto(customerRepository.save(customer));
	    }

	    @Override
	    public AddressDto patchAddress(Integer customerId, AddressDto dto) {
	        Customer customer = customerRepository.findById(customerId)
	                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

	        Address address = customer.getAddress();
	        if (dto.getAddress() != null) address.setAddress(dto.getAddress());
	        if (dto.getDistrict() != null) address.setDistrict(dto.getDistrict());
	        if (dto.getPostalCode() != null) address.setPostalCode(dto.getPostalCode());
	        if (dto.getPhone() != null) address.setPhone(dto.getPhone());

	        customerRepository.save(customer);
	        return mapToAddressDto(address);
	    }

	    @Override
	    public PaymentDto getCustomerPaymentById(Integer customerId, Integer paymentId) {
	        Payment payment = paymentRepository.findByCustomerIdAndPaymentId(customerId, paymentId)
	                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
	        return mapToPaymentDto(payment);
	    }

	    @Override
	    public RentalResponseDto getCustomerRentalById(Integer customerId, Integer rentalId) {
	        CustomerRental rental = rentalRepository.fetchByCustomerIdAndRentalId(customerId, rentalId)
	                .orElseThrow(() -> new ResourceNotFoundException("Rental not found"));
	        return mapToRentalDto(rental);
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
	        
	        if (r.getReturnDate() == null) {
	            dto.setStatus("RENTED");
	            if (r.getInventory() != null && r.getInventory().getFilm() != null && r.getInventory().getFilm().getRentalDuration() != null) {
	                LocalDateTime expectedReturn = r.getRentalDate().plusDays(r.getInventory().getFilm().getRentalDuration());
	                dto.setReturnDate(expectedReturn);
	                dto.setIsOverdue(LocalDateTime.now().isAfter(expectedReturn));
	            } else {
	                dto.setIsOverdue(false);
	            }
	        } else {
	            dto.setStatus("RETURNED");
	            dto.setReturnDate(r.getReturnDate());
	            dto.setIsOverdue(false);
	        }

	        if (r.getInventory() != null && r.getInventory().getFilm() != null) {
	            dto.setFilmTitle(r.getInventory().getFilm().getTitle());
	        }
	        return dto;
	    }

	    private PaymentDto mapToPaymentDto(Payment p) {
	        PaymentDto dto = new PaymentDto();
	        dto.setPaymentId(p.getPaymentId());
	        dto.setAmount(p.getAmount());
	        dto.setPaymentDate(p.getPaymentDate());
	        dto.setRentalId(p.getRental() != null ? p.getRental().getRentalId() : null);
	        return dto;
	    }
	}
    
