package com.student.api.annotation.validator.phone;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhoneValidatorTests {
    private final PhoneValidator phoneValidator = new PhoneValidator();

    @ParameterizedTest
    @CsvSource({
            "600600600,true",
            "+48603603603,true",
            "600600600t,false",
            "t500500500,false",
            "-50604604604,false"
    })
    void testPostalCodeValidator(String phone, boolean isValid) {
        assertEquals(isValid, phoneValidator.isValid(phone, null));
    }
}
