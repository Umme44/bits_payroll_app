package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserFeedbackDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserFeedbackDTO.class);
        UserFeedbackDTO userFeedbackDTO1 = new UserFeedbackDTO();
        userFeedbackDTO1.setId(1L);
        UserFeedbackDTO userFeedbackDTO2 = new UserFeedbackDTO();
        assertThat(userFeedbackDTO1).isNotEqualTo(userFeedbackDTO2);
        userFeedbackDTO2.setId(userFeedbackDTO1.getId());
        assertThat(userFeedbackDTO1).isEqualTo(userFeedbackDTO2);
        userFeedbackDTO2.setId(2L);
        assertThat(userFeedbackDTO1).isNotEqualTo(userFeedbackDTO2);
        userFeedbackDTO1.setId(null);
        assertThat(userFeedbackDTO1).isNotEqualTo(userFeedbackDTO2);
    }
}
