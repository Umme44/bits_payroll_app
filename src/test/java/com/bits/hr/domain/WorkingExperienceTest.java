package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkingExperienceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkingExperience.class);
        WorkingExperience workingExperience1 = new WorkingExperience();
        workingExperience1.setId(1L);
        WorkingExperience workingExperience2 = new WorkingExperience();
        workingExperience2.setId(workingExperience1.getId());
        assertThat(workingExperience1).isEqualTo(workingExperience2);
        workingExperience2.setId(2L);
        assertThat(workingExperience1).isNotEqualTo(workingExperience2);
        workingExperience1.setId(null);
        assertThat(workingExperience1).isNotEqualTo(workingExperience2);
    }
}
