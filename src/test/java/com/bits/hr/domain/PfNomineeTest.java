package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PfNomineeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PfNominee.class);
        PfNominee pfNominee1 = new PfNominee();
        pfNominee1.setId(1L);
        PfNominee pfNominee2 = new PfNominee();
        pfNominee2.setId(pfNominee1.getId());
        assertThat(pfNominee1).isEqualTo(pfNominee2);
        pfNominee2.setId(2L);
        assertThat(pfNominee1).isNotEqualTo(pfNominee2);
        pfNominee1.setId(null);
        assertThat(pfNominee1).isNotEqualTo(pfNominee2);
    }
}
