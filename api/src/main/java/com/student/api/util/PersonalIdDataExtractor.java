package com.student.api.util;

import com.student.api.dto.common.enums.Sex;
import com.student.api.exception.IncorrectValueException;

import static com.student.api.dto.common.enums.Sex.FEMALE;
import static com.student.api.dto.common.enums.Sex.MALE;
import static com.student.api.exception.handler.ErrorConstants.INCORRECT_PESEL_MESSAGE;

public class PersonalIdDataExtractor {
    private PersonalIdDataExtractor() {}

    public static Sex sexFromPersonalId(String personalId) {
        if(personalId.length()<11) {
            throw new IncorrectValueException(INCORRECT_PESEL_MESSAGE);
        }
        return personalId.charAt(9) % 2 == 0 ? FEMALE : MALE;
    }

}
