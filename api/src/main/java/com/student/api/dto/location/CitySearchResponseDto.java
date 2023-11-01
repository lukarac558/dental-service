package com.student.api.dto.location;

import lombok.Data;

@Data
public class CitySearchResponseDto {
    private Long id;
    private String name;
    private String postalCode;
}
