package com.bits.hr.util.validators;

import com.bits.hr.config.Constants;
import com.bits.hr.util.annotation.ValidateAlphabets;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AlphabetsValidator implements ConstraintValidator<ValidateAlphabets, String> {
    private boolean allowNull;

    @Override
    public void initialize(ValidateAlphabets constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return allowNull;
        }
        return value.matches(Constants.ALPHABETS_REGEX);
    }

}
