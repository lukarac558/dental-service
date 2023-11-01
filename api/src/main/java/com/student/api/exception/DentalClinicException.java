package com.student.api.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class DentalClinicException extends RuntimeException {
    private HttpStatus status;

    public DentalClinicException(String message) {
        super(message);
    }

    public DentalClinicException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public DentalClinicException(String message, Throwable cause) {
        super(message, cause);
    }

    public DentalClinicException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }
}
