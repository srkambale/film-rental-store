package com.example.demo.customer.dto;

import java.math.BigDecimal;

/**
 * Incoming request body for processing a new payment.
 */
public class PaymentRequestDto {

    private Integer customerId;
    private Integer rentalId;
    private BigDecimal amount;

    // Default staff ID used for mock/dummy payment processing
    private Integer staffId = 1;

    public PaymentRequestDto() {}

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getRentalId() { return rentalId; }
    public void setRentalId(Integer rentalId) { this.rentalId = rentalId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }
}
