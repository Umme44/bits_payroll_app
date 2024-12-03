package com.bits.hr.errors;

public class EmployeeNotFoundException extends BadRequestAlertException {

    public EmployeeNotFoundException(long employeeId) {
        super(String.format("Employee not found with ID: %s", employeeId), "Employee", "employeeNotFoundById");
    }
}
