package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserFeedbackMapperTest {

    private UserFeedbackMapper userFeedbackMapper;

    @BeforeEach
    public void setUp() {
        userFeedbackMapper = new UserFeedbackMapperImpl();
    }
}
