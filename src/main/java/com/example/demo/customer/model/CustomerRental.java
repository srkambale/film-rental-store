package com.example.demo.customer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rental")
public class CustomerRental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id", columnDefinition = "MEDIUMINT UNSIGNED")
    private Integer rentalId;

    @Column(name = "rental_date", nullable = false)
    private LocalDateTime rentalDate;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private CustomerInventory CustomerInventory;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "staff_id", nullable = false, columnDefinition = "TINYINT UNSIGNED")
    private Integer staffId;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    // Constructors
    public CustomerRental() {}

    public CustomerRental(Integer rentalId, LocalDateTime rentalDate, CustomerInventory CustomerInventory,
                  Customer customer, LocalDateTime returnDate,
                  Integer staffId, LocalDateTime lastUpdate) {
        this.rentalId = rentalId;
        this.rentalDate = rentalDate;
        this.CustomerInventory = CustomerInventory;
        this.customer = customer;
        this.returnDate = returnDate;
        this.staffId = staffId;
        this.lastUpdate = lastUpdate;
    }

    // Getters and Setters
    public Long getRentalId() { return rentalId != null ? rentalId.longValue() : null; }
    public void setRentalId(Long rentalId) { this.rentalId = rentalId != null ? rentalId.intValue() : null; }

    public LocalDateTime getRentalDate() { return rentalDate; }
    public void setRentalDate(LocalDateTime rentalDate) { this.rentalDate = rentalDate; }

    public CustomerInventory getInventory() { return CustomerInventory; }
    public void setInventory(CustomerInventory CustomerInventory) { this.CustomerInventory = CustomerInventory; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }

    public Long getStaffId() { return staffId != null ? staffId.longValue() : null; }
    public void setStaffId(Long staffId) { this.staffId = staffId != null ? staffId.intValue() : null; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}
