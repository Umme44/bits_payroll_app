package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeNOCDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeNOCDTO.class);
        EmployeeNOCDTO employeeNOCDTO1 = new EmployeeNOCDTO();
        employeeNOCDTO1.setId(1L);
        EmployeeNOCDTO employeeNOCDTO2 = new EmployeeNOCDTO();
        assertThat(employeeNOCDTO1).isNotEqualTo(employeeNOCDTO2);
        employeeNOCDTO2.setId(employeeNOCDTO1.getId());
        assertThat(employeeNOCDTO1).isEqualTo(employeeNOCDTO2);
        employeeNOCDTO2.setId(2L);
        assertThat(employeeNOCDTO1).isNotEqualTo(employeeNOCDTO2);
        employeeNOCDTO1.setId(null);
        assertThat(employeeNOCDTO1).isNotEqualTo(employeeNOCDTO2);
    }
}
