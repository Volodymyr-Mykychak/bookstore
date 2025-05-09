package com.store.book.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.apache.commons.beanutils.BeanUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    public void initialize(FieldMatch constraint) {
        firstFieldName = constraint.first();
        secondFieldName = constraint.second();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String firstObj;
        String secondObj;
        try {
            firstObj = BeanUtils.getProperty(value, firstFieldName);
            secondObj = BeanUtils.getProperty(value, secondFieldName);
        } catch (Exception e) {
            return false;
        }
        return Objects.equals(firstObj, secondObj);
    }
}
