package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArrearSalaryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArrearSalaryDTO.class);
        ArrearSalaryDTO arrearSalaryDTO1 = new ArrearSalaryDTO();
        arrearSalaryDTO1.setId(1L);
        ArrearSalaryDTO arrearSalaryDTO2 = new ArrearSalaryDTO();
        assertThat(arrearSalaryDTO1).isNotEqualTo(arrearSalaryDTO2);
        arrearSalaryDTO2.setId(arrearSalaryDTO1.getId());
        assertThat(arrearSalaryDTO1).isEqualTo(arrearSalaryDTO2);
        arrearSalaryDTO2.setId(2L);
        assertThat(arrearSalaryDTO1).isNotEqualTo(arrearSalaryDTO2);
        arrearSalaryDTO1.setId(null);
        assertThat(arrearSalaryDTO1).isNotEqualTo(arrearSalaryDTO2);
    }
}
