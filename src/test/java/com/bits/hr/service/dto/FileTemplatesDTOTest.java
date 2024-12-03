package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FileTemplatesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileTemplatesDTO.class);
        FileTemplatesDTO fileTemplatesDTO1 = new FileTemplatesDTO();
        fileTemplatesDTO1.setId(1L);
        FileTemplatesDTO fileTemplatesDTO2 = new FileTemplatesDTO();
        assertThat(fileTemplatesDTO1).isNotEqualTo(fileTemplatesDTO2);
        fileTemplatesDTO2.setId(fileTemplatesDTO1.getId());
        assertThat(fileTemplatesDTO1).isEqualTo(fileTemplatesDTO2);
        fileTemplatesDTO2.setId(2L);
        assertThat(fileTemplatesDTO1).isNotEqualTo(fileTemplatesDTO2);
        fileTemplatesDTO1.setId(null);
        assertThat(fileTemplatesDTO1).isNotEqualTo(fileTemplatesDTO2);
    }
}
