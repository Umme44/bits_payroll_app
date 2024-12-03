package com.bits.hr.service.mapper;


import com.bits.hr.domain.*;
import com.bits.hr.service.dto.RecruitmentRequisitionBudgetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link RecruitmentRequisitionBudget} and its DTO {@link RecruitmentRequisitionBudgetDTO}.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, DepartmentMapper.class})
public interface RecruitmentRequisitionBudgetMapper extends EntityMapper<RecruitmentRequisitionBudgetDTO, RecruitmentRequisitionBudget> {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.fullName", target = "employeeFullName")
    @Mapping(source = "employee.pin", target = "employeePin")
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.departmentName", target = "departmentName")
    RecruitmentRequisitionBudgetDTO toDto(RecruitmentRequisitionBudget recruitmentRequisitionBudget);

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "departmentId", target = "department")
    RecruitmentRequisitionBudget toEntity(RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO);

    default RecruitmentRequisitionBudget fromId(Long id) {
        if (id == null) {
            return null;
        }
        RecruitmentRequisitionBudget recruitmentRequisitionBudget = new RecruitmentRequisitionBudget();
        recruitmentRequisitionBudget.setId(id);
        return recruitmentRequisitionBudget;
    }
}
