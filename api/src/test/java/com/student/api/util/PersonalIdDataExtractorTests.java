package com.student.api.util;

import com.student.api.dto.common.enums.Sex;
import com.student.api.exception.IncorrectValueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonalIdDataExtractorTests {

    @ParameterizedTest
    @CsvSource({
            "19033179313,MALE",
            "71123161886,FEMALE",
            "63063067195,MALE",
            "63873188149,FEMALE"
    })
    void testSexExtraction(String personalId, Sex sex) {
        assertEquals(sex, PersonalIdDataExtractor.getSex(personalId));
    }

    @Test
    void testSexExtractionShouldThrow() {
        assertThrows(IncorrectValueException.class, () -> PersonalIdDataExtractor.getSex("638731881t9"));
    }

    @ParameterizedTest
    @CsvSource({
            "19033179313,1919-03-31",
            "71123161886,1971-12-31",
            "63063067195,1963-06-30",
            "63873188149,1863-07-31"
    })
    void testBirthDayExtraction(String personalId, String birthDay) {
        assertEquals(birthDay, PersonalIdDataExtractor.getBirthDate(personalId, PersonalIdDataExtractor.FORMATTER));
    }

    @Test
    void testBirthDayExtractionShouldThrow() {
        assertThrows(IncorrectValueException.class, () -> PersonalIdDataExtractor.getBirthDate("63t731881t9", PersonalIdDataExtractor.FORMATTER));
    }

    @ParameterizedTest
    @CsvSource({
            "19033179313,1919-03-31",
            "71123161886,1971-12-31",
            "63063067195,1963-06-30",
            "63873188149,1863-07-31"
    })
    void testAgeExtraction(String personalId, String birthDay) {
        assertEquals(Period.between(LocalDate.parse(birthDay), LocalDate.now()).getYears(), PersonalIdDataExtractor.getAge(personalId));
    }

    @Test
    void testAgeExtractionShouldThrow() {
        assertThrows(IncorrectValueException.class, () -> PersonalIdDataExtractor.getAge("63t731881t9"));
    }

}
