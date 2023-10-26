package com.student.reservationservice.user.address.city.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CityNotFoundException extends RuntimeException {
    private static final String MESSAGE = "City with code: %s was not found.";

    public CityNotFoundException(String code) {
        super(String.format(MESSAGE, code));
    }
}
