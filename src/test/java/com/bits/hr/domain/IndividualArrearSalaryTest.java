package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndividualArrearSalaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndividualArrearSalary.class);
        IndividualArrearSalary individualArrearSalary1 = new IndividualArrearSalary();
        individualArrearSalary1.setId(1L);
        IndividualArrearSalary individualArrearSalary2 = new IndividualArrearSalary();
        individualArrearSalary2.setId(individualArrearSalary1.getId());
        assertThat(individualArrearSalary1).isEqualTo(individualArrearSalary2);
        individualArrearSalary2.setId(2L);
        assertThat(individualArrearSalary1).isNotEqualTo(individualArrearSalary2);
        individualArrearSalary1.setId(null);
        assertThat(individualArrearSalary1).isNotEqualTo(individualArrearSalary2);
    }
}
