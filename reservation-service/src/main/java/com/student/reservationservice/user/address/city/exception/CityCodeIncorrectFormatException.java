package com.student.reservationservice.user.address.city.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class CityCodeIncorrectFormatException extends RuntimeException {
    private static final String VALID_FORMAT = "##-###";
    private static final String MESSAGE = "Given city code: %s is incorrect. Valid format is: %s";

    public CityCodeIncorrectFormatException(String code) {
        super(String.format(MESSAGE, code, VALID_FORMAT));
    }
}
