package com.student.reservationservice.user.address.city.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class CityCodeExistsException extends RuntimeException {
    private static final String MESSAGE = "City with code: %s already exists.";

    public CityCodeExistsException(String name) {
        super(String.format(MESSAGE, name));
    }
}
