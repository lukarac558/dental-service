package com.student.locationservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voivodeship")
public class VoivodeshipEntity {
    @Id
    @SequenceGenerator(name = "voivodeship_id_seq", sequenceName = "voivodeship_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voivodeship_id_seq")
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "voivodeship", cascade = CascadeType.ALL)
    private List<CityEntity> cities;
}
