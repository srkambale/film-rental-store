package com.example.demo.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomerResourceNotFoundException extends com.example.demo.exception.ResourceNotFoundException {
    public CustomerResourceNotFoundException(String message) {
        super(message);
    }
}
