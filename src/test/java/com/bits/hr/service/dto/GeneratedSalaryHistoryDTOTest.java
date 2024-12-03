package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeneratedSalaryHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeneratedSalaryHistoryDTO.class);
        GeneratedSalaryHistoryDTO generatedSalaryHistoryDTO1 = new GeneratedSalaryHistoryDTO();
        generatedSalaryHistoryDTO1.setId(1L);
        GeneratedSalaryHistoryDTO generatedSalaryHistoryDTO2 = new GeneratedSalaryHistoryDTO();
        assertThat(generatedSalaryHistoryDTO1).isNotEqualTo(generatedSalaryHistoryDTO2);
        generatedSalaryHistoryDTO2.setId(generatedSalaryHistoryDTO1.getId());
        assertThat(generatedSalaryHistoryDTO1).isEqualTo(generatedSalaryHistoryDTO2);
        generatedSalaryHistoryDTO2.setId(2L);
        assertThat(generatedSalaryHistoryDTO1).isNotEqualTo(generatedSalaryHistoryDTO2);
        generatedSalaryHistoryDTO1.setId(null);
        assertThat(generatedSalaryHistoryDTO1).isNotEqualTo(generatedSalaryHistoryDTO2);
    }
}
