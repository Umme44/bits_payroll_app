package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeDocument.class);
        EmployeeDocument employeeDocument1 = new EmployeeDocument();
        employeeDocument1.setId(1L);
        EmployeeDocument employeeDocument2 = new EmployeeDocument();
        employeeDocument2.setId(employeeDocument1.getId());
        assertThat(employeeDocument1).isEqualTo(employeeDocument2);
        employeeDocument2.setId(2L);
        assertThat(employeeDocument1).isNotEqualTo(employeeDocument2);
        employeeDocument1.setId(null);
        assertThat(employeeDocument1).isNotEqualTo(employeeDocument2);
    }
}
