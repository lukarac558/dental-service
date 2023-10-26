package com.student.reservationservice.user.applicationuser.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class IncorrectEmailOrPasswordException extends RuntimeException {
     private static final String MESSAGE = "Provided login or password is incorrect.";

     public IncorrectEmailOrPasswordException() {
         super(MESSAGE);
     }
}
