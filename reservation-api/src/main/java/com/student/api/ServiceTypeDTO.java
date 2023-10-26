package com.student.api;

import lombok.Data;

import java.sql.Time;

@Data
public class ServiceTypeDTO {
    private Long id;
    private String name;
    private Time durationTime;
    private String description;
    private ApplicationUserInfoDTO user;
}
