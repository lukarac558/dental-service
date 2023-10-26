package com.student.api;

import lombok.Data;

@Data
public class ApplicationUserDTO {
    private Long id;
    private String email;
    private Role role;
    private Long pesel;
    private String name;
    private String surname;
    private String phoneNumber;
    private CityDTO city;
    private String street;
    private String buildingNumber;
    private CompetencyInformationDTO competencyInformation;
}
