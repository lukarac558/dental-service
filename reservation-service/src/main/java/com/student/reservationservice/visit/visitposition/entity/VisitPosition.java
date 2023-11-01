package com.student.reservationservice.visit.visitposition.entity;

import com.student.reservationservice.visit.visit.entity.Visit;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "visit_position")
public class VisitPosition implements Serializable {
    private static final String FK_VISIT_POSITION_VISIT_ID = "fk_visit_position_visit_id";
    private static final String FK_VISIT_POSITION_SERVICE_TYPE_ID = "fk_visit_position_service_type_id";

    @SequenceGenerator(
            name = "visit_position_sequence",
            sequenceName = "visit_position_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "visit_position_sequence"
    )
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "visit_id", foreignKey = @ForeignKey(name = FK_VISIT_POSITION_VISIT_ID), nullable = false)
    private Visit visit;

    @Column(name = "service_type_id", nullable = false)
    private Long serviceTypeId;

    public VisitPosition(Visit visit, Long serviceTypeId) {
        this.visit = visit;
        this.serviceTypeId = serviceTypeId;
    }
}
