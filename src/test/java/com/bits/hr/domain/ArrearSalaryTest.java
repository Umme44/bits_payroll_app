package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArrearSalaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArrearSalary.class);
        ArrearSalary arrearSalary1 = new ArrearSalary();
        arrearSalary1.setId(1L);
        ArrearSalary arrearSalary2 = new ArrearSalary();
        arrearSalary2.setId(arrearSalary1.getId());
        assertThat(arrearSalary1).isEqualTo(arrearSalary2);
        arrearSalary2.setId(2L);
        assertThat(arrearSalary1).isNotEqualTo(arrearSalary2);
        arrearSalary1.setId(null);
        assertThat(arrearSalary1).isNotEqualTo(arrearSalary2);
    }
}
