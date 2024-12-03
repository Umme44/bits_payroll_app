package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkFromHomeApplicationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkFromHomeApplication.class);
        WorkFromHomeApplication workFromHomeApplication1 = new WorkFromHomeApplication();
        workFromHomeApplication1.setId(1L);
        WorkFromHomeApplication workFromHomeApplication2 = new WorkFromHomeApplication();
        workFromHomeApplication2.setId(workFromHomeApplication1.getId());
        assertThat(workFromHomeApplication1).isEqualTo(workFromHomeApplication2);
        workFromHomeApplication2.setId(2L);
        assertThat(workFromHomeApplication1).isNotEqualTo(workFromHomeApplication2);
        workFromHomeApplication1.setId(null);
        assertThat(workFromHomeApplication1).isNotEqualTo(workFromHomeApplication2);
    }
}
