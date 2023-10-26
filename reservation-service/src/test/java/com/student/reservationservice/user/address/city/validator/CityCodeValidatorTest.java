package com.student.reservationservice.user.address.city.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CityCodeValidatorTest {

    private static final String CORRECT_CODE = "44-300";
    private static final String CODE_WITH_FORBIDDEN_LETTER = "X1-300";
    private static final String CODE_WITH_INCORRECT_LENGTH = "44-3000";

    private final CityCodeValidator cityCodeValidator = new CityCodeValidator();

    @Test
    void shouldValidCityCode_correctCode() {
        Assertions.assertTrue(cityCodeValidator.isCityCodeValid(CORRECT_CODE));
    }

    @Test
    void shouldNotValidCityCode_forbiddenLetterWasGiven() {
        Assertions.assertFalse(cityCodeValidator.isCityCodeValid(CODE_WITH_FORBIDDEN_LETTER));
    }

    @Test
    void shouldNotValidCityCode_incorrectCode_tooLongCodeWasGiven() {
        Assertions.assertFalse(cityCodeValidator.isCityCodeValid(CODE_WITH_INCORRECT_LENGTH));
    }
}
