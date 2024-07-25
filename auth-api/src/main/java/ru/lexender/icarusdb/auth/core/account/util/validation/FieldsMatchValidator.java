package ru.lexender.icarusdb.auth.core.account.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Field;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class FieldsMatchValidator implements ConstraintValidator<Match, Object> {
    String firstFieldName;
    String secondFieldName;

    @Override
    public void initialize(Match matchAnnotation) {
        this.firstFieldName = matchAnnotation.firstField();
        this.secondFieldName = matchAnnotation.secondField();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            Class<?> clazz = obj.getClass();
            Field firstField = clazz.getDeclaredField(firstFieldName);
            Field secondField = clazz.getDeclaredField(secondFieldName);

            firstField.setAccessible(true);
            secondField.setAccessible(true);

            Object firstValue = firstField.get(obj);
            Object secondValue = secondField.get(obj);

            return firstValue != null && firstValue.equals(secondValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("An error occurred while validating fields match", e);
        }
    }
}
