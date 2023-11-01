package com.student.userservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Entity
@Table(name = "competency_information")
public class CompetencyInformationEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "competency_information_id_seq", sequenceName = "competency_information_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "competency_information_id_seq")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    @OneToOne(mappedBy = "competencyInformation")
    private UserEntity doctor;
}
