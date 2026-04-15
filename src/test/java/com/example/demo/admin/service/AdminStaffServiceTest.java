package com.example.demo.admin.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.admin.dto.AdminStaffRequest;
import com.example.demo.admin.repository.AdminStaffRepository;
import com.example.demo.auth.entity.UserAuth;
import com.example.demo.auth.repository.UserAuthRepository;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.staff.entity.Staff;
import com.example.demo.staff.repository.StaffRepository;

@ExtendWith(MockitoExtension.class)
class AdminStaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private UserAuthRepository userAuthRepository;

    @Mock
    private AdminStaffRepository adminStaffRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminStaffService adminStaffService;

    private Staff staff;
    private AdminStaffRequest request;

    @BeforeEach
    void setUp() {
        staff = new Staff();
        staff.setStaffId(1);
        staff.setFirstName("JOHN");
        staff.setLastName("DOE");
        staff.setUsername("johndoe");
        staff.setEmail("john@example.com");

        request = new AdminStaffRequest();
        request.setFirstName("JOHN");
        request.setLastName("DOE");
        request.setEmail("john@example.com");
        request.setUsername("johndoe");
        request.setPassword("password");
        request.setRole("STAFF");
        request.setStoreId(1);
        request.setActive(true);
    }

    @Test
    void testGetAllStaff() {
        when(staffRepository.findAll()).thenReturn(List.of(staff));

        List<Staff> result = adminStaffService.getAllStaff();

        assertEquals(1, result.size());
        verify(staffRepository).findAll();
    }

    @Test
    void testGetStaffById_Success() {
        when(staffRepository.findById(1)).thenReturn(Optional.of(staff));

        Staff result = adminStaffService.getStaffById(1);

        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
    }

    @Test
    void testCreateStaff_Success() {
        when(userAuthRepository.existsByUsername(anyString())).thenReturn(false);
        when(userAuthRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(staffRepository.save(any(Staff.class))).thenReturn(staff);

        Staff result = adminStaffService.createStaff(request);

        assertNotNull(result);
        verify(staffRepository).save(any(Staff.class));
        verify(userAuthRepository).save(any(UserAuth.class));
    }

    @Test
    void testCreateStaff_DuplicateUsername() {
        when(userAuthRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> adminStaffService.createStaff(request));
    }

    @Test
    void testUpdateStaff_Success() {
        UserAuth userAuth = new UserAuth();
        userAuth.setUsername("johndoe");
        userAuth.setRole(UserAuth.Role.STAFF);

        when(staffRepository.findById(1)).thenReturn(Optional.of(staff));
        when(userAuthRepository.findByUsername(anyString())).thenReturn(Optional.of(userAuth));
        when(staffRepository.save(any(Staff.class))).thenReturn(staff);

        Staff result = adminStaffService.updateStaff(1, request);

        assertNotNull(result);
        verify(staffRepository).save(any(Staff.class));
    }

    @Test
    void testDeleteStaff_Success() {
        when(staffRepository.findById(1)).thenReturn(Optional.of(staff));
        when(userAuthRepository.findByUsername(anyString())).thenReturn(Optional.of(new UserAuth()));

        adminStaffService.deleteStaff(1);

        verify(userAuthRepository).delete(any(UserAuth.class));
        verify(staffRepository).delete(any(Staff.class));
    }
}
