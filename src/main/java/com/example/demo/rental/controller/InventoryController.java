package com.example.demo.rental.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.rental.dto.InventoryDto;
import com.example.demo.rental.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // GET /api/inventory  — Get all inventory items
    @GetMapping
    public ResponseEntity<List<InventoryDto>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    // GET /api/inventory/{id}  — Get inventory item by ID
    @GetMapping("/{id}")
    public ResponseEntity<InventoryDto> getInventoryById(@PathVariable Integer id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    // GET /api/inventory/film/{filmId}  — All copies of a film (all stores)
    @GetMapping("/film/{filmId}")
    public ResponseEntity<List<InventoryDto>> getByFilm(@PathVariable Integer filmId) {
        return ResponseEntity.ok(inventoryService.getInventoryByFilm(filmId));
    }

    // GET /api/inventory/store/{storeId}  — All inventory at a store
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<InventoryDto>> getByStore(@PathVariable Integer storeId) {
        return ResponseEntity.ok(inventoryService.getInventoryByStore(storeId));
    }

    // GET /api/inventory/available?filmId=1&storeId=1  — Available (not rented) copies
    @GetMapping("/available")
    public ResponseEntity<List<InventoryDto>> getAvailableInventory(
            @RequestParam Integer filmId,
            @RequestParam Integer storeId) {
        return ResponseEntity.ok(inventoryService.getAvailableInventory(filmId, storeId));
    }

    // POST /api/inventory  — Add new inventory item
    @PostMapping
    public ResponseEntity<InventoryDto> addInventory(@RequestBody InventoryDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventoryService.addInventory(dto));
    }

    // DELETE /api/inventory/{id}  — Remove inventory item (blocked if rented)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Integer id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}
