package com.student.api;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;

@Data
public class CalendarDayCreationDTO {
    @NotNull
    private Timestamp startDate;

    @NotNull
    private Time workDuration;

    @NotNull
    private Time startBreakTime;

    @NotNull
    private Time breakDuration;

    @NotNull
    private Long userId;
}
