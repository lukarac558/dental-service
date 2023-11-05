package com.student.api.dto.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CityDto {
    private Long id;
    @Schema(example = "44-100")
    private String code;
    @Schema(example = "Gliwice")
    private String name;
    private VoivodeshipDto voivodeship;
}
