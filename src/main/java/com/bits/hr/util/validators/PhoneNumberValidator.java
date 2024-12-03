package com.bits.hr.util.validators;

import com.bits.hr.config.Constants;
import com.bits.hr.util.annotation.ValidatePhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidatePhoneNumber, String> {
    private boolean allowNull;
    private boolean allowBlank;

    @Override
    public void initialize(ValidatePhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.allowNull = constraintAnnotation.allowNull();
        this.allowBlank = constraintAnnotation.allowBlank();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // can contain numeric characters
        // can contain specific special characters
        // + - ( )
        // can contain space
        if(value == null){
            return allowNull;
        }
        if (value.isBlank()){
            return allowBlank;
        }
        return value.matches(Constants.PHONE_NUMBER_REGEX);
    }

}
