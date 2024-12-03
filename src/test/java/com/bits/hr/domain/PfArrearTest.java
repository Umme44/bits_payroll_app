package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PfArrearTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PfArrear.class);
        PfArrear pfArrear1 = new PfArrear();
        pfArrear1.setId(1L);
        PfArrear pfArrear2 = new PfArrear();
        pfArrear2.setId(pfArrear1.getId());
        assertThat(pfArrear1).isEqualTo(pfArrear2);
        pfArrear2.setId(2L);
        assertThat(pfArrear1).isNotEqualTo(pfArrear2);
        pfArrear1.setId(null);
        assertThat(pfArrear1).isNotEqualTo(pfArrear2);
    }
}
