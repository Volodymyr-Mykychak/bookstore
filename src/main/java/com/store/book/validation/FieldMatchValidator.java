package com.store.book.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    public void initialize(FieldMatch constraint) {
        firstFieldName = constraint.first();
        secondFieldName = constraint.second();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            final String firstObj = BeanUtils.getProperty(value, firstFieldName);
            final String secondObj = BeanUtils.getProperty(value, secondFieldName);
            return firstObj != null && firstObj.equals(secondObj);
        } catch (Exception ignore) {
            return false;
        }
    }
}

