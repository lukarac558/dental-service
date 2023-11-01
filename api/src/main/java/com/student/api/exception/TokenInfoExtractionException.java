package com.student.api.exception;

import org.springframework.http.HttpStatus;

public class TokenInfoExtractionException extends DentalClinicException {
    public TokenInfoExtractionException(String message, HttpStatus status) {
        super(message, status);
    }
}
