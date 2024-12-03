package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalaryCertificateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaryCertificateDTO.class);
        SalaryCertificateDTO salaryCertificateDTO1 = new SalaryCertificateDTO();
        salaryCertificateDTO1.setId(1L);
        SalaryCertificateDTO salaryCertificateDTO2 = new SalaryCertificateDTO();
        assertThat(salaryCertificateDTO1).isNotEqualTo(salaryCertificateDTO2);
        salaryCertificateDTO2.setId(salaryCertificateDTO1.getId());
        assertThat(salaryCertificateDTO1).isEqualTo(salaryCertificateDTO2);
        salaryCertificateDTO2.setId(2L);
        assertThat(salaryCertificateDTO1).isNotEqualTo(salaryCertificateDTO2);
        salaryCertificateDTO1.setId(null);
        assertThat(salaryCertificateDTO1).isNotEqualTo(salaryCertificateDTO2);
    }
}
