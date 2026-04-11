package com.example.demo.rental.dto;

public class InventoryDto {

    private Integer inventoryId;
    private Integer filmId;
    private Integer storeId;

    public InventoryDto() {}

    public InventoryDto(Integer inventoryId, Integer filmId, Integer storeId) {
        this.inventoryId = inventoryId;
        this.filmId = filmId;
        this.storeId = storeId;
    }

    public Integer getInventoryId() { return inventoryId; }
    public void setInventoryId(Integer inventoryId) { this.inventoryId = inventoryId; }

    public Integer getFilmId() { return filmId; }
    public void setFilmId(Integer filmId) { this.filmId = filmId; }

    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }
}
