package com.student.reservationservice.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name =  "voivodeship")
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
    private byte id;
    @Column(unique=true)
    private String name;

    public Voivodeship(String name) {
        this.name = name;
    }

    public Voivodeship() {
    }

    public byte getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
