package com.example.demo.admin.controller;

import com.example.demo.admin.dto.AdminStaffRequest;
import com.example.demo.admin.service.AdminStaffService;
import com.example.demo.staff.entity.Staff;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://10.30.74.131:8082")
@RestController
@RequestMapping("/api/v1/admin/staff")
public class AdminStaffController {

    private final AdminStaffService adminStaffService;

    public AdminStaffController(AdminStaffService adminStaffService) {
        this.adminStaffService = adminStaffService;
    }

    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        return ResponseEntity.ok(adminStaffService.getAllStaff());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Integer id) {
        return ResponseEntity.ok(adminStaffService.getStaffById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Staff>> searchStaffByName(@RequestParam String name) {
        return ResponseEntity.ok(adminStaffService.searchByName(name));
    }

    @PostMapping
    public ResponseEntity<Staff> createStaff(@Valid @RequestBody AdminStaffRequest request) {
        Staff createdStaff = adminStaffService.createStaff(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStaff);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Staff> updateStaff(@PathVariable Integer id, @Valid @RequestBody AdminStaffRequest request) {
        Staff updatedStaff = adminStaffService.updateStaff(id, request);
        return ResponseEntity.ok(updatedStaff);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Integer id) {
        adminStaffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}
