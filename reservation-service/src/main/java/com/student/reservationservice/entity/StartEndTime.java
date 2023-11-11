package com.student.reservationservice.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class StartEndTime {
    @NotNull
    private Timestamp startTime;

    @NotNull
    private Timestamp endTime;

    public StartEndTime(Timestamp startTime) {
        this.startTime = startTime;
    }
}
