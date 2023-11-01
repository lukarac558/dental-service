package com.student.api.dto.location;

import lombok.Data;

@Data
public class CityDto {
    private Long id;
    private String code;
    private String name;
    private VoivodeshipDto voivodeship;
}
