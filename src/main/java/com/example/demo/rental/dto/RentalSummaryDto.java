package com.example.demo.rental.dto;

import java.time.LocalDateTime;

public class RentalSummaryDto {

    private Integer rentalId;
    private Integer customerId;
    private Integer inventoryId;
    private LocalDateTime rentalDate;
    private String status;

    public RentalSummaryDto() {}

    public RentalSummaryDto(Integer rentalId, Integer customerId,
                            Integer inventoryId, LocalDateTime rentalDate, LocalDateTime returnDate) {
        this.rentalId = rentalId;
        this.customerId = customerId;
        this.inventoryId = inventoryId;
        this.rentalDate = rentalDate;
        this.status = (returnDate == null) ? "ACTIVE" : "RETURNED";
    }

    public Integer getRentalId() { return rentalId; }
    public void setRentalId(Integer rentalId) { this.rentalId = rentalId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getInventoryId() { return inventoryId; }
    public void setInventoryId(Integer inventoryId) { this.inventoryId = inventoryId; }

    public LocalDateTime getRentalDate() { return rentalDate; }
    public void setRentalDate(LocalDateTime rentalDate) { this.rentalDate = rentalDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
