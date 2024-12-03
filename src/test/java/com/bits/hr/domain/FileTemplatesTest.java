package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FileTemplatesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileTemplates.class);
        FileTemplates fileTemplates1 = new FileTemplates();
        fileTemplates1.setId(1L);
        FileTemplates fileTemplates2 = new FileTemplates();
        fileTemplates2.setId(fileTemplates1.getId());
        assertThat(fileTemplates1).isEqualTo(fileTemplates2);
        fileTemplates2.setId(2L);
        assertThat(fileTemplates1).isNotEqualTo(fileTemplates2);
        fileTemplates1.setId(null);
        assertThat(fileTemplates1).isNotEqualTo(fileTemplates2);
    }
}
