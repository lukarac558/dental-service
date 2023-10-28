package com.student.reservationservice.common.exception.entity;

public class IncorrectValueException extends RuntimeException {
    public IncorrectValueException(String message) {
        super(message);
    }
}
