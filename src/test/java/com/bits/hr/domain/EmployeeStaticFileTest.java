package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeStaticFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeStaticFile.class);
        EmployeeStaticFile employeeStaticFile1 = new EmployeeStaticFile();
        employeeStaticFile1.setId(1L);
        EmployeeStaticFile employeeStaticFile2 = new EmployeeStaticFile();
        employeeStaticFile2.setId(employeeStaticFile1.getId());
        assertThat(employeeStaticFile1).isEqualTo(employeeStaticFile2);
        employeeStaticFile2.setId(2L);
        assertThat(employeeStaticFile1).isNotEqualTo(employeeStaticFile2);
        employeeStaticFile1.setId(null);
        assertThat(employeeStaticFile1).isNotEqualTo(employeeStaticFile2);
    }
}
