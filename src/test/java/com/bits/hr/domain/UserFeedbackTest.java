package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserFeedbackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserFeedback.class);
        UserFeedback userFeedback1 = new UserFeedback();
        userFeedback1.setId(1L);
        UserFeedback userFeedback2 = new UserFeedback();
        userFeedback2.setId(userFeedback1.getId());
        assertThat(userFeedback1).isEqualTo(userFeedback2);
        userFeedback2.setId(2L);
        assertThat(userFeedback1).isNotEqualTo(userFeedback2);
        userFeedback1.setId(null);
        assertThat(userFeedback1).isNotEqualTo(userFeedback2);
    }
}
