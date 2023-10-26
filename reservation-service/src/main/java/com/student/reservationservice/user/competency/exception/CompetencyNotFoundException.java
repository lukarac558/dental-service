package com.student.reservationservice.user.competency.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CompetencyNotFoundException extends RuntimeException {
    private static final String MESSAGE = "User competency with id: %s was not found.";

    public CompetencyNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
