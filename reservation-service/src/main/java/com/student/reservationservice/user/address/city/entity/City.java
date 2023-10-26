package com.student.reservationservice.user.address.city.entity;

import com.student.reservationservice.user.address.voivodeship.entity.Voivodeship;
import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "city")
public class City {
    private static final String FK_CITY_VOIVODESHIP_ID = "fk_city_voivodeship_id";

    @Id
    private String code;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "voivodeship_id", foreignKey = @ForeignKey(name = FK_CITY_VOIVODESHIP_ID), nullable = false)
    private Voivodeship voivodeship;
}
