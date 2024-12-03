package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeePinDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeePinDTO.class);
        EmployeePinDTO employeePinDTO1 = new EmployeePinDTO();
        employeePinDTO1.setId(1L);
        EmployeePinDTO employeePinDTO2 = new EmployeePinDTO();
        assertThat(employeePinDTO1).isNotEqualTo(employeePinDTO2);
        employeePinDTO2.setId(employeePinDTO1.getId());
        assertThat(employeePinDTO1).isEqualTo(employeePinDTO2);
        employeePinDTO2.setId(2L);
        assertThat(employeePinDTO1).isNotEqualTo(employeePinDTO2);
        employeePinDTO1.setId(null);
        assertThat(employeePinDTO1).isNotEqualTo(employeePinDTO2);
    }
}
