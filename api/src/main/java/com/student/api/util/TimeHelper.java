package com.student.api.util;

import com.student.api.dto.reservation.StartEndTime;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeHelper {
    public static final int ONE_MINUTE_IN_MS = 60000;
    public static final int ONE_HOUR_IN_MS = 60 * ONE_MINUTE_IN_MS;

    private TimeHelper() {
    }

    public static boolean isEnough(StartEndTime startEndTime, Time duration) {
        long differenceInMs = TimeHelper.getDifferenceInMs(startEndTime);
        long durationInMs = duration.getTime();
        return differenceInMs >= durationInMs;
    }

    public static Timestamp getEndDate(Timestamp startDate, Time time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startDate.getTime());
        cal.add(Calendar.HOUR, time.getHours());
        cal.add(Calendar.MINUTE, time.getMinutes());
        return new Timestamp(cal.getTime().getTime());
    }

    public static Time getSummed(Time time1, Time time2) {
        int h = time1.getHours() + time2.getHours();
        int m = time1.getMinutes() + time2.getMinutes();
        int s = time1.getSeconds() + time2.getSeconds();
        return new Time(h, m, s);
    }

    public static List<String> getStartDatesInIntervals(List<StartEndTime> possibleTimes, Time duration, int intervalInMs) {
        List<String> startDates = new ArrayList<>();
        possibleTimes.forEach(startEndTime -> {
            long differenceInMs = getDifferenceInMs(startEndTime);
            long durationInMs = getDurationInMs(duration);
            long intervals = (differenceInMs - durationInMs) / intervalInMs;

            if (intervals == 0) {
                String startDateString = TimestampFormatParser.parse(startEndTime.getStartTime());
                startDates.add(startDateString);
            } else if (intervals > 0) {
                for (int i = 0; i <= intervals; i++) {
                    Timestamp startDate = getNextInterval(startEndTime.getStartTime(), i * intervalInMs);
                    String startDateString = TimestampFormatParser.parse(startDate);
                    startDates.add(startDateString);
                }
            }
        });
        return startDates;
    }

    private static long getDurationInMs(Time duration) {
        int hours = duration.getHours();
        int minutes = duration.getMinutes();
        return (hours * ONE_HOUR_IN_MS) + (minutes * ONE_MINUTE_IN_MS);
    }

    private static long getDifferenceInMs(StartEndTime startEndTime) {
        long start = startEndTime.getStartTime().getTime();
        long end = startEndTime.getEndTime().getTime();
        return end - start;
    }

    private static Timestamp getNextInterval(Timestamp startDate, int timeLaterInMs) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startDate.getTime());
        cal.add(Calendar.MILLISECOND, timeLaterInMs);
        return new Timestamp(cal.getTime().getTime());
    }
}
