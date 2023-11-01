package com.student.api.annotation.validator.postalcode;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostalCodeValidator implements ConstraintValidator<PostalCode, String> {

    private static final Pattern pattern = Pattern.compile("^\\d{2}-\\d{3}$");

    @Override
    public boolean isValid(String code, ConstraintValidatorContext constraintValidatorContext) {
        Matcher matcher = pattern.matcher(code);
        return matcher.find();
    }
}
