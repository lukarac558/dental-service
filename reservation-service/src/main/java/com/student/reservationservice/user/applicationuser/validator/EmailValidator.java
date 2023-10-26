package com.student.reservationservice.user.applicationuser.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

     private static final Pattern pattern = Pattern.compile("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$");

        public boolean isEmailFormatValid(String email) {
            Matcher matcher = pattern.matcher(email);
            return matcher.find();
        }
}
