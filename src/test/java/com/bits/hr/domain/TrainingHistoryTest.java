package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrainingHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingHistory.class);
        TrainingHistory trainingHistory1 = new TrainingHistory();
        trainingHistory1.setId(1L);
        TrainingHistory trainingHistory2 = new TrainingHistory();
        trainingHistory2.setId(trainingHistory1.getId());
        assertThat(trainingHistory1).isEqualTo(trainingHistory2);
        trainingHistory2.setId(2L);
        assertThat(trainingHistory1).isNotEqualTo(trainingHistory2);
        trainingHistory1.setId(null);
        assertThat(trainingHistory1).isNotEqualTo(trainingHistory2);
    }
}
