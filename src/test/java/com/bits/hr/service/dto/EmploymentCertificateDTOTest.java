package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmploymentCertificateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmploymentCertificateDTO.class);
        EmploymentCertificateDTO employmentCertificateDTO1 = new EmploymentCertificateDTO();
        employmentCertificateDTO1.setId(1L);
        EmploymentCertificateDTO employmentCertificateDTO2 = new EmploymentCertificateDTO();
        assertThat(employmentCertificateDTO1).isNotEqualTo(employmentCertificateDTO2);
        employmentCertificateDTO2.setId(employmentCertificateDTO1.getId());
        assertThat(employmentCertificateDTO1).isEqualTo(employmentCertificateDTO2);
        employmentCertificateDTO2.setId(2L);
        assertThat(employmentCertificateDTO1).isNotEqualTo(employmentCertificateDTO2);
        employmentCertificateDTO1.setId(null);
        assertThat(employmentCertificateDTO1).isNotEqualTo(employmentCertificateDTO2);
    }
}
