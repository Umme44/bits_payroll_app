package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecruitmentRequisitionBudgetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecruitmentRequisitionBudgetDTO.class);
        RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO1 = new RecruitmentRequisitionBudgetDTO();
        recruitmentRequisitionBudgetDTO1.setId(1L);
        RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO2 = new RecruitmentRequisitionBudgetDTO();
        assertThat(recruitmentRequisitionBudgetDTO1).isNotEqualTo(recruitmentRequisitionBudgetDTO2);
        recruitmentRequisitionBudgetDTO2.setId(recruitmentRequisitionBudgetDTO1.getId());
        assertThat(recruitmentRequisitionBudgetDTO1).isEqualTo(recruitmentRequisitionBudgetDTO2);
        recruitmentRequisitionBudgetDTO2.setId(2L);
        assertThat(recruitmentRequisitionBudgetDTO1).isNotEqualTo(recruitmentRequisitionBudgetDTO2);
        recruitmentRequisitionBudgetDTO1.setId(null);
        assertThat(recruitmentRequisitionBudgetDTO1).isNotEqualTo(recruitmentRequisitionBudgetDTO2);
    }
}
