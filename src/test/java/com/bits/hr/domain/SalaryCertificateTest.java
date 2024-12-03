package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalaryCertificateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaryCertificate.class);
        SalaryCertificate salaryCertificate1 = new SalaryCertificate();
        salaryCertificate1.setId(1L);
        SalaryCertificate salaryCertificate2 = new SalaryCertificate();
        salaryCertificate2.setId(salaryCertificate1.getId());
        assertThat(salaryCertificate1).isEqualTo(salaryCertificate2);
        salaryCertificate2.setId(2L);
        assertThat(salaryCertificate1).isNotEqualTo(salaryCertificate2);
        salaryCertificate1.setId(null);
        assertThat(salaryCertificate1).isNotEqualTo(salaryCertificate2);
    }
}
