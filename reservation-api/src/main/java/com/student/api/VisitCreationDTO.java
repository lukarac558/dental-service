package com.student.api;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class VisitCreationDTO {
    @NotNull
    private Timestamp startDate;

    @NotNull
    private Long userId;

    @NotNull
    private Timestamp reservationDate;
}
