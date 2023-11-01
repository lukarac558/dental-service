package com.student.api.annotation.validator.phone;

import jakarta.validation.Constraint;

import java.lang.annotation.*;
import java.math.BigInteger;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {
    String message() default "Numer telefonu jest nieprawidłowy";
    Class<?>[] groups() default {};
    Class<? extends BigInteger>[] payload() default {};
}
