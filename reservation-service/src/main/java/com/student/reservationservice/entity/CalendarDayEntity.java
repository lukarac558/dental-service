package com.student.reservationservice.entity;

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
public class CalendarDayEntity implements Serializable {
    @SequenceGenerator(name = "calendar_day_id_seq", sequenceName = "calendar_day_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calendar_day_id_seq")
    @Id
    private Long id;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "work_duration")
    private Time workDuration;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;
}
