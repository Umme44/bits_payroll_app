package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HolidaysDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HolidaysDTO.class);
        HolidaysDTO holidaysDTO1 = new HolidaysDTO();
        holidaysDTO1.setId(1L);
        HolidaysDTO holidaysDTO2 = new HolidaysDTO();
        assertThat(holidaysDTO1).isNotEqualTo(holidaysDTO2);
        holidaysDTO2.setId(holidaysDTO1.getId());
        assertThat(holidaysDTO1).isEqualTo(holidaysDTO2);
        holidaysDTO2.setId(2L);
        assertThat(holidaysDTO1).isNotEqualTo(holidaysDTO2);
        holidaysDTO1.setId(null);
        assertThat(holidaysDTO1).isNotEqualTo(holidaysDTO2);
    }
}
