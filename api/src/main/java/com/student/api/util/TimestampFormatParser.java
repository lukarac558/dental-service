package com.student.api.util;

import com.student.api.exception.IncorrectFormatException;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.student.api.exception.handler.ErrorConstants.INCORRECT_TIMESTAMP_FORMAT_MESSAGE;

public class TimestampFormatParser {
    private TimestampFormatParser(){}
    private static final List<SimpleDateFormat> allowedTimestampFormats = List.of(
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    );

    public static Timestamp parse(String timeString) {
        for (SimpleDateFormat format : allowedTimestampFormats) {
            Optional<Timestamp> timestamp = parse(format, timeString);
            if (timestamp.isPresent()) {
                return timestamp.get();
            }
        }
        throw new IncorrectFormatException(INCORRECT_TIMESTAMP_FORMAT_MESSAGE);
    }

    private static Optional<Timestamp> parse(SimpleDateFormat format, String timeString) {
        try {
            Date date = format.parse(timeString);
            return Optional.of(new Timestamp(date.getTime()));
        } catch (ParseException e) {
            return Optional.empty();
        }
    }
}
