package com.example.demo.customer.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", columnDefinition = "SMALLINT UNSIGNED")
    private Integer paymentId;

    @Column(name = "customer_id", nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    private Integer customerId;

    @Column(name = "staff_id", nullable = false, columnDefinition = "TINYINT UNSIGNED")
    private Integer staffId;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    private CustomerRental CustomerRental;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;
    
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    // Constructors
    public Payment() {}

    public Payment(Integer paymentId, Integer customerId, Integer staffId,
                   CustomerRental CustomerRental, BigDecimal amount, LocalDateTime paymentDate, LocalDateTime lastUpdate) {
        this.paymentId = paymentId;
        this.customerId = customerId;
        this.staffId = staffId;
        this.CustomerRental = CustomerRental;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.lastUpdate = lastUpdate;
    }

    // Getters and Setters
    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }

    public CustomerRental getRental() { return CustomerRental; }
    public void setRental(CustomerRental CustomerRental) { this.CustomerRental = CustomerRental; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
}