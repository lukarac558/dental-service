package com.student.api.dto.reservation;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class VisitDTO {
    private Long id;
    private Timestamp startDate;
    private Long patientId;
    private Timestamp reservationDate;
    private String description;
}
