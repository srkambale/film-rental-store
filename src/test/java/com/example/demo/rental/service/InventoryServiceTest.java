package com.example.demo.rental.service;

import com.example.demo.rental.dto.InventoryDto;
import com.example.demo.rental.entity.Inventory;
import com.example.demo.rental.repository.InventoryRepository;
import com.example.demo.rental.repository.RentalRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void testGetAllInventory() {
        Inventory inv = new Inventory();
        inv.setInventoryId(1);
        inv.setFilmId(10);
        inv.setStoreId(2);

        when(inventoryRepository.findAll()).thenReturn(List.of(inv));

        List<InventoryDto> result = inventoryService.getAllInventory();

        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getFilmId());
    }

    @Test
    void testGetInventoryById() {
        Inventory inv = new Inventory();
        inv.setInventoryId(1);
        inv.setFilmId(5);

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inv));

        InventoryDto result = inventoryService.getInventoryById(1);

        assertEquals(5, result.getFilmId());
    }

    @Test
    void testAddInventory() {
        Inventory saved = new Inventory();
        saved.setInventoryId(1);
        saved.setFilmId(10);
        saved.setStoreId(1);

        when(inventoryRepository.save(any())).thenReturn(saved);

        InventoryDto dto = new InventoryDto(null, 10, 1);
        InventoryDto result = inventoryService.addInventory(dto);

        assertNotNull(result);
        assertEquals(10, result.getFilmId());
    }

    @Test
    void testDeleteInventory_Success() {
        when(inventoryRepository.existsById(1)).thenReturn(true);
        when(rentalRepository.existsByInventory_InventoryIdAndReturnDateIsNull(1)).thenReturn(false);

        inventoryService.deleteInventory(1);

        verify(inventoryRepository).deleteById(1);
    }

    @Test
    void testDeleteInventory_AlreadyRented() {
        when(inventoryRepository.existsById(1)).thenReturn(true);
        when(rentalRepository.existsByInventory_InventoryIdAndReturnDateIsNull(1)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> inventoryService.deleteInventory(1));
    }
}