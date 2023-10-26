package com.student.reservationservice.servicetype.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ServiceTypeNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Service type with id: %s was not found.";

    public ServiceTypeNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
