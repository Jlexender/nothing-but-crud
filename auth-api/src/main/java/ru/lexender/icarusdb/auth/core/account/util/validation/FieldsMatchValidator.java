package ru.lexender.icarusdb.auth.core.account.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.AssertTrue;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

@Log4j2
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class FieldsMatchValidator implements ConstraintValidator<Match, Object> {
    String firstField;
    String secondField;

    @Override
    public void initialize(Match constraintAnnotation) {
        firstField = constraintAnnotation.firstField();
        secondField = constraintAnnotation.secondField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field first = value.getClass().getDeclaredField(firstField);
            Field second = value.getClass().getDeclaredField(secondField);

            first.setAccessible(true);
            second.setAccessible(true);

            Object firstValue = first.get(value);
            Object secondValue = second.get(value);

            boolean matches = firstValue == null && secondValue == null || firstValue != null && firstValue.equals(secondValue);

            if (!matches) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode(secondField)
                        .addConstraintViolation();
            }

            return matches;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("Error while validating fields match", e);
            return false;
        }
    }
}
