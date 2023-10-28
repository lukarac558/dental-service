package com.student.reservationservice.common.exception.entity;

public class CancellationForbiddenException extends RuntimeException {
    public CancellationForbiddenException(String message) {
        super(message);
    }
}
