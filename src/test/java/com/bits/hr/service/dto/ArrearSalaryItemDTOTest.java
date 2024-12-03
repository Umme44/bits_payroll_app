package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArrearSalaryItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArrearSalaryItemDTO.class);
        ArrearSalaryItemDTO arrearSalaryItemDTO1 = new ArrearSalaryItemDTO();
        arrearSalaryItemDTO1.setId(1L);
        ArrearSalaryItemDTO arrearSalaryItemDTO2 = new ArrearSalaryItemDTO();
        assertThat(arrearSalaryItemDTO1).isNotEqualTo(arrearSalaryItemDTO2);
        arrearSalaryItemDTO2.setId(arrearSalaryItemDTO1.getId());
        assertThat(arrearSalaryItemDTO1).isEqualTo(arrearSalaryItemDTO2);
        arrearSalaryItemDTO2.setId(2L);
        assertThat(arrearSalaryItemDTO1).isNotEqualTo(arrearSalaryItemDTO2);
        arrearSalaryItemDTO1.setId(null);
        assertThat(arrearSalaryItemDTO1).isNotEqualTo(arrearSalaryItemDTO2);
    }
}
