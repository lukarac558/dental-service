package com.student.reservationservice.lock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class VisitLockExistsException extends RuntimeException {
    private static final String MESSAGE = "Given start date to docot is already reserved.";

    public VisitLockExistsException() {
        super(MESSAGE);
    }
}
