package com.student.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VoivodeshipCreationDTO {
    @NotBlank
    @Size(min = 3, max = 20)
    String name;
}
