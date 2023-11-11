package com.student.api.util;

import com.student.api.exception.IncorrectFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Time;

class TimeFormatParserTest {
    private static final String CORRECT_TIME_WITH_SECONDS = "08:00:00";
    private static final String CORRECT_TIME_WITHOUT_SECONDS = "8:00";
    private static final String INCORRECT_TIME = "8a:00";

    @Test
    void shouldValidTime_correctTimeWithSeconds() {
        Time time = TimeFormatParser.parseOrThrow(CORRECT_TIME_WITH_SECONDS);
        Assertions.assertEquals(CORRECT_TIME_WITH_SECONDS, time.toString());
    }

    @Test
    void shouldValidTime_correctTimeWithoutSeconds() {
        Time time = TimeFormatParser.parseOrThrow(CORRECT_TIME_WITHOUT_SECONDS);
        Assertions.assertEquals(CORRECT_TIME_WITH_SECONDS, time.toString());
    }

    @Test
    void shouldNotValidTime_incorrectTime_letterInTime() {
        Assertions.assertThrows(IncorrectFormatException.class, () -> TimeFormatParser.parseOrThrow(INCORRECT_TIME));
    }
}
