package com.epam.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DifferentPasswordsValidator.class)
public @interface DifferentPasswords {
    String message() default "The two passwords must be different";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}