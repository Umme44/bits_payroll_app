package com.bits.hr.errors;

public class NoEmployeeProfileException extends BadRequestAlertException {

    public NoEmployeeProfileException() {
        super("No employee profile is associated with you. Please contact HR", "Employee", ErrorConstants.ERR_NO_EMPLOYEE_PROFILE);
    }
}
