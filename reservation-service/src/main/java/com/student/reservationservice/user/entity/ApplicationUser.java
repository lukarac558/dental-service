package com.student.reservationservice.user.entity;

import com.student.reservationservice.visit.ServiceType;
import com.student.reservationservice.visit.Visit;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "application_user")
public class ApplicationUser implements Serializable {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    @Column(unique=true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "pesel", referencedColumnName = "pesel")
//    private PersonalIdentity pesel;
    @Column(unique=true)
    private Long pesel;
    private String name;
    private String surname;
    private String phoneNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_code", referencedColumnName = "code")
    private City city;
    private String street;
    private String buildingNumber;
    @OneToMany(mappedBy="user")
    private Set<ServiceType> serviceTypes;
    @OneToMany(mappedBy="user")
    private Set<Visit> visits;

    public ApplicationUser(String email, String password, Role role, Long pesel, String name, String surname, String phoneNumber, City city, String street, String buildingNumber) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
    }

    public ApplicationUser() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getPesel() {
        return pesel;
    }

    public void setPesel(long pesel) {
        this.pesel = pesel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    @Override
    public String toString() {
        return email;
    }
}
