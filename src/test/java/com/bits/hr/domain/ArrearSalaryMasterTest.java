package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArrearSalaryMasterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArrearSalaryMaster.class);
        ArrearSalaryMaster arrearSalaryMaster1 = new ArrearSalaryMaster();
        arrearSalaryMaster1.setId(1L);
        ArrearSalaryMaster arrearSalaryMaster2 = new ArrearSalaryMaster();
        arrearSalaryMaster2.setId(arrearSalaryMaster1.getId());
        assertThat(arrearSalaryMaster1).isEqualTo(arrearSalaryMaster2);
        arrearSalaryMaster2.setId(2L);
        assertThat(arrearSalaryMaster1).isNotEqualTo(arrearSalaryMaster2);
        arrearSalaryMaster1.setId(null);
        assertThat(arrearSalaryMaster1).isNotEqualTo(arrearSalaryMaster2);
    }
}
