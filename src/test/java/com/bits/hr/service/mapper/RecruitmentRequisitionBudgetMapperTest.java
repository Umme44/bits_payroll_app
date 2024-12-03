package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.domain.RecruitmentRequisitionBudget;
import com.bits.hr.service.dto.RecruitmentRequisitionBudgetDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RecruitmentRequisitionBudgetMapperTest {

    private RecruitmentRequisitionBudgetMapper recruitmentRequisitionBudgetMapper;

    @BeforeEach
    public void setUp() {
        recruitmentRequisitionBudgetMapper = new RecruitmentRequisitionBudgetMapper() {
            @Override
            public RecruitmentRequisitionBudgetDTO toDto(RecruitmentRequisitionBudget recruitmentRequisitionBudget) {
                return null;
            }

            @Override
            public RecruitmentRequisitionBudget toEntity(RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO) {
                return null;
            }

            @Override
            public RecruitmentRequisitionBudget fromId(Long id) {
                return RecruitmentRequisitionBudgetMapper.super.fromId(id);
            }

            @Override
            public List<RecruitmentRequisitionBudget> toEntity(List<RecruitmentRequisitionBudgetDTO> dtoList) {
                return null;
            }

            @Override
            public List<RecruitmentRequisitionBudgetDTO> toDto(List<RecruitmentRequisitionBudget> entityList) {
                return null;
            }

            @Override
            public void partialUpdate(RecruitmentRequisitionBudget entity, RecruitmentRequisitionBudgetDTO dto) {

            }
        };
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(recruitmentRequisitionBudgetMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(recruitmentRequisitionBudgetMapper.fromId(null)).isNull();
    }
}
