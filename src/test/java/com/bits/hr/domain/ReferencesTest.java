package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReferencesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(References.class);
        References references1 = new References();
        references1.setId(1L);
        References references2 = new References();
        references2.setId(references1.getId());
        assertThat(references1).isEqualTo(references2);
        references2.setId(2L);
        assertThat(references1).isNotEqualTo(references2);
        references1.setId(null);
        assertThat(references1).isNotEqualTo(references2);
    }
}
