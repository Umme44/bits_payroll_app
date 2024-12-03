package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmploymentHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmploymentHistoryDTO.class);
        EmploymentHistoryDTO employmentHistoryDTO1 = new EmploymentHistoryDTO();
        employmentHistoryDTO1.setId(1L);
        EmploymentHistoryDTO employmentHistoryDTO2 = new EmploymentHistoryDTO();
        assertThat(employmentHistoryDTO1).isNotEqualTo(employmentHistoryDTO2);
        employmentHistoryDTO2.setId(employmentHistoryDTO1.getId());
        assertThat(employmentHistoryDTO1).isEqualTo(employmentHistoryDTO2);
        employmentHistoryDTO2.setId(2L);
        assertThat(employmentHistoryDTO1).isNotEqualTo(employmentHistoryDTO2);
        employmentHistoryDTO1.setId(null);
        assertThat(employmentHistoryDTO1).isNotEqualTo(employmentHistoryDTO2);
    }
}
