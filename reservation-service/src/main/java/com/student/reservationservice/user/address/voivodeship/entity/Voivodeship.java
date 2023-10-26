package com.student.reservationservice.user.address.voivodeship.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voivodeship")
public class Voivodeship {
    @SequenceGenerator(
            name = "voivodeship_sequence",
            sequenceName = "voivodeship_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "voivodeship_sequence"
    )
    @Id
    private int id;

    @Column(unique = true)
    private String name;
}
