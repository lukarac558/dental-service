package com.student.api.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class DentalClinicException extends RuntimeException {
    protected final HttpStatus status;

    public DentalClinicException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public DentalClinicException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public DentalClinicException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public DentalClinicException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }
}
