package com.bits.hr.util.validators;

import com.bits.hr.config.Constants;
import com.bits.hr.util.annotation.ValidateAlphaNumeric;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AlphaNumericValidator implements ConstraintValidator<ValidateAlphaNumeric, String> {
    private boolean allowNull;

    @Override
    public void initialize(ValidateAlphaNumeric constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // string can contain chars between a-z
        // string can contain chars between A-Z
        // string can contain numbers between 0-9
        if(value == null){
            return allowNull;
        }
        return value.matches(Constants.ALPHA_NUMERIC_REGEX);
    }
}
