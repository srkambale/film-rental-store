package com.example.demo.staff.controller;

import com.example.demo.staff.entity.Staff;
import com.example.demo.staff.service.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StaffControllerTest {

    @Mock
    private StaffService staffService;

    @InjectMocks
    private StaffController staffController;

    private Staff staff;

    @BeforeEach
    void setUp() {
        staff = new Staff();
        staff.setStaffId(1);
        staff.setFirstName("John");
        staff.setLastName("Doe");
        staff.setEmail("john.doe@example.com");
    }

    @Test
    void testGetAllStaff() {
        List<Staff> staffList = Arrays.asList(staff);
        when(staffService.getAllStaff()).thenReturn(staffList);

        List<Staff> result = staffController.getAllStaff();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(staffService, times(1)).getAllStaff();
    }

    @Test
    void testGetStaffById() {
        when(staffService.getStaffById(1)).thenReturn(staff);

        Staff result = staffController.getStaffById(1);

        assertNotNull(result);
        assertEquals(1, result.getStaffId());
        assertEquals("John", result.getFirstName());
        verify(staffService, times(1)).getStaffById(1);
    }

    @Test
    void testCreateStaff() {
        when(staffService.createStaff(any(Staff.class))).thenReturn(staff);

        Staff result = staffController.createStaff(staff);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(staffService, times(1)).createStaff(any(Staff.class));
    }

    @Test
    void testUpdateStaff() {
        when(staffService.updateStaff(eq(1), any(Staff.class))).thenReturn(staff);

        Staff result = staffController.updateStaff(1, staff);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(staffService, times(1)).updateStaff(eq(1), any(Staff.class));
    }

    @Test
    void testDeleteStaff() {
        doNothing().when(staffService).deleteStaff(1);

        staffController.deleteStaff(1);

        verify(staffService, times(1)).deleteStaff(1);
    }
}
