package com.student.reservationservice.user.competency.entity;

import com.student.reservationservice.user.applicationuser.entity.ApplicationUser;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "competency_information")
public class CompetencyInformation implements Serializable {
    @SequenceGenerator(
            name = "competency_information_sequence",
            sequenceName = "competency_information_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "competency_information_sequence"
    )
    @Id
    private Long id;

    private String title;

    private String description;

    @OneToOne(mappedBy = "competencyInformation")
    private ApplicationUser applicationUser;

    public CompetencyInformation(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
