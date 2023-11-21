package com.student.reservationservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "visit_lock")
public class VisitLockEntity implements Serializable {
    @SequenceGenerator(name = "visit_lock_id_seq", sequenceName = "visit_lock_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visit_lock_id_seq")
    @Id
    private Long id;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "doctor_id")
    private Long doctorId;

    public VisitLockEntity(Timestamp startDate, Long doctorId) {
        this.startDate = startDate;
        this.doctorId = doctorId;
    }
}