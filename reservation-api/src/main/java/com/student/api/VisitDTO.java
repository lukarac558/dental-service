package com.student.api;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class VisitDTO {
    private Long id;
    private Timestamp startDate;
    private ApplicationUserInfoDTO user;
    private Timestamp reservationDate;
    private String description;
}
