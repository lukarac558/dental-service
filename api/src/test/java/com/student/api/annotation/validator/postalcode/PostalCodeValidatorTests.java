package com.student.api.annotation.validator.postalcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostalCodeValidatorTests {
    private final PostalCodeValidator postalCodeValidator = new PostalCodeValidator();

    @ParameterizedTest
    @CsvSource({
            "44-100,true",
            "44-1000,false",
            "44-100t,false",
            "44 100,false",
            "44-300,true",
            "X1-300,false",
            "44-3000,false"
    })
    void testPostalCodeValidator(String postalCode, boolean isValid) {
        assertEquals(isValid, postalCodeValidator.isValid(postalCode, null));
    }
}
