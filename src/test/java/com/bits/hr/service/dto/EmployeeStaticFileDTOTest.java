package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeStaticFileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeStaticFileDTO.class);
        EmployeeStaticFileDTO employeeStaticFileDTO1 = new EmployeeStaticFileDTO();
        employeeStaticFileDTO1.setId(1L);
        EmployeeStaticFileDTO employeeStaticFileDTO2 = new EmployeeStaticFileDTO();
        assertThat(employeeStaticFileDTO1).isNotEqualTo(employeeStaticFileDTO2);
        employeeStaticFileDTO2.setId(employeeStaticFileDTO1.getId());
        assertThat(employeeStaticFileDTO1).isEqualTo(employeeStaticFileDTO2);
        employeeStaticFileDTO2.setId(2L);
        assertThat(employeeStaticFileDTO1).isNotEqualTo(employeeStaticFileDTO2);
        employeeStaticFileDTO1.setId(null);
        assertThat(employeeStaticFileDTO1).isNotEqualTo(employeeStaticFileDTO2);
    }
}
