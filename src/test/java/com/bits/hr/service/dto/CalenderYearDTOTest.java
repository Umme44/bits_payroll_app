package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CalenderYearDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CalenderYearDTO.class);
        CalenderYearDTO calenderYearDTO1 = new CalenderYearDTO();
        calenderYearDTO1.setId(1L);
        CalenderYearDTO calenderYearDTO2 = new CalenderYearDTO();
        assertThat(calenderYearDTO1).isNotEqualTo(calenderYearDTO2);
        calenderYearDTO2.setId(calenderYearDTO1.getId());
        assertThat(calenderYearDTO1).isEqualTo(calenderYearDTO2);
        calenderYearDTO2.setId(2L);
        assertThat(calenderYearDTO1).isNotEqualTo(calenderYearDTO2);
        calenderYearDTO1.setId(null);
        assertThat(calenderYearDTO1).isNotEqualTo(calenderYearDTO2);
    }
}
