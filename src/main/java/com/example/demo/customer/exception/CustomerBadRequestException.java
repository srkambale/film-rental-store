package com.example.demo.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomerBadRequestException extends com.example.demo.exception.BadRequestException {
    public CustomerBadRequestException(String message) {
        super(message);
    }
}
