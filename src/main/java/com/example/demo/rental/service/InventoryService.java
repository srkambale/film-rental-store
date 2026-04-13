package com.example.demo.rental.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.rental.dto.InventoryDto;
import com.example.demo.rental.entity.Inventory;
import com.example.demo.rental.repository.InventoryRepository;
import com.example.demo.rental.repository.RentalRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final RentalRepository rentalRepository;

    public InventoryService(InventoryRepository inventoryRepository,
            RentalRepository rentalRepository) {
        this.inventoryRepository = inventoryRepository;
        this.rentalRepository = rentalRepository;
    }

    // ─── Get all inventory ────────────────────────────────────────────────────
    public List<InventoryDto> getAllInventory() {
        return inventoryRepository.findAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    // ─── Get by ID ────────────────────────────────────────────────────────────
    public InventoryDto getInventoryById(Integer id) {
        return inventoryRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Inventory item not found with id: " + id));
    }

    // ─── Get by film ──────────────────────────────────────────────────────────
    public List<InventoryDto> getInventoryByFilm(Integer filmId) {
        return inventoryRepository.findByFilmId(filmId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    // ─── Get by store ─────────────────────────────────────────────────────────
    public List<InventoryDto> getInventoryByStore(Integer storeId) {
        return inventoryRepository.findByStoreId(storeId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    // ─── Get available inventory for a film at a store ────────────────────────
    public List<InventoryDto> getAvailableInventory(Integer filmId, Integer storeId) {
        return inventoryRepository.findByFilmIdAndStoreId(filmId, storeId)
                .stream()
                .filter(inv -> !rentalRepository.existsByInventory_InventoryIdAndReturnDateIsNull(inv.getInventoryId()))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ─── Add inventory item ──────────────────────────────────────────────────
    public InventoryDto addInventory(InventoryDto dto) {
        Inventory inv = new Inventory();
        inv.setFilmId(dto.getFilmId());
        inv.setStoreId(dto.getStoreId());
        return toDto(inventoryRepository.save(inv));
    }

    // ─── Delete inventory item ────────────────────────────────────────────────
    public void deleteInventory(Integer id) {
        if (!inventoryRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Inventory item not found with id: " + id);
        }
        // Prevent deletion if currently rented
        boolean currentlyRented = rentalRepository.existsByInventory_InventoryIdAndReturnDateIsNull(id);
        if (currentlyRented) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Cannot delete inventory item that is currently rented out");
        }
        inventoryRepository.deleteById(id);
    }

    // ─── Mapper ───────────────────────────────────────────────────────────────
    private InventoryDto toDto(Inventory inv) {
        return new InventoryDto(inv.getInventoryId(), inv.getFilmId(), inv.getStoreId());
    }
}
