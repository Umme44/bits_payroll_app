package com.bits.hr.util.validators;

import com.bits.hr.config.Constants;
import com.bits.hr.util.annotation.ValidateSearchText;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SearchTextValidator implements ConstraintValidator<ValidateSearchText, String> {
    private boolean allowNull;
    private boolean allowBlank;
    @Override
    public void initialize(ValidateSearchText constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
        this.allowBlank = constraintAnnotation.allowBlank();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return allowNull;
        }
        if (value.isBlank()){
            return allowBlank;
        }
        // string must start with a char or number
        // string can contain chars between a-z
        // string can contain chars between A-Z
        // string can contain numbers between 0-9
        // string can contain space
        // string can contain some special characters
        // - _ + - ( ) # .
        // string can be null
        return value.matches(Constants.NATURAL_TEXT_REGEX);
    }
}
