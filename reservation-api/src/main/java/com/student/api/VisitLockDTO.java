package com.student.api;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class VisitLockDTO {
    long id;
    private Timestamp startDate;
    private long userId;
}
