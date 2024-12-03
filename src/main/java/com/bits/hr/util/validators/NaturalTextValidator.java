package com.bits.hr.util.validators;

import com.bits.hr.config.Constants;
import com.bits.hr.util.annotation.ValidateNaturalText;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NaturalTextValidator implements ConstraintValidator<ValidateNaturalText, String> {
    private boolean allowNull;
    @Override
    public void initialize(ValidateNaturalText constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // string must start with a char or number
        // string can contain chars between a-z
        // string can contain chars between A-Z
        // string can contain numbers between 0-9
        // string can contain space
        // string can contain some special characters
        // - _ + - ( ) # .
        // string can be null
        if(value == null){
            return allowNull;
        }
        return value == null || value.isBlank() || value.matches(Constants.NATURAL_TEXT_REGEX);
    }
}
