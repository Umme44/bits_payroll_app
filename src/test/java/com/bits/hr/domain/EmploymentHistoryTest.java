package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmploymentHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmploymentHistory.class);
        EmploymentHistory employmentHistory1 = new EmploymentHistory();
        employmentHistory1.setId(1L);
        EmploymentHistory employmentHistory2 = new EmploymentHistory();
        employmentHistory2.setId(employmentHistory1.getId());
        assertThat(employmentHistory1).isEqualTo(employmentHistory2);
        employmentHistory2.setId(2L);
        assertThat(employmentHistory1).isNotEqualTo(employmentHistory2);
        employmentHistory1.setId(null);
        assertThat(employmentHistory1).isNotEqualTo(employmentHistory2);
    }
}
