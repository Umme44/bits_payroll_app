package com.bits.hr.util.annotation;

import com.bits.hr.util.validators.AlphabetsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AlphabetsValidator.class)
@Target( { ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateAlphabets {
    String message() default "Field must contain only alphabets";
    boolean allowNull() default false;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
