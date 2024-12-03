package com.bits.hr.util.annotation;

import com.bits.hr.util.validators.SearchTextValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SearchTextValidator.class)
@Target( { ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateSearchText {
    String message() default "Field must match the expression";
    boolean allowNull() default true;
    boolean allowBlank() default true;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
