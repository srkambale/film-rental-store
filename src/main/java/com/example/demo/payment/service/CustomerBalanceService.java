package com.example.demo.payment.service;

import com.example.demo.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CustomerBalanceService {

    private final CustomerRepository customerRepository;

    public CustomerBalanceService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public BigDecimal getCustomerBalance(Long customerId) {
        return customerRepository.getCustomerBalance(customerId);
    }
}