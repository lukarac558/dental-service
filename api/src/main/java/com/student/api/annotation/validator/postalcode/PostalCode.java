package com.student.api.annotation.validator.postalcode;

import jakarta.validation.Constraint;

import java.lang.annotation.*;
import java.math.BigInteger;

@Documented
@Constraint(validatedBy = PostalCodeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PostalCode {
    String message() default "Kod pocztowy jest nieprawidłowy";
    Class<?>[] groups() default {};
    Class<? extends BigInteger>[] payload() default {};
}
