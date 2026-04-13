package com.example.demo.payment.controller;

import com.example.demo.payment.dto.PaymentRequest;
import com.example.demo.payment.dto.PaymentResponse;
import com.example.demo.payment.service.CustomerBalanceService;
import com.example.demo.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.math.BigDecimal;
import java.util.List;
 
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
 
    private final PaymentService         paymentService;
    private final CustomerBalanceService balanceService;
 
    public PaymentController(PaymentService paymentService,
                             CustomerBalanceService balanceService) {
        this.paymentService = paymentService;
        this.balanceService = balanceService;
    }
 
    /**
     * GET /api/payments
     * Access: ADMIN
     * Get all payments in the system — NEW
     */
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
 
    /**
     * GET /api/payments/my
     * Access: CUSTOMER
     * Get logged-in customer's own payment history — NEW
     */
    @GetMapping("/my")
    public ResponseEntity<List<PaymentResponse>> getMyPayments(
            @RequestParam Integer customerId) {
        return ResponseEntity.ok(paymentService.getPaymentsByCustomer(customerId));
    }
 
    /**
     * GET /api/payments/{id}
     * Access: STAFF, ADMIN
     * Get a single payment by ID — existing
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }
 
    /**
     * GET /api/payments/customer/{customerId}
     * Access: STAFF, ADMIN
     * Get all payments for a specific customer — existing
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PaymentResponse>> getByCustomer(
            @PathVariable Integer customerId) {
        return ResponseEntity.ok(paymentService.getPaymentsByCustomer(customerId));
    }
 
    /**
     * GET /api/payments/rental/{rentalId}
     * Access: STAFF, ADMIN
     * Get all payments linked to a rental — existing
     */
    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<List<PaymentResponse>> getByRental(
            @PathVariable Integer rentalId) {
        return ResponseEntity.ok(paymentService.getPaymentsByRental(rentalId));
    }
 
    /**
     * GET /api/payments/balance/{customerId}
     * Access: STAFF, ADMIN
     * Get outstanding balance for a customer — existing
     */
    @GetMapping("/balance/{customerId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long customerId) {
        return ResponseEntity.ok(balanceService.getCustomerBalance(customerId));
    }
 
    /**
     * POST /api/payments
     * Access: STAFF, ADMIN
     * Record a new payment — existing
     */
    @PostMapping
    public ResponseEntity<PaymentResponse> makePayment(
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.makePayment(request));
    }
}