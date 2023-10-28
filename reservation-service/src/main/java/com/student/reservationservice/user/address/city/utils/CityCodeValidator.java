package com.student.reservationservice.user.address.city.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CityCodeValidator {
    private static final Pattern pattern = Pattern.compile("^\\d{2}-\\d{3}$");

    public static boolean isValid(String code) {
        Matcher matcher = pattern.matcher(code);
        return matcher.find();
    }
}
