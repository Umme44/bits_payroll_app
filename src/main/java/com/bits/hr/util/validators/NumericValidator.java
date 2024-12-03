package com.bits.hr.util.validators;

import com.bits.hr.config.Constants;
import com.bits.hr.util.annotation.ValidateNumeric;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NumericValidator implements ConstraintValidator<ValidateNumeric, String> {
    private boolean allowNull;

    @Override
    public void initialize(ValidateNumeric constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return allowNull;
        }
        // can contain only numeric characters
        return value==null || value.isBlank() || (value.matches(Constants.NUMERIC_REGEX));
    }

}
