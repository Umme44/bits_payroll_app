package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArrearSalaryMasterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArrearSalaryMasterDTO.class);
        ArrearSalaryMasterDTO arrearSalaryMasterDTO1 = new ArrearSalaryMasterDTO();
        arrearSalaryMasterDTO1.setId(1L);
        ArrearSalaryMasterDTO arrearSalaryMasterDTO2 = new ArrearSalaryMasterDTO();
        assertThat(arrearSalaryMasterDTO1).isNotEqualTo(arrearSalaryMasterDTO2);
        arrearSalaryMasterDTO2.setId(arrearSalaryMasterDTO1.getId());
        assertThat(arrearSalaryMasterDTO1).isEqualTo(arrearSalaryMasterDTO2);
        arrearSalaryMasterDTO2.setId(2L);
        assertThat(arrearSalaryMasterDTO1).isNotEqualTo(arrearSalaryMasterDTO2);
        arrearSalaryMasterDTO1.setId(null);
        assertThat(arrearSalaryMasterDTO1).isNotEqualTo(arrearSalaryMasterDTO2);
    }
}
