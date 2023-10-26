package com.student.reservationservice.user.applicationuser.entity;

import com.student.api.Role;
import com.student.reservationservice.calendar.entity.CalendarDay;
import com.student.reservationservice.servicetype.entity.ServiceType;
import com.student.reservationservice.user.address.city.entity.City;
import com.student.reservationservice.user.competency.entity.CompetencyInformation;
import com.student.reservationservice.visit.visit.entity.Visit;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "application_user")
public class ApplicationUser implements Serializable {
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Id
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(unique = true)
    private Long pesel;

    private String name;

    private String surname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne()
    @JoinColumn(name = "city_code", referencedColumnName = "code")
    private City city;

    private String street;

    @Column(name = "building_number")
    private String buildingNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<ServiceType> serviceTypes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Visit> visits;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<CalendarDay> calendarDays;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private CompetencyInformation competencyInformation;
}
