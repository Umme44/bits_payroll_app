package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalaryGeneratorMasterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaryGeneratorMaster.class);
        SalaryGeneratorMaster salaryGeneratorMaster1 = new SalaryGeneratorMaster();
        salaryGeneratorMaster1.setId(1L);
        SalaryGeneratorMaster salaryGeneratorMaster2 = new SalaryGeneratorMaster();
        salaryGeneratorMaster2.setId(salaryGeneratorMaster1.getId());
        assertThat(salaryGeneratorMaster1).isEqualTo(salaryGeneratorMaster2);
        salaryGeneratorMaster2.setId(2L);
        assertThat(salaryGeneratorMaster1).isNotEqualTo(salaryGeneratorMaster2);
        salaryGeneratorMaster1.setId(null);
        assertThat(salaryGeneratorMaster1).isNotEqualTo(salaryGeneratorMaster2);
    }
}
