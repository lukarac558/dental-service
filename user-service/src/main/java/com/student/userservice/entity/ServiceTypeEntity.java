package com.student.userservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Time;

@Data
@Entity
@Table(name = "service_type")
public class ServiceTypeEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "service_type_id_seq", sequenceName = "service_type_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_type_id_seq")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "duration_time", nullable = false)
    private Time durationTime;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", foreignKey = @ForeignKey(name = "fk_service_type_user_id"), nullable = false)
    private UserEntity doctor;

    private String description;
}
