package com.bits.hr.util.annotation;

import com.bits.hr.util.validators.AlphaNumericValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AlphaNumericValidator.class)
@Target( { ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateAlphaNumeric {
    String message() default "Field must contain only alphabets or numeric characters";
    boolean allowNull() default false;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
