package com.student.reservationservice.user.applicationuser.utils;

import com.student.api.ApplicationUserInfoDTO;
import com.student.reservationservice.user.applicationuser.entity.ApplicationUser;

public class ApplicationUserMapper {
    public static ApplicationUserInfoDTO map(ApplicationUser applicationUser) {
        return ApplicationUserInfoDTO.builder()
                .id(applicationUser.getId())
                .email(applicationUser.getEmail())
                .name(applicationUser.getName())
                .surname(applicationUser.getSurname())
                .phoneNumber(applicationUser.getPhoneNumber())
                .build();
    }
}
