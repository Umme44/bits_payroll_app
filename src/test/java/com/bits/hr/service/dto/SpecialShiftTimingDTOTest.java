package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialShiftTimingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialShiftTimingDTO.class);
        SpecialShiftTimingDTO specialShiftTimingDTO1 = new SpecialShiftTimingDTO();
        specialShiftTimingDTO1.setId(1L);
        SpecialShiftTimingDTO specialShiftTimingDTO2 = new SpecialShiftTimingDTO();
        assertThat(specialShiftTimingDTO1).isNotEqualTo(specialShiftTimingDTO2);
        specialShiftTimingDTO2.setId(specialShiftTimingDTO1.getId());
        assertThat(specialShiftTimingDTO1).isEqualTo(specialShiftTimingDTO2);
        specialShiftTimingDTO2.setId(2L);
        assertThat(specialShiftTimingDTO1).isNotEqualTo(specialShiftTimingDTO2);
        specialShiftTimingDTO1.setId(null);
        assertThat(specialShiftTimingDTO1).isNotEqualTo(specialShiftTimingDTO2);
    }
}
