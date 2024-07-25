package ru.lexender.icarusdb.auth.core.account.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldsMatchValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Match {
    String firstField();

    String secondField();

    String message() default "passwords must match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
