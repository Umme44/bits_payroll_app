package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkFromHomeApplicationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkFromHomeApplicationDTO.class);
        WorkFromHomeApplicationDTO workFromHomeApplicationDTO1 = new WorkFromHomeApplicationDTO();
        workFromHomeApplicationDTO1.setId(1L);
        WorkFromHomeApplicationDTO workFromHomeApplicationDTO2 = new WorkFromHomeApplicationDTO();
        assertThat(workFromHomeApplicationDTO1).isNotEqualTo(workFromHomeApplicationDTO2);
        workFromHomeApplicationDTO2.setId(workFromHomeApplicationDTO1.getId());
        assertThat(workFromHomeApplicationDTO1).isEqualTo(workFromHomeApplicationDTO2);
        workFromHomeApplicationDTO2.setId(2L);
        assertThat(workFromHomeApplicationDTO1).isNotEqualTo(workFromHomeApplicationDTO2);
        workFromHomeApplicationDTO1.setId(null);
        assertThat(workFromHomeApplicationDTO1).isNotEqualTo(workFromHomeApplicationDTO2);
    }
}
