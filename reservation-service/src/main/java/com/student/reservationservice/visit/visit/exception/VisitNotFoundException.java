package com.student.reservationservice.visit.visit.exception;

public class VisitNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Visit with id: %s was not found.";

    public VisitNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
