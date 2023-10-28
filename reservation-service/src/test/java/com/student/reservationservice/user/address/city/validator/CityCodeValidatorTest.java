package com.student.reservationservice.user.address.city.validator;

import com.student.reservationservice.user.address.city.utils.CityCodeValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CityCodeValidatorTest {

    private static final String CORRECT_CODE = "44-300";
    private static final String CODE_WITH_FORBIDDEN_LETTER = "X1-300";
    private static final String CODE_WITH_INCORRECT_LENGTH = "44-3000";

    @Test
    void shouldValidCityCode_correctCode() {
        Assertions.assertTrue(CityCodeValidator.isValid(CORRECT_CODE));
    }

    @Test
    void shouldNotValidCityCode_forbiddenLetterWasGiven() {
        Assertions.assertFalse(CityCodeValidator.isValid(CODE_WITH_FORBIDDEN_LETTER));
    }

    @Test
    void shouldNotValidCityCode_incorrectCode_tooLongCodeWasGiven() {
        Assertions.assertFalse(CityCodeValidator.isValid(CODE_WITH_INCORRECT_LENGTH));
    }
}
