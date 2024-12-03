package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeResignationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeResignation.class);
        EmployeeResignation employeeResignation1 = new EmployeeResignation();
        employeeResignation1.setId(1L);
        EmployeeResignation employeeResignation2 = new EmployeeResignation();
        employeeResignation2.setId(employeeResignation1.getId());
        assertThat(employeeResignation1).isEqualTo(employeeResignation2);
        employeeResignation2.setId(2L);
        assertThat(employeeResignation1).isNotEqualTo(employeeResignation2);
        employeeResignation1.setId(null);
        assertThat(employeeResignation1).isNotEqualTo(employeeResignation2);
    }
}
