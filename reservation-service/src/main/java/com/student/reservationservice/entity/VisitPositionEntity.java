package com.student.reservationservice.entity;

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
public class VisitPositionEntity implements Serializable {
    @SequenceGenerator(name = "visit_position_id_seq", sequenceName = "visit_position_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visit_position_id_seq")
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "visit_id", foreignKey = @ForeignKey(name = "fk_visit_position_visit_id"), nullable = false)
    private VisitEntity visit;

    @Column(name = "service_type_id", nullable = false)
    private Long serviceTypeId;

    public VisitPositionEntity(VisitEntity visit, Long serviceTypeId) {
        this.visit = visit;
        this.serviceTypeId = serviceTypeId;
    }
}
