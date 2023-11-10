package com.student.api.exception;

import org.springframework.http.HttpStatus;

public class ApprovalForbiddenException extends DentalClinicException {

    public ApprovalForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
