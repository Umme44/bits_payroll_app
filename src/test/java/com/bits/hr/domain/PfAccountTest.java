package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PfAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PfAccount.class);
        PfAccount pfAccount1 = new PfAccount();
        pfAccount1.setId(1L);
        PfAccount pfAccount2 = new PfAccount();
        pfAccount2.setId(pfAccount1.getId());
        assertThat(pfAccount1).isEqualTo(pfAccount2);
        pfAccount2.setId(2L);
        assertThat(pfAccount1).isNotEqualTo(pfAccount2);
        pfAccount1.setId(null);
        assertThat(pfAccount1).isNotEqualTo(pfAccount2);
    }
}
