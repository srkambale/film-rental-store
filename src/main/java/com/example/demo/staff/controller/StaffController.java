package com.example.demo.staff.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.example.demo.staff.entity.Staff;
import com.example.demo.staff.service.StaffService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {

    private final StaffService service;

    public StaffController(StaffService service) {
        this.service = service;
    }

    @GetMapping
    public List<Staff> getAllStaff() {
        return service.getAllStaff();
    }

    @GetMapping("/{id}")
    public Staff getStaffById(@PathVariable Integer id) {
        return service.getStaffById(id);
    }

    @PostMapping
    public Staff createStaff(@Valid @RequestBody Staff staff) {
        return service.createStaff(staff);
    }

    @PutMapping("/{id}")
    public Staff updateStaff(@PathVariable Integer id, @Valid @RequestBody Staff staff) {
        return service.updateStaff(id, staff);
    }

    @DeleteMapping("/{id}")
    public void deleteStaff(@PathVariable Integer id) {
        service.deleteStaff(id);
    }
}
