package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalaryDeductionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaryDeduction.class);
        SalaryDeduction salaryDeduction1 = new SalaryDeduction();
        salaryDeduction1.setId(1L);
        SalaryDeduction salaryDeduction2 = new SalaryDeduction();
        salaryDeduction2.setId(salaryDeduction1.getId());
        assertThat(salaryDeduction1).isEqualTo(salaryDeduction2);
        salaryDeduction2.setId(2L);
        assertThat(salaryDeduction1).isNotEqualTo(salaryDeduction2);
        salaryDeduction1.setId(null);
        assertThat(salaryDeduction1).isNotEqualTo(salaryDeduction2);
    }
}
