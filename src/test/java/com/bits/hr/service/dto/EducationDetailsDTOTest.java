package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EducationDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EducationDetailsDTO.class);
        EducationDetailsDTO educationDetailsDTO1 = new EducationDetailsDTO();
        educationDetailsDTO1.setId(1L);
        EducationDetailsDTO educationDetailsDTO2 = new EducationDetailsDTO();
        assertThat(educationDetailsDTO1).isNotEqualTo(educationDetailsDTO2);
        educationDetailsDTO2.setId(educationDetailsDTO1.getId());
        assertThat(educationDetailsDTO1).isEqualTo(educationDetailsDTO2);
        educationDetailsDTO2.setId(2L);
        assertThat(educationDetailsDTO1).isNotEqualTo(educationDetailsDTO2);
        educationDetailsDTO1.setId(null);
        assertThat(educationDetailsDTO1).isNotEqualTo(educationDetailsDTO2);
    }
}
