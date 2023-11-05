package com.student.api.exception;

import org.springframework.http.HttpStatus;

public class IncorrectValueException extends DentalClinicException {
    public IncorrectValueException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
