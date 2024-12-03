package com.bits.hr.util.annotation;

import com.bits.hr.util.validators.NaturalTextValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NaturalTextValidator.class)
@Target( { ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateNaturalText {
    String message() default "Field must match the expression";
    boolean allowNull() default false;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
