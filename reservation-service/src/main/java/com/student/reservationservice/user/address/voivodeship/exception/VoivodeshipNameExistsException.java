package com.student.reservationservice.user.address.voivodeship.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class VoivodeshipNameExistsException extends RuntimeException {
    private static final String MESSAGE = "Voivodeship with name: %s already exists.";

    public VoivodeshipNameExistsException(String name) {
        super(String.format(MESSAGE, name));
    }
}
