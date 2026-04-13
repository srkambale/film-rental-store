package com.example.demo.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {
	private Integer paymentId;
    private Integer customerId;
    private String customerName;
    private Integer staffId;
    private Long rentalId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
 

 
    public PaymentResponse() {}
 
    public PaymentResponse(Integer paymentId, Integer customerId, String customerName,
                           Integer staffId, Long rentalId, BigDecimal amount,
                           LocalDateTime paymentDate) {
        this.paymentId    = paymentId;
        this.customerId   = customerId;
        this.customerName = customerName;
        this.staffId      = staffId;
        this.rentalId     = rentalId;
        this.amount       = amount;
        this.paymentDate  = paymentDate;
    }
 
   
 
    public Integer getPaymentId()         { return paymentId; }
    public Integer getCustomerId()        { return customerId; }
    public String getCustomerName()       { return customerName; }
    public Integer getStaffId()           { return staffId; }
    public Long getRentalId()          { return rentalId; }
    public BigDecimal getAmount()         { return amount; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
 
    
 
    public void setPaymentId(Integer paymentId)          { this.paymentId = paymentId; }
    public void setCustomerId(Integer customerId)         { this.customerId = customerId; }
    public void setCustomerName(String customerName)      { this.customerName = customerName; }
    public void setStaffId(Integer staffId)               { this.staffId = staffId; }
    public void setRentalId(Long integer)             { this.rentalId = integer; }
    public void setAmount(BigDecimal amount)              { this.amount = amount; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

	public void setRentalId(Integer integer) {
		// TODO Auto-generated method stub
		
	}
}
