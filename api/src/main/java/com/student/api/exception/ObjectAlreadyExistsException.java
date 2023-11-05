package com.student.api.exception;

import org.springframework.http.HttpStatus;

public class ObjectAlreadyExistsException extends DentalClinicException {
    public ObjectAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
