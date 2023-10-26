package com.student.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class VisitLockCreationDTO {
    @NotNull
    private Timestamp startDate;

    @NotNull
    private long userId;
}
