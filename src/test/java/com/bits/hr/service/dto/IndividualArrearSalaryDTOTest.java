package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndividualArrearSalaryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndividualArrearSalaryDTO.class);
        IndividualArrearSalaryDTO individualArrearSalaryDTO1 = new IndividualArrearSalaryDTO();
        individualArrearSalaryDTO1.setId(1L);
        IndividualArrearSalaryDTO individualArrearSalaryDTO2 = new IndividualArrearSalaryDTO();
        assertThat(individualArrearSalaryDTO1).isNotEqualTo(individualArrearSalaryDTO2);
        individualArrearSalaryDTO2.setId(individualArrearSalaryDTO1.getId());
        assertThat(individualArrearSalaryDTO1).isEqualTo(individualArrearSalaryDTO2);
        individualArrearSalaryDTO2.setId(2L);
        assertThat(individualArrearSalaryDTO1).isNotEqualTo(individualArrearSalaryDTO2);
        individualArrearSalaryDTO1.setId(null);
        assertThat(individualArrearSalaryDTO1).isNotEqualTo(individualArrearSalaryDTO2);
    }
}
