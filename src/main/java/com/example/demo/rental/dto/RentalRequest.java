package com.example.demo.rental.dto;

import jakarta.validation.constraints.NotNull;

public class RentalRequest {

    @NotNull(message = "Inventory ID is required")
    private Integer inventoryId;

    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @NotNull(message = "Staff ID is required")
    private Integer staffId;

    public RentalRequest() {}

    public Integer getInventoryId() { return inventoryId; }
    public void setInventoryId(Integer inventoryId) { this.inventoryId = inventoryId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }
}
