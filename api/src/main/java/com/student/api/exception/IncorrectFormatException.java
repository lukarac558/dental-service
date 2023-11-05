package com.student.api.exception;

import org.springframework.http.HttpStatus;

public class IncorrectFormatException extends DentalClinicException{
    public IncorrectFormatException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
