package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrainingHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingHistoryDTO.class);
        TrainingHistoryDTO trainingHistoryDTO1 = new TrainingHistoryDTO();
        trainingHistoryDTO1.setId(1L);
        TrainingHistoryDTO trainingHistoryDTO2 = new TrainingHistoryDTO();
        assertThat(trainingHistoryDTO1).isNotEqualTo(trainingHistoryDTO2);
        trainingHistoryDTO2.setId(trainingHistoryDTO1.getId());
        assertThat(trainingHistoryDTO1).isEqualTo(trainingHistoryDTO2);
        trainingHistoryDTO2.setId(2L);
        assertThat(trainingHistoryDTO1).isNotEqualTo(trainingHistoryDTO2);
        trainingHistoryDTO1.setId(null);
        assertThat(trainingHistoryDTO1).isNotEqualTo(trainingHistoryDTO2);
    }
}
