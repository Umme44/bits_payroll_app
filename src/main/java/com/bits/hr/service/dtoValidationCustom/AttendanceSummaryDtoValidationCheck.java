package com.bits.hr.service.dtoValidationCustom;

import com.bits.hr.service.dto.AttendanceSummaryDTO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AttendanceSummaryDtoValidationCheck implements ConstraintValidator<AttendanceSummaryDtoValidation, AttendanceSummaryDTO> {

    @Override
    public void initialize(AttendanceSummaryDtoValidation constraint) {}

    @Override
    public boolean isValid(AttendanceSummaryDTO object, ConstraintValidatorContext context) {
        int month, year, workingdays, leavedays, absentdays, fractiondays, flag = 0;

        month = object.getMonth();
        year = object.getYear();
        workingdays = object.getTotalWorkingDays();
        leavedays = object.getTotalLeaveDays();
        absentdays = object.getTotalAbsentDays();
        fractiondays = object.getTotalFractionDays();
        if ((year % 400 == 0) || (year % 100 != 0 && year % 4 == 0)) {
            flag = 1;
        }

        if (flag == 1 && month == 2 && (workingdays > 29 || leavedays > 29 || absentdays > 29 && fractiondays > 29)) {
            // System.out.println("first\n");
            return false;
        }

        if (flag == 0 && month == 2 && (workingdays > 28 || leavedays > 28 || absentdays > 28 || fractiondays > 28)) {
            return false;
        }
        if (
            (month == 4 || month == 6 || month == 9 || month == 11) &&
            (workingdays > 30 || leavedays > 30 || absentdays > 30 || fractiondays > 30)
        ) {
            return false;
        }

        return true;
    }
}
