package com.example.demo.rental.controller;

import com.example.demo.rental.dto.InventoryDto;
import com.example.demo.rental.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private InventoryDto inventoryDto;

    @BeforeEach
    void setUp() {
        inventoryDto = new InventoryDto(1, 1, 1);
    }

    @Test
    void testGetAllInventory() {
        List<InventoryDto> list = Arrays.asList(inventoryDto);
        when(inventoryService.getAllInventory()).thenReturn(list);

        ResponseEntity<List<InventoryDto>> response = inventoryController.getAllInventory();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(inventoryService, times(1)).getAllInventory();
    }

    @Test
    void testGetInventoryById() {
        when(inventoryService.getInventoryById(1)).thenReturn(inventoryDto);

        ResponseEntity<InventoryDto> response = inventoryController.getInventoryById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getInventoryId());
        verify(inventoryService, times(1)).getInventoryById(1);
    }

    @Test
    void testGetByFilm() {
        List<InventoryDto> list = Arrays.asList(inventoryDto);
        when(inventoryService.getInventoryByFilm(1)).thenReturn(list);

        ResponseEntity<List<InventoryDto>> response = inventoryController.getByFilm(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(inventoryService, times(1)).getInventoryByFilm(1);
    }

    @Test
    void testGetByStore() {
        List<InventoryDto> list = Arrays.asList(inventoryDto);
        when(inventoryService.getInventoryByStore(1)).thenReturn(list);

        ResponseEntity<List<InventoryDto>> response = inventoryController.getByStore(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(inventoryService, times(1)).getInventoryByStore(1);
    }

    @Test
    void testGetAvailableInventory() {
        List<InventoryDto> list = Arrays.asList(inventoryDto);
        when(inventoryService.getAvailableInventory(1, 1)).thenReturn(list);

        ResponseEntity<List<InventoryDto>> response = inventoryController.getAvailableInventory(1, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(inventoryService, times(1)).getAvailableInventory(1, 1);
    }

    @Test
    void testAddInventory() {
        when(inventoryService.addInventory(any(InventoryDto.class))).thenReturn(inventoryDto);

        ResponseEntity<InventoryDto> response = inventoryController.addInventory(inventoryDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(inventoryService, times(1)).addInventory(any(InventoryDto.class));
    }

    @Test
    void testDeleteInventory() {
        doNothing().when(inventoryService).deleteInventory(1);

        ResponseEntity<Void> response = inventoryController.deleteInventory(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(inventoryService, times(1)).deleteInventory(1);
    }
}
