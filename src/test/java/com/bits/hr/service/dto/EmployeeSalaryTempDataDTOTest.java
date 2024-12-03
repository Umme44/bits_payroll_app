package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeSalaryTempDataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeSalaryTempDataDTO.class);
        EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO1 = new EmployeeSalaryTempDataDTO();
        employeeSalaryTempDataDTO1.setId(1L);
        EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO2 = new EmployeeSalaryTempDataDTO();
        assertThat(employeeSalaryTempDataDTO1).isNotEqualTo(employeeSalaryTempDataDTO2);
        employeeSalaryTempDataDTO2.setId(employeeSalaryTempDataDTO1.getId());
        assertThat(employeeSalaryTempDataDTO1).isEqualTo(employeeSalaryTempDataDTO2);
        employeeSalaryTempDataDTO2.setId(2L);
        assertThat(employeeSalaryTempDataDTO1).isNotEqualTo(employeeSalaryTempDataDTO2);
        employeeSalaryTempDataDTO1.setId(null);
        assertThat(employeeSalaryTempDataDTO1).isNotEqualTo(employeeSalaryTempDataDTO2);
    }
}
