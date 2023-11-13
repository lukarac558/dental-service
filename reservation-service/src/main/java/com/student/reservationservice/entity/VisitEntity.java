package com.student.reservationservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "visit")
public class VisitEntity implements Serializable {
    @SequenceGenerator(name = "visit_id_seq", sequenceName = "visit_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visit_id_seq")
    @Id
    private Long id;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "reservation_date")
    private Timestamp reservationDate;

    private String description;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisitPositionEntity> visitPositions;

    private boolean approved;
}

