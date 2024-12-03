package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinalSettlementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinalSettlementDTO.class);
        FinalSettlementDTO finalSettlementDTO1 = new FinalSettlementDTO();
        finalSettlementDTO1.setId(1L);
        FinalSettlementDTO finalSettlementDTO2 = new FinalSettlementDTO();
        assertThat(finalSettlementDTO1).isNotEqualTo(finalSettlementDTO2);
        finalSettlementDTO2.setId(finalSettlementDTO1.getId());
        assertThat(finalSettlementDTO1).isEqualTo(finalSettlementDTO2);
        finalSettlementDTO2.setId(2L);
        assertThat(finalSettlementDTO1).isNotEqualTo(finalSettlementDTO2);
        finalSettlementDTO1.setId(null);
        assertThat(finalSettlementDTO1).isNotEqualTo(finalSettlementDTO2);
    }
}
