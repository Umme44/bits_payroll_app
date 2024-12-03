package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmploymentCertificateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmploymentCertificate.class);
        EmploymentCertificate employmentCertificate1 = new EmploymentCertificate();
        employmentCertificate1.setId(1L);
        EmploymentCertificate employmentCertificate2 = new EmploymentCertificate();
        employmentCertificate2.setId(employmentCertificate1.getId());
        assertThat(employmentCertificate1).isEqualTo(employmentCertificate2);
        employmentCertificate2.setId(2L);
        assertThat(employmentCertificate1).isNotEqualTo(employmentCertificate2);
        employmentCertificate1.setId(null);
        assertThat(employmentCertificate1).isNotEqualTo(employmentCertificate2);
    }
}
