package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecruitmentRequisitionFormDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecruitmentRequisitionFormDTO.class);
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO1 = new RecruitmentRequisitionFormDTO();
        recruitmentRequisitionFormDTO1.setId(1L);
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO2 = new RecruitmentRequisitionFormDTO();
        assertThat(recruitmentRequisitionFormDTO1).isNotEqualTo(recruitmentRequisitionFormDTO2);
        recruitmentRequisitionFormDTO2.setId(recruitmentRequisitionFormDTO1.getId());
        assertThat(recruitmentRequisitionFormDTO1).isEqualTo(recruitmentRequisitionFormDTO2);
        recruitmentRequisitionFormDTO2.setId(2L);
        assertThat(recruitmentRequisitionFormDTO1).isNotEqualTo(recruitmentRequisitionFormDTO2);
        recruitmentRequisitionFormDTO1.setId(null);
        assertThat(recruitmentRequisitionFormDTO1).isNotEqualTo(recruitmentRequisitionFormDTO2);
    }
}
