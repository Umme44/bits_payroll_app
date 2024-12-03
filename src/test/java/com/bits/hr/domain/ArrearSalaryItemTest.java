package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArrearSalaryItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArrearSalaryItem.class);
        ArrearSalaryItem arrearSalaryItem1 = new ArrearSalaryItem();
        arrearSalaryItem1.setId(1L);
        ArrearSalaryItem arrearSalaryItem2 = new ArrearSalaryItem();
        arrearSalaryItem2.setId(arrearSalaryItem1.getId());
        assertThat(arrearSalaryItem1).isEqualTo(arrearSalaryItem2);
        arrearSalaryItem2.setId(2L);
        assertThat(arrearSalaryItem1).isNotEqualTo(arrearSalaryItem2);
        arrearSalaryItem1.setId(null);
        assertThat(arrearSalaryItem1).isNotEqualTo(arrearSalaryItem2);
    }
}
