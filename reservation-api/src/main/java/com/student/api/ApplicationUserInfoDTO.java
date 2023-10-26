package com.student.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUserInfoDTO {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private String phoneNumber;

}
