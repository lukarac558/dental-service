package com.student.reservationservice.user.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "city")
public class City {
    @Id
    private String code;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "voivodeship_id", referencedColumnName = "id")
    private Voivodeship voivodeship;
    @OneToOne(mappedBy = "city")
    private ApplicationUser user;

    public City(String code, String name, Voivodeship voivodeship) {
        this.code = code;
        this.name = name;
        this.voivodeship = voivodeship;
    }

    public City() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Voivodeship getVoivodeshipId() {
        return voivodeship;
    }

    public void setVoivodeshipId(Voivodeship voivodeship) {
        this.voivodeship = voivodeship;
    }

    @Override
    public String toString() {
        return name + "[" + code + "]";
    }
}
