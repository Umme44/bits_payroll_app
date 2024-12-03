package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AitPaymentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AitPaymentDTO.class);
        AitPaymentDTO aitPaymentDTO1 = new AitPaymentDTO();
        aitPaymentDTO1.setId(1L);
        AitPaymentDTO aitPaymentDTO2 = new AitPaymentDTO();
        assertThat(aitPaymentDTO1).isNotEqualTo(aitPaymentDTO2);
        aitPaymentDTO2.setId(aitPaymentDTO1.getId());
        assertThat(aitPaymentDTO1).isEqualTo(aitPaymentDTO2);
        aitPaymentDTO2.setId(2L);
        assertThat(aitPaymentDTO1).isNotEqualTo(aitPaymentDTO2);
        aitPaymentDTO1.setId(null);
        assertThat(aitPaymentDTO1).isNotEqualTo(aitPaymentDTO2);
    }
}
