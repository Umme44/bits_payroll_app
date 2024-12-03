package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeSalaryTempDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeSalaryTempData.class);
        EmployeeSalaryTempData employeeSalaryTempData1 = new EmployeeSalaryTempData();
        employeeSalaryTempData1.setId(1L);
        EmployeeSalaryTempData employeeSalaryTempData2 = new EmployeeSalaryTempData();
        employeeSalaryTempData2.setId(employeeSalaryTempData1.getId());
        assertThat(employeeSalaryTempData1).isEqualTo(employeeSalaryTempData2);
        employeeSalaryTempData2.setId(2L);
        assertThat(employeeSalaryTempData1).isNotEqualTo(employeeSalaryTempData2);
        employeeSalaryTempData1.setId(null);
        assertThat(employeeSalaryTempData1).isNotEqualTo(employeeSalaryTempData2);
    }
}
