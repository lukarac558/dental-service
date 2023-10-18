package com.student.reservationservice.visit;

import com.student.reservationservice.user.entity.ApplicationUser;
import jakarta.persistence.*;

import java.sql.Time;

@Entity
@Table(name = "service_type")
public class ServiceType {
    @SequenceGenerator(
            name = "type_sequence",
            sequenceName = "type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "type_sequence"
    )
    @Id
    private Long id;
    private String name;
    private Time durationTime;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private ApplicationUser user;
    private String description;

    public ServiceType() {
    }

    public ServiceType(String name, Time durationTime, ApplicationUser user, String description) {
        this.name = name;
        this.durationTime = durationTime;
        this.user = user;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Time getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Time durationTime) {
        this.durationTime = durationTime;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
