package com.bits.hr.util.annotation;

import com.bits.hr.util.validators.NumericValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NumericValidator.class)
@Target( {ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateNumeric {
    String message() default "Field must contain only numeric characters";
    boolean allowNull() default false;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
