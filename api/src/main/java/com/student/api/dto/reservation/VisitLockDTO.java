package com.student.api.dto.reservation;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class VisitLockDTO {
    long id;
    private Timestamp startDate;
    private Long doctorId;
}
