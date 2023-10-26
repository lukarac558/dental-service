package com.student.reservationservice.servicetype.entity;

import com.student.reservationservice.user.applicationuser.entity.ApplicationUser;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Time;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service_type")
public class ServiceType implements Serializable {
    private static final String FK_SERVICE_TYPE_USER_ID = "fk_service_type_user_id";

    @SequenceGenerator(
            name = "type_sequence",
            sequenceName = "type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "type_sequence"
    )
    @Id
    private Long id;

    private String name;

    @Column(name = "duration_time")
    private Time durationTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = FK_SERVICE_TYPE_USER_ID), nullable = false)
    private ApplicationUser user;

    private String description;
}
