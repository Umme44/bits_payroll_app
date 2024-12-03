package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArrearPaymentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArrearPaymentDTO.class);
        ArrearPaymentDTO arrearPaymentDTO1 = new ArrearPaymentDTO();
        arrearPaymentDTO1.setId(1L);
        ArrearPaymentDTO arrearPaymentDTO2 = new ArrearPaymentDTO();
        assertThat(arrearPaymentDTO1).isNotEqualTo(arrearPaymentDTO2);
        arrearPaymentDTO2.setId(arrearPaymentDTO1.getId());
        assertThat(arrearPaymentDTO1).isEqualTo(arrearPaymentDTO2);
        arrearPaymentDTO2.setId(2L);
        assertThat(arrearPaymentDTO1).isNotEqualTo(arrearPaymentDTO2);
        arrearPaymentDTO1.setId(null);
        assertThat(arrearPaymentDTO1).isNotEqualTo(arrearPaymentDTO2);
    }
}
