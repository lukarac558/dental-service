package com.student.api.util;

import com.student.api.dto.reservation.StartEndTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StartEndTimeTest {

    private static final Timestamp START_DATE_1 = Timestamp.valueOf("2023-10-20 08:00:00");
    private static final Timestamp START_DATE_2 = Timestamp.valueOf("2023-10-21 08:00:00");
    private static final Timestamp START_DATE_3 = Timestamp.valueOf("2023-10-21 09:00:00");

    @Test
    void shouldSortVisitTimesByStartDate() {
        List<StartEndTime> visitTimes = new ArrayList<>();

        visitTimes.add(new StartEndTime(START_DATE_2));
        visitTimes.add(new StartEndTime(START_DATE_3));
        visitTimes.add(new StartEndTime(START_DATE_1));

        visitTimes.sort(Comparator.comparing(StartEndTime::getStartTime));

        Assertions.assertEquals(visitTimes.get(0).getStartTime(), START_DATE_1);
        Assertions.assertEquals(visitTimes.get(1).getStartTime(), START_DATE_2);
        Assertions.assertEquals(visitTimes.get(2).getStartTime(), START_DATE_3);
    }
}
