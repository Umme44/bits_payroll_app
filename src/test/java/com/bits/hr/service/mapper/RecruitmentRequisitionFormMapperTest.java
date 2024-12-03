package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecruitmentRequisitionFormMapperTest {

    private RecruitmentRequisitionFormMapper recruitmentRequisitionFormMapper;

    @BeforeEach
    public void setUp() {
        recruitmentRequisitionFormMapper = new RecruitmentRequisitionFormMapperImpl();
    }
}
