package com.student.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Time;

@Data
public class ServiceTypeCreationDTO {
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @NotNull
    private Time durationTime;

    private String description;

    @NotNull
    private Long userId;
}
