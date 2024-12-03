package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeductionTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeductionTypeDTO.class);
        DeductionTypeDTO deductionTypeDTO1 = new DeductionTypeDTO();
        deductionTypeDTO1.setId(1L);
        DeductionTypeDTO deductionTypeDTO2 = new DeductionTypeDTO();
        assertThat(deductionTypeDTO1).isNotEqualTo(deductionTypeDTO2);
        deductionTypeDTO2.setId(deductionTypeDTO1.getId());
        assertThat(deductionTypeDTO1).isEqualTo(deductionTypeDTO2);
        deductionTypeDTO2.setId(2L);
        assertThat(deductionTypeDTO1).isNotEqualTo(deductionTypeDTO2);
        deductionTypeDTO1.setId(null);
        assertThat(deductionTypeDTO1).isNotEqualTo(deductionTypeDTO2);
    }
}
