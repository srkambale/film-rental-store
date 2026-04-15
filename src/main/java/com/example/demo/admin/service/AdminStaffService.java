package com.example.demo.admin.service;

import com.example.demo.admin.dto.AdminStaffRequest;
import com.example.demo.admin.repository.AdminStaffRepository;
import com.example.demo.auth.entity.UserAuth;
import com.example.demo.auth.repository.UserAuthRepository;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.BadRequestException;
import com.example.demo.staff.entity.Staff;
import com.example.demo.staff.repository.StaffRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminStaffService {

    private final StaffRepository staffRepository;
    private final UserAuthRepository userAuthRepository;
    private final AdminStaffRepository adminStaffRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminStaffService(StaffRepository staffRepository,
                             UserAuthRepository userAuthRepository,
                             AdminStaffRepository adminStaffRepository,
                             PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.userAuthRepository = userAuthRepository;
        this.adminStaffRepository = adminStaffRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Staff getStaffById(Integer id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
    }

    public List<Staff> searchByName(String name) {
        return adminStaffRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    @Transactional
    public Staff createStaff(AdminStaffRequest request) {
        // 1. Validation
        if (userAuthRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists: " + request.getUsername());
        }
        if (userAuthRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists: " + request.getEmail());
        }

        UserAuth.Role authRole;
        try {
            authRole = UserAuth.Role.valueOf(request.getRole().toUpperCase());
            if (authRole == UserAuth.Role.CUSTOMER) {
                throw new BadRequestException("Cannot create staff with CUSTOMER role");
            }
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + request.getRole());
        }

        // 2. Create Staff entity
        Staff staff = new Staff();
        staff.setFirstName(request.getFirstName());
        staff.setLastName(request.getLastName());
        staff.setEmail(request.getEmail());
        staff.setUsername(request.getUsername());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setStoreId(request.getStoreId());
        staff.setAddressId(1); // Default as requested
        staff.setActive(request.getActive());
        staff.setPhone(request.getPhone());
        staff.setRole(authRole.name());

        Staff savedStaff = staffRepository.save(staff);

        // 3. Create UserAuth entity
        UserAuth userAuth = new UserAuth();
        userAuth.setUsername(request.getUsername());
        userAuth.setEmail(request.getEmail());
        userAuth.setPassword(passwordEncoder.encode(request.getPassword()));
        userAuth.setRole(authRole);
        userAuth.setRefId(savedStaff.getStaffId());

        userAuthRepository.save(userAuth);

        return savedStaff;
    }

    @Transactional
    public Staff updateStaff(Integer id, AdminStaffRequest request) {
        Staff existingStaff = getStaffById(id);
        
        // Find existing UserAuth
        UserAuth userAuth = userAuthRepository.findByUsername(existingStaff.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("UserAuth not found for staff: " + existingStaff.getUsername()));

        // Validate unique constraints if changed
        if (!existingStaff.getUsername().equals(request.getUsername()) && 
            userAuthRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already taken: " + request.getUsername());
        }

        UserAuth.Role authRole;
        try {
            authRole = UserAuth.Role.valueOf(request.getRole().toUpperCase());
            if (authRole == UserAuth.Role.CUSTOMER) {
                throw new BadRequestException("Cannot update staff to CUSTOMER role");
            }
            // User requested no role promotion to ADMIN if not already? 
            // Actually they said "no" to "Should the admin be allowed to change a regular staff member's role to ADMIN?"
            if (authRole == UserAuth.Role.ADMIN && userAuth.getRole() != UserAuth.Role.ADMIN) {
                throw new BadRequestException("Role promotion to ADMIN is not allowed via this service");
            }
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + request.getRole());
        }

        // Update Staff
        existingStaff.setFirstName(request.getFirstName());
        existingStaff.setLastName(request.getLastName());
        existingStaff.setEmail(request.getEmail());
        existingStaff.setUsername(request.getUsername());
        existingStaff.setStoreId(request.getStoreId());
        existingStaff.setActive(request.getActive());
        existingStaff.setPhone(request.getPhone());
        existingStaff.setRole(authRole.name());
        
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            String encoded = passwordEncoder.encode(request.getPassword());
            existingStaff.setPassword(encoded);
            userAuth.setPassword(encoded);
        }

        // Update UserAuth
        userAuth.setUsername(request.getUsername());
        userAuth.setEmail(request.getEmail());
        userAuth.setRole(authRole);

        userAuthRepository.save(userAuth);
        return staffRepository.save(existingStaff);
    }

    @Transactional
    public void deleteStaff(Integer id) {
        Staff staff = getStaffById(id);
        
        // Delete UserAuth record first (as requested)
        userAuthRepository.findByUsername(staff.getUsername())
                .ifPresent(userAuthRepository::delete);

        staffRepository.delete(staff);
    }
}
