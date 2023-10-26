package com.student.reservationservice.user.applicationuser.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class PeselIncorrectFormatException extends RuntimeException {
    private static final String MESSAGE = "Given PESEL: %s is incorrect. Valid PESEL should consist of 11 digits.";

    public PeselIncorrectFormatException(Long pesel) {
        super(String.format(MESSAGE, pesel));
    }
}
