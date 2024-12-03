package com.bits.hr.service.mapper;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.EmployeeApprovalDTO;
import com.bits.hr.service.dto.EmployeeCommonDTO;
import com.bits.hr.service.dto.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { DesignationMapper.class })
public interface EmployeeApprovalMapper extends EntityMapper<EmployeeApprovalDTO, Employee> {
    @Mapping(source = "designation.designationName", target = "designationName")
    @Mapping(source = "currentInTime", target = "currentInTime")
    @Mapping(source = "currentOutTime", target = "currentOutTime")
    EmployeeApprovalDTO toDto(Employee employee);

    default Employee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
