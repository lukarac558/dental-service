package com.student.reservationservice.user.applicationuser.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class EmailIncorrectFormatException extends RuntimeException {
    private static final String MESSAGE = "Email: %s is incorrect.";

    public EmailIncorrectFormatException(String email) {
        super(String.format(MESSAGE, email));
    }
}
