package com.student.reservationservice.user.applicationuser.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    private static final String MESSAGE = "User with id: %s was not found.";

    public UserNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
