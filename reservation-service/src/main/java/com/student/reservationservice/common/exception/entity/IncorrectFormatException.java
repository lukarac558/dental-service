package com.student.reservationservice.common.exception.entity;

public class IncorrectFormatException extends RuntimeException{
    public IncorrectFormatException(String message) {
        super(message);
    }
}
