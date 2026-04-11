package com.example.demo.rental.dto;

import java.time.LocalDateTime;

public class RentalResponse {

    private Integer rentalId;
    private LocalDateTime rentalDate;
    private Integer inventoryId;
    private Integer customerId;
    private LocalDateTime returnDate;
    private Integer staffId;
    private String status;

    public RentalResponse() {}

    public RentalResponse(Integer rentalId, LocalDateTime rentalDate, Integer inventoryId,
                          Integer customerId, LocalDateTime returnDate, Integer staffId) {
        this.rentalId = rentalId;
        this.rentalDate = rentalDate;
        this.inventoryId = inventoryId;
        this.customerId = customerId;
        this.returnDate = returnDate;
        this.staffId = staffId;
        this.status = (returnDate == null) ? "ACTIVE" : "RETURNED";
    }

    public Integer getRentalId() { return rentalId; }
    public void setRentalId(Integer rentalId) { this.rentalId = rentalId; }

    public LocalDateTime getRentalDate() { return rentalDate; }
    public void setRentalDate(LocalDateTime rentalDate) { this.rentalDate = rentalDate; }

    public Integer getInventoryId() { return inventoryId; }
    public void setInventoryId(Integer inventoryId) { this.inventoryId = inventoryId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
        this.status = (returnDate == null) ? "ACTIVE" : "RETURNED";
    }

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
