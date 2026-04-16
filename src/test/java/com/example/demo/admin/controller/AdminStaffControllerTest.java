package com.example.demo.admin.controller;

import com.example.demo.admin.dto.AdminStaffRequest;
import com.example.demo.admin.service.AdminStaffService;
import com.example.demo.staff.entity.Staff;
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
public class AdminStaffControllerTest {

    @Mock
    private AdminStaffService adminStaffService;

    @InjectMocks
    private AdminStaffController adminStaffController;

    private Staff staff;
    private AdminStaffRequest staffRequest;

    @BeforeEach
    void setUp() {
        staff = new Staff();
        staff.setStaffId(1);
        staff.setFirstName("Admin");
        staff.setLastName("User");

        staffRequest = new AdminStaffRequest();
        staffRequest.setFirstName("Admin");
        staffRequest.setLastName("User");
        staffRequest.setEmail("admin@example.com");
        staffRequest.setUsername("adminuser");
        staffRequest.setPassword("password");
        staffRequest.setStoreId(1);
        staffRequest.setRole("ADMIN");
    }

    @Test
    void testGetAllStaff() {
        List<Staff> list = Arrays.asList(staff);
        when(adminStaffService.getAllStaff()).thenReturn(list);

        ResponseEntity<List<Staff>> response = adminStaffController.getAllStaff();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(adminStaffService, times(1)).getAllStaff();
    }

    @Test
    void testGetStaffById() {
        when(adminStaffService.getStaffById(1)).thenReturn(staff);

        ResponseEntity<Staff> response = adminStaffController.getStaffById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getStaffId());
        verify(adminStaffService, times(1)).getStaffById(1);
    }

    @Test
    void testSearchStaffByName() {
        List<Staff> list = Arrays.asList(staff);
        when(adminStaffService.searchByName("Admin")).thenReturn(list);

        ResponseEntity<List<Staff>> response = adminStaffController.searchStaffByName("Admin");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(adminStaffService, times(1)).searchByName("Admin");
    }

    @Test
    void testCreateStaff() {
        when(adminStaffService.createStaff(any(AdminStaffRequest.class))).thenReturn(staff);

        ResponseEntity<Staff> response = adminStaffController.createStaff(staffRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(adminStaffService, times(1)).createStaff(any(AdminStaffRequest.class));
    }

    @Test
    void testUpdateStaff() {
        when(adminStaffService.updateStaff(eq(1), any(AdminStaffRequest.class))).thenReturn(staff);

        ResponseEntity<Staff> response = adminStaffController.updateStaff(1, staffRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(adminStaffService, times(1)).updateStaff(eq(1), any(AdminStaffRequest.class));
    }

    @Test
    void testDeleteStaff() {
        doNothing().when(adminStaffService).deleteStaff(1);

        ResponseEntity<Void> response = adminStaffController.deleteStaff(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(adminStaffService, times(1)).deleteStaff(1);
    }
}
