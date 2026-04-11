package com.example.demo.customer.service;

import com.example.demo.customer.dto.RentalRequestDto;
import com.example.demo.customer.dto.RentalResponseDto;
import java.util.List;

public interface CustomerRentalService {

    RentalResponseDto createRental(RentalRequestDto request);

    List<RentalResponseDto> getRentalsByCustomer(Long customerId);
}
