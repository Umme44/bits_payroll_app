package com.bits.hr.service.dtoValidationCustom;

import static java.time.temporal.ChronoUnit.DAYS;

import java.lang.reflect.Field;
import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DateValidation implements ConstraintValidator<ValidDate, Object> {

    String startFieldName;
    String endFieldName;

    public void initialize(ValidDate constraint) {
        startFieldName = constraint.start();
        endFieldName = constraint.end();
    }

    public boolean isValid(Object object, ConstraintValidatorContext context) {
        Class<?> cls = object.getClass();
        try {
            Field field = cls.getDeclaredField(startFieldName);
            field.setAccessible(true);
            LocalDate startDate = (LocalDate) field.get(object);
            field.setAccessible(false);

            field = cls.getDeclaredField(endFieldName);
            field.setAccessible(true);
            LocalDate endDate = (LocalDate) field.get(object);
            field.setAccessible(false);

            if (startDate != null && endDate != null) {
                long numberOfDaysBetween = DAYS.between(startDate, endDate);

                if (numberOfDaysBetween >= 0) return true;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error(e);
        }

        return false;
    }
}
