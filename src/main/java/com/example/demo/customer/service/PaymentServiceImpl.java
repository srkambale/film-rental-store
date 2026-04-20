package com.example.demo.customer.service;

import com.example.demo.customer.dto.PaymentRequestDto;
import com.example.demo.customer.dto.PaymentResponseDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.customer.model.Customer;
import com.example.demo.customer.model.CustomerRental;
import com.example.demo.customer.model.Payment;
import com.example.demo.customer.repository.CustomerRepository;
import com.example.demo.customer.repository.CustomerRentalRepository;
import com.example.demo.customer.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final CustomerRentalRepository rentalRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              CustomerRepository customerRepository,
                              CustomerRentalRepository rentalRepository) {
        this.paymentRepository    = paymentRepository;
        this.customerRepository   = customerRepository;
        this.rentalRepository     = rentalRepository;
    }

   

    @Override
    public PaymentResponseDto processPayment(PaymentRequestDto request) {

        
        if (request.getCustomerId() == null) {
            throw new BadRequestException("customerId is required.");
        }
        if (request.getRentalId() == null) {
            throw new BadRequestException("rentalId is required.");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be greater than zero.");
        }

        // 2. Validate customer exists
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with ID: " + request.getCustomerId()));

        // 3. Validate rental exists and belongs to the customer
        CustomerRental rental = rentalRepository
                .fetchByCustomerIdAndRentalId(request.getCustomerId(), request.getRentalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rental not found with ID " + request.getRentalId()
                        + " for customer ID " + request.getCustomerId()));

        // 4. Check for duplicate payment on the same rental
        boolean alreadyPaid = paymentRepository
                .findAll()
                .stream()
                .anyMatch(p -> p.getRental() != null
                        && rental.getRentalId() != null
                        && rental.getRentalId().equals(p.getRental().getRentalId()));

        if (alreadyPaid) {
            throw new BadRequestException(
                    "Payment already exists for rental ID: " + request.getRentalId());
        }

      
        Payment payment = new Payment();
        payment.setCustomerId(request.getCustomerId());
        payment.setStaffId(request.getStaffId() != null ? request.getStaffId() : 1);
        payment.setRental(rental);
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);

       
        PaymentResponseDto response = mapToDto(saved);
        response.setStatus("SUCCESS");
        response.setMessage("Payment processed successfully for rental ID: " + request.getRentalId());
        return response;
    }

   

    @Override
    public List<PaymentResponseDto> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

   

    @Override
    public PaymentResponseDto getPaymentById(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                         "Payment not found with ID: " + paymentId));
        return mapToDto(payment);
    }

    // ─── Fetch Payments by Customer ──────────────────────────────────────────

    @Override
    public List<PaymentResponseDto> getPaymentsByCustomer(Integer customerId) {
        // Verify customer exists before querying
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with ID: " + customerId));

        return paymentRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponseDto> getMyPayments(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerResourceNotFoundException("Customer not found with email: " + email));
        return getPaymentsByCustomer(customer.getCustomerId());
    }

    // ─── Fetch Payment by Rental ─────────────────────────────────────────────

    @Override
    public PaymentResponseDto getPaymentByRental(Integer rentalId) {
        // Verify rental exists
        rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rental not found with ID: " + rentalId));

        return paymentRepository.findAll()
                .stream()
                .filter(p -> p.getRental() != null
                        && rentalId.equals(p.getRental().getRentalId()))
                .findFirst()
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No payment found for rental ID: " + rentalId));
    }

    // ─── Get Customer Balance (total spent) ───────────────────────────────────

    @Override
    public BigDecimal getCustomerBalance(Integer customerId) {
        // Verify customer exists
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with ID: " + customerId));

        return paymentRepository.findByCustomerId(customerId)
                .stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ─── Mapper ───────────────────────────────────────────────────────────────

    private PaymentResponseDto mapToDto(Payment p) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setPaymentId(p.getPaymentId());
        dto.setCustomerId(p.getCustomerId());
        dto.setStaffId(p.getStaffId());

        Integer rentalId = p.getRental() != null ? p.getRental().getRentalId().intValue() : null;
        dto.setRentalId(rentalId);

        dto.setAmount(p.getAmount());
        dto.setPaymentDate(p.getPaymentDate());
        dto.setStatus("SUCCESS");
        dto.setMessage("Payment of " + p.getAmount()
                + " processed successfully for rental ID: " + rentalId);
        return dto;
    }
}
