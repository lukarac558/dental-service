package com.student.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class VisitCreationDTO {
    @NotNull
    @Schema(required = true, example = "2023-10-27 08:00:00")
    private String startDate;

    @NotNull
    private Long userId;

    @NotNull
    @Schema(required = true, example = "2023-10-20 17:30:00")
    private String reservationDate;
}
