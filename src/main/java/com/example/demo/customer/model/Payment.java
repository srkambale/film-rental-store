package com.example.demo.customer.model;

import com.example.demo.customer.model.Customer;       // Member C — confirm this import path
import com.example.demo.staff.entity.Staff;              // Member A — confirm this import path
import com.example.demo.customer.model.CustomerRental; // Member D — confirm this import path
import com.example.demo.rental.entity.Rental;

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
	 
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "customer_id", nullable = false)
	    private Customer customer;
	 
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "staff_id", nullable = false)
	    private Staff staff;
	 
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "rental_id")
	    private Rental rental;                          // was CustomerRental — now matches Member D
	 
	    @Column(name = "amount", nullable = false)
	    private BigDecimal amount;
	 
	    @Column(name = "payment_date", nullable = false)
	    private LocalDateTime paymentDate;
	 
	    @Column(name = "last_update", nullable = false)
	    private LocalDateTime lastUpdate;
	 
	    // ── Constructors ──────────────────────────────────────────────────────────
	 
	    public Payment() {}
	 
	    public Payment(Integer paymentId, Customer customer, Staff staff,
	                   Rental rental, BigDecimal amount,
	                   LocalDateTime paymentDate, LocalDateTime lastUpdate) {
	        this.paymentId   = paymentId;
	        this.customer    = customer;
	        this.staff       = staff;
	        this.rental      = rental;
	        this.amount      = amount;
	        this.paymentDate = paymentDate;
	        this.lastUpdate  = lastUpdate;
	    }
	 
	    // ── Lifecycle hooks ───────────────────────────────────────────────────────
	 
	    @PrePersist
	    public void prePersist() {
	        this.paymentDate = LocalDateTime.now();
	        this.lastUpdate  = LocalDateTime.now();
	    }
	 
	    @PreUpdate
	    public void preUpdate() {
	        this.lastUpdate = LocalDateTime.now();
	    }
	 
	    // ── Getters ───────────────────────────────────────────────────────────────
	 
	    public Integer getPaymentId()         { return paymentId; }
	    public Customer getCustomer()         { return customer; }
	    public Staff getStaff()               { return staff; }
	    public Rental getRental()             { return rental; }
	    public BigDecimal getAmount()         { return amount; }
	    public LocalDateTime getPaymentDate() { return paymentDate; }
	    public LocalDateTime getLastUpdate()  { return lastUpdate; }
	 
	    // ── Setters ───────────────────────────────────────────────────────────────
	 
	    public void setPaymentId(Integer paymentId)          { this.paymentId = paymentId; }
	    public void setCustomer(Customer customer)            { this.customer = customer; }
	    public void setStaff(Staff staff)                    { this.staff = staff; }
	    public void setRental(Rental rental)                 { this.rental = rental; }  // now accepts Rental
	    public void setAmount(BigDecimal amount)              { this.amount = amount; }
	    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
	    public void setLastUpdate(LocalDateTime lastUpdate)   { this.lastUpdate = lastUpdate; }
	 
	    // ── Builder ───────────────────────────────────────────────────────────────
	 
	    public static Builder builder() { return new Builder(); }
	 
	    public static class Builder {
	        private Integer paymentId;
	        private Customer customer;
	        private Staff staff;
	        private Rental rental;
	        private BigDecimal amount;
	        private LocalDateTime paymentDate;
	        private LocalDateTime lastUpdate;
	 
	        public Builder paymentId(Integer paymentId)            { this.paymentId = paymentId;    return this; }
	        public Builder customer(Customer customer)              { this.customer = customer;       return this; }
	        public Builder staff(Staff staff)                      { this.staff = staff;             return this; }
	        public Builder rental(Rental rental)                   { this.rental = rental;           return this; }
	        public Builder amount(BigDecimal amount)                { this.amount = amount;           return this; }
	        public Builder paymentDate(LocalDateTime paymentDate)   { this.paymentDate = paymentDate; return this; }
	        public Builder lastUpdate(LocalDateTime lastUpdate)     { this.lastUpdate = lastUpdate;   return this; }
	 
	        public Payment build() {
	            return new Payment(paymentId, customer, staff, rental,
	                               amount, paymentDate, lastUpdate);
	        }
	    }
}