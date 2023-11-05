package com.student.api.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends DentalClinicException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
