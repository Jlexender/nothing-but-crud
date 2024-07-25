package ru.lexender.icarusdb.auth.core.account.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldsMatchValidator.class)
public @interface Match {
    String message() default "Fields do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String firstField();
    String secondField();
}
