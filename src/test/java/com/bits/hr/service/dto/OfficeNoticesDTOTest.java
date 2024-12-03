package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OfficeNoticesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OfficeNoticesDTO.class);
        OfficeNoticesDTO officeNoticesDTO1 = new OfficeNoticesDTO();
        officeNoticesDTO1.setId(1L);
        OfficeNoticesDTO officeNoticesDTO2 = new OfficeNoticesDTO();
        assertThat(officeNoticesDTO1).isNotEqualTo(officeNoticesDTO2);
        officeNoticesDTO2.setId(officeNoticesDTO1.getId());
        assertThat(officeNoticesDTO1).isEqualTo(officeNoticesDTO2);
        officeNoticesDTO2.setId(2L);
        assertThat(officeNoticesDTO1).isNotEqualTo(officeNoticesDTO2);
        officeNoticesDTO1.setId(null);
        assertThat(officeNoticesDTO1).isNotEqualTo(officeNoticesDTO2);
    }
}
