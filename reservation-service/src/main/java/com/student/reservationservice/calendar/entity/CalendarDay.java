package com.student.reservationservice.calendar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "calendar_day")
public class CalendarDay implements Serializable {
    private static final String FK_CALENDAR_DAY_USER_ID = "fk_calendar_day_user_id";

    @SequenceGenerator(
            name = "calendar_day_sequence",
            sequenceName = "calendar_day_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "calendar_day_sequence"
    )
    @Id
    private Long id;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "work_duration")
    private Time workDuration;

    @Column(name = "start_break_time")
    private Time startBreakTime;

    @Column(name = "break_duration")
    private Time breakDuration;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;
}
