package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeneratedSalaryHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeneratedSalaryHistory.class);
        GeneratedSalaryHistory generatedSalaryHistory1 = new GeneratedSalaryHistory();
        generatedSalaryHistory1.setId(1L);
        GeneratedSalaryHistory generatedSalaryHistory2 = new GeneratedSalaryHistory();
        generatedSalaryHistory2.setId(generatedSalaryHistory1.getId());
        assertThat(generatedSalaryHistory1).isEqualTo(generatedSalaryHistory2);
        generatedSalaryHistory2.setId(2L);
        assertThat(generatedSalaryHistory1).isNotEqualTo(generatedSalaryHistory2);
        generatedSalaryHistory1.setId(null);
        assertThat(generatedSalaryHistory1).isNotEqualTo(generatedSalaryHistory2);
    }
}
