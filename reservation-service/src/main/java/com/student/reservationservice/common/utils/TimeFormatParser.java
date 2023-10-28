package com.student.reservationservice.common.utils;

import com.student.reservationservice.common.exception.entity.IncorrectFormatException;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.student.reservationservice.common.exception.entity.ErrorConstants.INCORRECT_TIME_FORMAT_MESSAGE;

public class TimeFormatParser {
    private static final List<SimpleDateFormat> allowedTimeFormats = List.of(
            new SimpleDateFormat("HH:mm:ss"),
            new SimpleDateFormat("HH:mm")
    );

    public static Time parse(String timeString) {
        for (SimpleDateFormat format : allowedTimeFormats) {
            Optional<Time> time = parse(format, timeString);
            if (time.isPresent()) {
                return time.get();
            }
        }
        throw new IncorrectFormatException(INCORRECT_TIME_FORMAT_MESSAGE);
    }

    private static Optional<Time> parse(SimpleDateFormat format, String timeString) {
        try {
            Date date = format.parse(timeString);
            return Optional.of(new Time(date.getTime()));
        } catch (ParseException e) {
            return Optional.empty();
        }
    }
}
