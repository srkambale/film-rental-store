package com.example.demo.customer.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO returned after a successful payment or payment lookup.
 */
public class PaymentResponseDto {

    private Integer paymentId;
    private Integer customerId;
    private Integer staffId;
    private Integer rentalId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String status;
    private String message;

    public PaymentResponseDto() {}

    // ─── Getters & Setters ───────────────────────────────────────────────────

    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }

    public Integer getRentalId() { return rentalId; }
    public void setRentalId(Integer rentalId) { this.rentalId = rentalId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
