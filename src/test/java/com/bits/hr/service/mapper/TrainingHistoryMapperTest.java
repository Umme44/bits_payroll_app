package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrainingHistoryMapperTest {

    private TrainingHistoryMapper trainingHistoryMapper;

    @BeforeEach
    public void setUp() {
        trainingHistoryMapper = new TrainingHistoryMapperImpl();
    }
}
