package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalaryDeductionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaryDeductionDTO.class);
        SalaryDeductionDTO salaryDeductionDTO1 = new SalaryDeductionDTO();
        salaryDeductionDTO1.setId(1L);
        SalaryDeductionDTO salaryDeductionDTO2 = new SalaryDeductionDTO();
        assertThat(salaryDeductionDTO1).isNotEqualTo(salaryDeductionDTO2);
        salaryDeductionDTO2.setId(salaryDeductionDTO1.getId());
        assertThat(salaryDeductionDTO1).isEqualTo(salaryDeductionDTO2);
        salaryDeductionDTO2.setId(2L);
        assertThat(salaryDeductionDTO1).isNotEqualTo(salaryDeductionDTO2);
        salaryDeductionDTO1.setId(null);
        assertThat(salaryDeductionDTO1).isNotEqualTo(salaryDeductionDTO2);
    }
}
