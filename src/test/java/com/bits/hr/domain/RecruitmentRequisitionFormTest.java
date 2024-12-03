package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecruitmentRequisitionFormTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecruitmentRequisitionForm.class);
        RecruitmentRequisitionForm recruitmentRequisitionForm1 = new RecruitmentRequisitionForm();
        recruitmentRequisitionForm1.setId(1L);
        RecruitmentRequisitionForm recruitmentRequisitionForm2 = new RecruitmentRequisitionForm();
        recruitmentRequisitionForm2.setId(recruitmentRequisitionForm1.getId());
        assertThat(recruitmentRequisitionForm1).isEqualTo(recruitmentRequisitionForm2);
        recruitmentRequisitionForm2.setId(2L);
        assertThat(recruitmentRequisitionForm1).isNotEqualTo(recruitmentRequisitionForm2);
        recruitmentRequisitionForm1.setId(null);
        assertThat(recruitmentRequisitionForm1).isNotEqualTo(recruitmentRequisitionForm2);
    }
}
