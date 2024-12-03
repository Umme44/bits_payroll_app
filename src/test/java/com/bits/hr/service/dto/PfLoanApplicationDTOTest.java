package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PfLoanApplicationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PfLoanApplicationDTO.class);
        PfLoanApplicationDTO pfLoanApplicationDTO1 = new PfLoanApplicationDTO();
        pfLoanApplicationDTO1.setId(1L);
        PfLoanApplicationDTO pfLoanApplicationDTO2 = new PfLoanApplicationDTO();
        assertThat(pfLoanApplicationDTO1).isNotEqualTo(pfLoanApplicationDTO2);
        pfLoanApplicationDTO2.setId(pfLoanApplicationDTO1.getId());
        assertThat(pfLoanApplicationDTO1).isEqualTo(pfLoanApplicationDTO2);
        pfLoanApplicationDTO2.setId(2L);
        assertThat(pfLoanApplicationDTO1).isNotEqualTo(pfLoanApplicationDTO2);
        pfLoanApplicationDTO1.setId(null);
        assertThat(pfLoanApplicationDTO1).isNotEqualTo(pfLoanApplicationDTO2);
    }
}
