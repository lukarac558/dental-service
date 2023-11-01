package com.student.reservationservice.common.utils;

import com.student.api.exception.IncorrectFormatException;
import com.student.api.util.TimeFormatParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Time;

class TimeFormatParserTest {
    private static final String CORRECT_TIME_WITH_SECONDS = "08:00:00";
    private static final String CORRECT_TIME_WITHOUT_SECONDS = "8:00";
    private static final String INCORRECT_TIME = "8a:00";

    @Test
    void shouldValidTime_correctTimeWithSeconds() {
        Time time = TimeFormatParser.parse(CORRECT_TIME_WITH_SECONDS);
        Assertions.assertEquals(CORRECT_TIME_WITH_SECONDS, time.toString());
    }

    @Test
    void shouldValidTime_correctTimeWithoutSeconds() {
        Time time = TimeFormatParser.parse(CORRECT_TIME_WITHOUT_SECONDS);
        Assertions.assertEquals(CORRECT_TIME_WITH_SECONDS, time.toString());
    }

    @Test
    void shouldNotValidTime_incorrectTime_letterInTime() {
        Assertions.assertThrows(IncorrectFormatException.class, () -> TimeFormatParser.parse(INCORRECT_TIME));
    }
}
