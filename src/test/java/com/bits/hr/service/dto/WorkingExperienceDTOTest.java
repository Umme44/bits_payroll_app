package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkingExperienceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkingExperienceDTO.class);
        WorkingExperienceDTO workingExperienceDTO1 = new WorkingExperienceDTO();
        workingExperienceDTO1.setId(1L);
        WorkingExperienceDTO workingExperienceDTO2 = new WorkingExperienceDTO();
        assertThat(workingExperienceDTO1).isNotEqualTo(workingExperienceDTO2);
        workingExperienceDTO2.setId(workingExperienceDTO1.getId());
        assertThat(workingExperienceDTO1).isEqualTo(workingExperienceDTO2);
        workingExperienceDTO2.setId(2L);
        assertThat(workingExperienceDTO1).isNotEqualTo(workingExperienceDTO2);
        workingExperienceDTO1.setId(null);
        assertThat(workingExperienceDTO1).isNotEqualTo(workingExperienceDTO2);
    }
}
