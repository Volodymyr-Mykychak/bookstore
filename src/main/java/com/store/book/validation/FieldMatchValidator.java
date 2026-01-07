package com.store.book.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraint) {
        this.firstFieldName = constraint.first();
        this.secondFieldName = constraint.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object firstValue = new BeanWrapperImpl(value)
                .getPropertyValue(firstFieldName);
        Object secondValue = new BeanWrapperImpl(value)
                .getPropertyValue(secondFieldName);
        boolean isValid = Objects.equals(firstValue, secondValue);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(
                            context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(secondFieldName)
                    .addConstraintViolation();
        }
        return isValid;
    }
}
