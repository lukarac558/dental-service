package com.student.api.dto.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CalendarDayCreationDTO {
    @NotNull
    @Schema(required = true, example = "2023-10-27 08:00:00")
    private String startDate;

    @NotNull
    @Schema(required = true, example = "08:00:00")
    private String workDuration;

    @Schema(example = "12:00:00")
    private String startBreakTime;

    @Schema(example = "00:30:00")
    private String breakDuration;

    @NotNull
    @Schema(required = true)
    private Long doctorId;
}
