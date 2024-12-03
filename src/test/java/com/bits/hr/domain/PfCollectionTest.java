package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PfCollectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PfCollection.class);
        PfCollection pfCollection1 = new PfCollection();
        pfCollection1.setId(1L);
        PfCollection pfCollection2 = new PfCollection();
        pfCollection2.setId(pfCollection1.getId());
        assertThat(pfCollection1).isEqualTo(pfCollection2);
        pfCollection2.setId(2L);
        assertThat(pfCollection1).isNotEqualTo(pfCollection2);
        pfCollection1.setId(null);
        assertThat(pfCollection1).isNotEqualTo(pfCollection2);
    }
}
