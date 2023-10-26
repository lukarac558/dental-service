package com.student.api;

import lombok.Data;

@Data
public class CityDTO {
    private String code;
    private String name;
    private VoivodeshipDTO voivodeship;
}
