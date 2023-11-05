package com.student.api.exception;

import org.springframework.http.HttpStatus;

public class CancellationForbiddenException extends DentalClinicException {
    public CancellationForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
