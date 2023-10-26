package com.student.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompetencyInformationCreationDTO {
    @NotBlank
    @Size(min = 1, max = 50)
    String title;

    @NotBlank
    @Size(min = 1, max = 500)
    String description;
}
