package com.student.reservationservice.visit;

import com.student.reservationservice.user.entity.ApplicationUser;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "visit")
public class Visit {
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
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private ApplicationUser user;
    private Timestamp reservationDate;

    public Visit() {
    }

    public Visit(Timestamp startDate, ApplicationUser user, Timestamp reservationDate) {
        this.startDate = startDate;
        this.user = user;
        this.reservationDate = reservationDate;
    }

    public Long getId() {
        return id;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public Timestamp getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Timestamp reservationDate) {
        this.reservationDate = reservationDate;
    }
}

