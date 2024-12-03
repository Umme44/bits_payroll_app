package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BandDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BandDTO.class);
        BandDTO bandDTO1 = new BandDTO();
        bandDTO1.setId(1L);
        BandDTO bandDTO2 = new BandDTO();
        assertThat(bandDTO1).isNotEqualTo(bandDTO2);
        bandDTO2.setId(bandDTO1.getId());
        assertThat(bandDTO1).isEqualTo(bandDTO2);
        bandDTO2.setId(2L);
        assertThat(bandDTO1).isNotEqualTo(bandDTO2);
        bandDTO1.setId(null);
        assertThat(bandDTO1).isNotEqualTo(bandDTO2);
    }
}
