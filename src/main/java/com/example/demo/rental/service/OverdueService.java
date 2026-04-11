package com.example.demo.rental.service;

import org.springframework.stereotype.Service;

import com.example.demo.rental.dto.RentalSummaryDto;
import com.example.demo.rental.entity.Rental;
import com.example.demo.rental.repository.RentalRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OverdueService {

    private static final int OVERDUE_DAYS = 7;

    private final RentalRepository rentalRepository;

    public OverdueService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    // ✅ Get overdue rentals
    public List<RentalSummaryDto> getOverdueRentals() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(OVERDUE_DAYS);

        return rentalRepository.findOverdueRentals(cutoff)
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    // ✅ Custom days
    public List<RentalSummaryDto> getOverdueRentals(int days) {
        if (days <= 0) days = OVERDUE_DAYS;

        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);

        return rentalRepository.findOverdueRentals(cutoff)
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    // ✅ Count
    public long countOverdueRentals() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(OVERDUE_DAYS);
        return rentalRepository.findOverdueRentals(cutoff).size();
    }

    // ✅ FIXED MAPPER
    private RentalSummaryDto toSummary(Rental r) {
        return new RentalSummaryDto(
                r.getRentalId(),
                r.getCustomer() != null && r.getCustomer().getCustomerId() != null 
                    ? r.getCustomer().getCustomerId().intValue() : null, // ✅ FIXED
                r.getInventory() != null ? r.getInventory().getInventoryId() : null, // safer
                r.getRentalDate(),
                r.getReturnDate()
        );
    }
}