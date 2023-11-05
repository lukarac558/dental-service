package com.student.api.dto.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VoivodeshipDto {
    private int id;
    @Schema(example = "Śląsk")
    private String name;
}
