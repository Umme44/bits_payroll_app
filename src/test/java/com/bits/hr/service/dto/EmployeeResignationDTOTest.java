package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeResignationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeResignationDTO.class);
        EmployeeResignationDTO employeeResignationDTO1 = new EmployeeResignationDTO();
        employeeResignationDTO1.setId(1L);
        EmployeeResignationDTO employeeResignationDTO2 = new EmployeeResignationDTO();
        assertThat(employeeResignationDTO1).isNotEqualTo(employeeResignationDTO2);
        employeeResignationDTO2.setId(employeeResignationDTO1.getId());
        assertThat(employeeResignationDTO1).isEqualTo(employeeResignationDTO2);
        employeeResignationDTO2.setId(2L);
        assertThat(employeeResignationDTO1).isNotEqualTo(employeeResignationDTO2);
        employeeResignationDTO1.setId(null);
        assertThat(employeeResignationDTO1).isNotEqualTo(employeeResignationDTO2);
    }
}
