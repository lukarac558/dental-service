package com.student.reservationservice.user.address.voivodeship.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class VoivodeshipNotFoundException extends RuntimeException {
    private static final String NOT_FOUND_BY_NAME_MESSAGE = "Voivodeship with name: %s was not found.";
    private static final String NOT_FOUND_BY_ID_MESSAGE = "Voivodeship with id: %s was not found.";

    public VoivodeshipNotFoundException(int id) {
        super(String.format(NOT_FOUND_BY_ID_MESSAGE, id));
    }

    public VoivodeshipNotFoundException(String name) {
        super(String.format(NOT_FOUND_BY_NAME_MESSAGE, name));
    }
}
