package com.example.demo.payment.service;

import com.example.demo.customer.model.Customer;
import com.example.demo.customer.repository.CustomerRepository;
import com.example.demo.payment.dto.PaymentRequest;
import com.example.demo.payment.dto.PaymentResponse;
import com.example.demo.customer.model.Payment;
import com.example.demo.customer.repository.PaymentRepository;
import com.example.demo.rental.entity.Rental;
import com.example.demo.rental.repository.RentalRepository;
import com.example.demo.staff.entity.Staff;
import com.example.demo.staff.repository.StaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.util.List;
import java.util.stream.Collectors;
 
@Service
public class PaymentService {
 
    private final PaymentRepository  paymentRepository;
    private final CustomerRepository customerRepository;
    private final StaffRepository    staffRepository;
    private final RentalRepository   rentalRepository;
 
    public PaymentService(PaymentRepository paymentRepository,
                          CustomerRepository customerRepository,
                          StaffRepository staffRepository,
                          RentalRepository rentalRepository) {
        this.paymentRepository  = paymentRepository;
        this.customerRepository = customerRepository;
        this.staffRepository    = staffRepository;
        this.rentalRepository   = rentalRepository;
    }
 
    /** GET /api/payments — all payments (ADMIN) — NEW */
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
 
    /** POST /api/payments — record a new payment (STAFF, ADMIN) — existing */
    @Transactional
    public PaymentResponse makePayment(PaymentRequest request) {
 
        Customer customer = customerRepository.findById(request.getCustomerId().longValue())
                .orElseThrow(() -> new RuntimeException(
                        "Customer not found: " + request.getCustomerId()));
 
        Staff staff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new RuntimeException(
                        "Staff not found: " + request.getStaffId()));
 
        Rental rental = null;
        if (request.getRentalId() != null) {
            rental = rentalRepository.findById(request.getRentalId())
                    .orElseThrow(() -> new RuntimeException(
                            "Rental not found: " + request.getRentalId()));
        }
 
        Payment payment = new Payment();
        payment.setCustomer(customer);
        payment.setStaff(staff);
        payment.setRental(rental);
        payment.setAmount(request.getAmount());
 
        Payment saved = paymentRepository.save(payment);
        return toResponse(saved);
    }
 
    /** GET /api/payments/{id} — single payment by ID (STAFF, ADMIN) — existing */
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException(
                        "Payment not found: " + paymentId));
        return toResponse(payment);
    }
 
    /** GET /api/payments/my and /api/payments/customer/{customerId} — existing */
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByCustomer(Integer customerId) {
        return paymentRepository
                .findByCustomer_CustomerId(customerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
 
    /** GET /api/payments/rental/{rentalId} — existing */
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByRental(Integer rentalId) {
        return paymentRepository
                .findByRental_RentalId(rentalId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
 
    // ── Mapper ────────────────────────────────────────────────────────────────
 
    private PaymentResponse toResponse(Payment p) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(p.getPaymentId());
        response.setCustomerId(p.getCustomer().getCustomerId());
        response.setCustomerName(
                p.getCustomer().getFirstName() + " " + p.getCustomer().getLastName());
        response.setStaffId(p.getStaff().getStaffId());
        response.setRentalId(p.getRental() != null ? p.getRental().getRentalId() : null);
        response.setAmount(p.getAmount());
        response.setPaymentDate(p.getPaymentDate());
        return response;
    }
}
