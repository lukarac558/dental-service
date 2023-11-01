package com.student.reservationservice.lock.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "visit_lock")
public class VisitLock implements Serializable {
    @SequenceGenerator(
            name = "visit_lock_sequence",
            sequenceName = "visit_lock_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "visit_lock_sequence"
    )
    @Id
    private Long id;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    public VisitLock(Timestamp startDate, Long doctorId) {
        this.startDate = startDate;
        this.doctorId = doctorId;
    }
}
