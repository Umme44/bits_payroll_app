package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecruitmentRequisitionBudgetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecruitmentRequisitionBudget.class);
        RecruitmentRequisitionBudget recruitmentRequisitionBudget1 = new RecruitmentRequisitionBudget();
        recruitmentRequisitionBudget1.setId(1L);
        RecruitmentRequisitionBudget recruitmentRequisitionBudget2 = new RecruitmentRequisitionBudget();
        recruitmentRequisitionBudget2.setId(recruitmentRequisitionBudget1.getId());
        assertThat(recruitmentRequisitionBudget1).isEqualTo(recruitmentRequisitionBudget2);
        recruitmentRequisitionBudget2.setId(2L);
        assertThat(recruitmentRequisitionBudget1).isNotEqualTo(recruitmentRequisitionBudget2);
        recruitmentRequisitionBudget1.setId(null);
        assertThat(recruitmentRequisitionBudget1).isNotEqualTo(recruitmentRequisitionBudget2);
    }
}
