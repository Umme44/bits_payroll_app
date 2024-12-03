package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeePinTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeePin.class);
        EmployeePin employeePin1 = new EmployeePin();
        employeePin1.setId(1L);
        EmployeePin employeePin2 = new EmployeePin();
        employeePin2.setId(employeePin1.getId());
        assertThat(employeePin1).isEqualTo(employeePin2);
        employeePin2.setId(2L);
        assertThat(employeePin1).isNotEqualTo(employeePin2);
        employeePin1.setId(null);
        assertThat(employeePin1).isNotEqualTo(employeePin2);
    }
}
