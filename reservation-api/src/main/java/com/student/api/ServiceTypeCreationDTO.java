package com.student.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Time;

@Data
public class ServiceTypeCreationDTO {
    @NotBlank
    @Size(min = 1, max = 100)
    @Schema(required = true)
    private String name;

    @NotNull
    @Schema(required = true, example = "01:00:00")
    private String durationTime;


    private String description;

    @NotNull
    @Schema(required = true)
    private Long userId;
}
