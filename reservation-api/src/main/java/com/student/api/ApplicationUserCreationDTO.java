package com.student.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApplicationUserCreationDTO {
    @NotBlank
    @Size(min = 3, max = 100)
    private String email;

    @NotBlank
    @Size(min = 6, max = 50)
    private String password;

    @NotNull
    @Size(min = 11, max = 11)
    private Long pesel;

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @Size(min = 2, max = 50)
    private String surname;

    @NotBlank
    @Size(min = 8, max = 12)
    private String phoneNumber;

    @NotBlank
    @Size(min = 6, max = 6)
    private String cityCode;

    @NotBlank
    @Size(min = 2, max = 80)
    private String street;

    @NotBlank
    @Size(min = 1, max = 8)
    private String buildingNumber;
}