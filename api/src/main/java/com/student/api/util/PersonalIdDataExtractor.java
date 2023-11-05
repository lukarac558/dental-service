package com.student.api.util;

import com.student.api.dto.common.enums.Sex;
import com.student.api.exception.IncorrectValueException;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import static com.student.api.dto.common.enums.Sex.FEMALE;
import static com.student.api.dto.common.enums.Sex.MALE;
import static com.student.api.exception.handler.ErrorConstants.INCORRECT_PESEL_MESSAGE;

public class PersonalIdDataExtractor {
    private PersonalIdDataExtractor() {}

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static Sex getSex(String personalId) {
        try {
            return Integer.parseInt(personalId.substring(9, 10)) % 2 == 0 ? FEMALE : MALE;
        } catch (Exception e) {
            throw new IncorrectValueException(String.format(INCORRECT_PESEL_MESSAGE, personalId));
        }
    }

    public static LocalDate getBirthDate(String personalId) {
        try {
            int centuryCode = Integer.parseInt(personalId.substring(2, 4)) / 20;
            return LocalDate.of(
                    Integer.parseInt(personalId.substring(0, 2)) + switch (centuryCode) {
                        case 0 -> 1900;
                        case 1 -> 2000;
                        case 2 -> 2100;
                        case 3 -> 2200;
                        case 4 -> 1800;
                        default -> throw new IllegalStateException("Unexpected value: " + centuryCode);
                    },
                    Integer.parseInt(personalId.substring(2, 4)) % 20,
                    Integer.parseInt(personalId.substring(4, 6))
            );
        } catch (Exception e) {
            throw new IncorrectValueException(String.format(INCORRECT_PESEL_MESSAGE, personalId));
        }
    }

    public static String getBirthDate(String personalId, DateTimeFormatter formatter) {
        return getBirthDate(personalId).format(formatter);
    }

    public static Integer getAge(String personalId) {
        return Period.between(getBirthDate(personalId), LocalDate.now()).getYears();
    }

}
