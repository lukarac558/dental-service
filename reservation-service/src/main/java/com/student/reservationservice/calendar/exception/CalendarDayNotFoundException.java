package com.student.reservationservice.calendar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CalendarDayNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Calendar day with id: %s was not found.";

    public CalendarDayNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
