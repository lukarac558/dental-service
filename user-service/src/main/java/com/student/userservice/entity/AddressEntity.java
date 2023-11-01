package com.student.userservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Entity
@Table(name = "address")
public class AddressEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "address_id_seq", sequenceName = "address_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_id_seq")
    private Long id;

    @Column(name = "voivodeship_id", nullable = false)
    private Long voivodeshipId;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "building_number", nullable = false)
    private String buildingNumber;

    @Column(name = "postalCode", nullable = false)
    private String postalCode;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    @OneToOne(mappedBy = "address")
    private UserEntity user;

}
