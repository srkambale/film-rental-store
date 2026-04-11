package com.example.demo.customer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CustomerInventory")
public class CustomerInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long inventoryId;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    // Constructors
    public CustomerInventory() {}

    public CustomerInventory(Long inventoryId, Film film, Long storeId, LocalDateTime lastUpdate) {
        this.inventoryId = inventoryId;
        this.film = film;
        this.storeId = storeId;
        this.lastUpdate = lastUpdate;
    }

    // Getters and Setters
    public Long getInventoryId() { return inventoryId; }
    public void setInventoryId(Long inventoryId) { this.inventoryId = inventoryId; }

    public Film getFilm() { return film; }
    public void setFilm(Film film) { this.film = film; }

    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}
