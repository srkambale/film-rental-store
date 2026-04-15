package com.example.demo.customer.controller;

import com.example.demo.customer.dto.PaymentRequestDto;
import com.example.demo.customer.dto.PaymentResponseDto;
import com.example.demo.customer.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Payment endpoints.
 *
 * Base path: /api/payments
 */
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ─── GET /api/payments ────────────────────────────────────────────────────
    /**
     * Retrieve all payments in the system.
     */
    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments() {
        List<PaymentResponseDto> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // ─── GET /api/payments/my?customerId=1 ────────────────────────────────────
    /**
     * Retrieve payments for a specific customer using a query param.
     * Example: GET /api/payments/my?customerId=1
     */
    @GetMapping("/my")
    public ResponseEntity<List<PaymentResponseDto>> getMyPayments(
            @RequestParam Long customerId) {
        List<PaymentResponseDto> payments = paymentService.getPaymentsByCustomer(customerId);
        return ResponseEntity.ok(payments);
    }

    // ─── GET /api/payments/{id} ───────────────────────────────────────────────
    /**
     * Retrieve a single payment by its primary key.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable Long id) {
        PaymentResponseDto payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    // ─── GET /api/payments/customer/{customerId} ──────────────────────────────
    /**
     * Retrieve all payments for a customer using a path variable.
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByCustomer(
            @PathVariable Long customerId) {
        List<PaymentResponseDto> payments = paymentService.getPaymentsByCustomer(customerId);
        return ResponseEntity.ok(payments);
    }

    // ─── GET /api/payments/rental/{rentalId} ──────────────────────────────────
    /**
     * Retrieve the payment associated with a specific rental.
     */
    @GetMapping("/rentals/{rentalId}")
    public ResponseEntity<PaymentResponseDto> getPaymentByRental(
            @PathVariable Long rentalId) {
        PaymentResponseDto payment = paymentService.getPaymentByRental(rentalId);
        return ResponseEntity.ok(payment);
    }

    // ─── GET /api/payments/balance/{customerId} ───────────────────────────────
    /**
     * Get total amount spent by a customer (their running balance).
     */
    @GetMapping("/balance/{customerId}")
    public ResponseEntity<Map<String, Object>> getCustomerBalance(
            @PathVariable Long customerId) {
        BigDecimal balance = paymentService.getCustomerBalance(customerId);
        return ResponseEntity.ok(Map.of(
                "customerId", customerId,
                "totalSpent", balance,
                "currency", "USD"
        ));
    }

    // ─── POST /api/payments ───────────────────────────────────────────────────
    /**
     * Process a new payment for an existing rental.
     * Returns 201 CREATED on success.
     */
    @PostMapping
    public ResponseEntity<PaymentResponseDto> processPayment(
            @RequestBody PaymentRequestDto request) {
        PaymentResponseDto response = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
