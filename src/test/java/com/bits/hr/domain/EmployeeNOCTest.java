package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeNOCTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeNOC.class);
        EmployeeNOC employeeNOC1 = new EmployeeNOC();
        employeeNOC1.setId(1L);
        EmployeeNOC employeeNOC2 = new EmployeeNOC();
        employeeNOC2.setId(employeeNOC1.getId());
        assertThat(employeeNOC1).isEqualTo(employeeNOC2);
        employeeNOC2.setId(2L);
        assertThat(employeeNOC1).isNotEqualTo(employeeNOC2);
        employeeNOC1.setId(null);
        assertThat(employeeNOC1).isNotEqualTo(employeeNOC2);
    }
}
