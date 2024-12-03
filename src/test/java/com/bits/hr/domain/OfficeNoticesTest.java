package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OfficeNoticesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OfficeNotices.class);
        OfficeNotices officeNotices1 = new OfficeNotices();
        officeNotices1.setId(1L);
        OfficeNotices officeNotices2 = new OfficeNotices();
        officeNotices2.setId(officeNotices1.getId());
        assertThat(officeNotices1).isEqualTo(officeNotices2);
        officeNotices2.setId(2L);
        assertThat(officeNotices1).isNotEqualTo(officeNotices2);
        officeNotices1.setId(null);
        assertThat(officeNotices1).isNotEqualTo(officeNotices2);
    }
}
