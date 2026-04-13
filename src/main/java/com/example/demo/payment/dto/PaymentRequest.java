package com.example.demo.payment.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class PaymentRequest {
	@NotNull(message = "Customer ID is required")
    private Integer customerId;
 
    @NotNull(message = "Staff ID is required")
    private Integer staffId;
 
    // Optional — payment may not be linked to a rental
    private Integer rentalId;
 
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
 
    // ── Constructors ──────────────────────────────────────────────────────────
 
    public PaymentRequest() {}
 
    public PaymentRequest(Integer customerId, Integer staffId,
                          Integer rentalId, BigDecimal amount) {
        this.customerId = customerId;
        this.staffId    = staffId;
        this.rentalId   = rentalId;
        this.amount     = amount;
    }
 
    // ── Getters ───────────────────────────────────────────────────────────────
 
    public Integer getCustomerId()  { return customerId; }
    public Integer getStaffId()     { return staffId; }
    public Integer getRentalId()    { return rentalId; }
    public BigDecimal getAmount()   { return amount; }
 
    // ── Setters ───────────────────────────────────────────────────────────────
 
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public void setStaffId(Integer staffId)       { this.staffId = staffId; }
    public void setRentalId(Integer rentalId)     { this.rentalId = rentalId; }
    public void setAmount(BigDecimal amount)       { this.amount = amount; }
}
