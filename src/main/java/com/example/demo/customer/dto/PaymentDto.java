package com.example.demo.customer.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDto {
	private Integer paymentId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private Integer rentalId;          // changed from Long to Integer — matches Rental.getRentalId()
 
    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }
 
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
 
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
 
    public Integer getRentalId() { return rentalId; }
    public void setRentalId(Integer rentalId) { this.rentalId = rentalId; }
    
    
}
