package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.EmployeeSalaryTempDataDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link EmployeeSalaryTempData} and its DTO {@link EmployeeSalaryTempDataDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface EmployeeSalaryTempDataMapper extends EntityMapper<EmployeeSalaryTempDataDTO, EmployeeSalaryTempData> {
    @Mapping(source = "employee.id", target = "employeeId")
    EmployeeSalaryTempDataDTO toDto(EmployeeSalaryTempData employeeSalaryTempData);

    @Mapping(source = "employeeId", target = "employee")
    EmployeeSalaryTempData toEntity(EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO);

    default EmployeeSalaryTempData fromId(Long id) {
        if (id == null) {
            return null;
        }
        EmployeeSalaryTempData employeeSalaryTempData = new EmployeeSalaryTempData();
        employeeSalaryTempData.setId(id);
        return employeeSalaryTempData;
    }
}
