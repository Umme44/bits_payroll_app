package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalaryGeneratorMasterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaryGeneratorMasterDTO.class);
        SalaryGeneratorMasterDTO salaryGeneratorMasterDTO1 = new SalaryGeneratorMasterDTO();
        salaryGeneratorMasterDTO1.setId(1L);
        SalaryGeneratorMasterDTO salaryGeneratorMasterDTO2 = new SalaryGeneratorMasterDTO();
        assertThat(salaryGeneratorMasterDTO1).isNotEqualTo(salaryGeneratorMasterDTO2);
        salaryGeneratorMasterDTO2.setId(salaryGeneratorMasterDTO1.getId());
        assertThat(salaryGeneratorMasterDTO1).isEqualTo(salaryGeneratorMasterDTO2);
        salaryGeneratorMasterDTO2.setId(2L);
        assertThat(salaryGeneratorMasterDTO1).isNotEqualTo(salaryGeneratorMasterDTO2);
        salaryGeneratorMasterDTO1.setId(null);
        assertThat(salaryGeneratorMasterDTO1).isNotEqualTo(salaryGeneratorMasterDTO2);
    }
}
