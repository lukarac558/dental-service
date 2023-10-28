package com.student.reservationservice.visit.visit.entity;

import com.student.reservationservice.user.applicationuser.entity.ApplicationUser;
import com.student.reservationservice.visit.visitposition.entity.VisitPosition;
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
public class Visit implements Serializable {
    private static final String FK_VISIT_USER_ID = "fk_visit_user_id";

    @SequenceGenerator(
            name = "visit_sequence",
            sequenceName = "visit_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "visit_sequence"
    )
    @Id
    private Long id;

    private Timestamp startDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = FK_VISIT_USER_ID), nullable = false)
    private ApplicationUser user;

    @Column(name = "reservation_date")
    private Timestamp reservationDate;

    private String description;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisitPosition> visitPositions;
}

