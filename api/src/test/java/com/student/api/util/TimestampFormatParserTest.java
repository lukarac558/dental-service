package com.student.api.util;

import com.student.api.exception.IncorrectFormatException;
import com.student.api.util.TimestampFormatParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

class TimestampFormatParserTest {
    private static final String CORRECT_TIMESTAMP = "2023-10-27 08:00:00.0";
    private static final String CORRECT_TIMESTAMP_WITH_MS = "2023-10-27 08:00:00.000";
    private static final String CORRECT_TIMESTAMP_WITHOUT_MS = "2023-10-27 08:00:00";
    private static final String INCORRECT_TIMESTAMP = "2023-12-1a 08:00:00";

    @Test
    void shouldValidTime_correctTimeWithSeconds() {
        Timestamp timestamp = TimestampFormatParser.parse(CORRECT_TIMESTAMP_WITH_MS);
        Assertions.assertEquals(CORRECT_TIMESTAMP, timestamp.toString());
    }

    @Test
    void shouldValidTime_correctTimeWithoutSeconds() {
        Timestamp timestamp = TimestampFormatParser.parse(CORRECT_TIMESTAMP_WITHOUT_MS);
        Assertions.assertEquals(CORRECT_TIMESTAMP, timestamp.toString());
    }

    @Test
    void shouldNotValidTimestamp_incorrectTime_letterInDay() {
        Assertions.assertThrows(IncorrectFormatException.class, () -> TimestampFormatParser.parse(INCORRECT_TIMESTAMP));
    }
}
