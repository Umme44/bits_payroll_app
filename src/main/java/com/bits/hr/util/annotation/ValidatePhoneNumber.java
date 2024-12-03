package com.bits.hr.util.annotation;

import com.bits.hr.util.validators.PhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target( { ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatePhoneNumber {
    String message() default "Field must contain valid characters for a phone number";
    boolean allowNull() default false;
    boolean allowBlank() default false;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
