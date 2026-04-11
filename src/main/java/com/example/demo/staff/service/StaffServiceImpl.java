package com.example.demo.staff.service;

import org.springframework.stereotype.Service;

import com.example.demo.staff.entity.Staff;
import com.example.demo.staff.exception.ResourceNotFoundException;
import com.example.demo.staff.repository.StaffRepository;

import java.util.List;

@Service
public class StaffServiceImpl {

    private final StaffRepository repository;

    public StaffServiceImpl(StaffRepository repository) {
        this.repository = repository;
    }

    // 🔹 Get all staff
    public List<Staff> getAllStaff() {
        return repository.findAll();
    }

    // 🔹 Get by ID
    public Staff getStaffById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
    }

    // 🔹 Create
    public Staff createStaff(Staff staff) {
        return repository.save(staff);
    }

    // 🔹 Update
    public Staff updateStaff(Integer id, Staff updatedStaff) {
        Staff existing = getStaffById(id);

        existing.setFirstName(updatedStaff.getFirstName());
        existing.setLastName(updatedStaff.getLastName());
        existing.setEmail(updatedStaff.getEmail());
        existing.setUsername(updatedStaff.getUsername());
        existing.setPassword(updatedStaff.getPassword());
        existing.setPhone(updatedStaff.getPhone());
        existing.setRole(updatedStaff.getRole());
        existing.setActive(updatedStaff.getActive());
        existing.setStoreId(updatedStaff.getStoreId());
        existing.setAddressId(updatedStaff.getAddressId());

        return repository.save(existing);
    }

    // 🔹 Delete
    public void deleteStaff(Integer id) {
        Staff staff = getStaffById(id);
        repository.delete(staff);
    }
}
