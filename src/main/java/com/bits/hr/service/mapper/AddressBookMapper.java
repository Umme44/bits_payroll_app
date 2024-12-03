package com.bits.hr.service.mapper;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.AddressBookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the BloodGroupInfoDTO
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface AddressBookMapper extends EntityMapper<AddressBookDTO, Employee> {
    @Mapping(source = "pin", target = "pin")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "designation.designationName", target = "designationName")
    @Mapping(source = "department.departmentName", target = "departmentName")
    @Mapping(source = "unit.unitName", target = "unitName")
    @Mapping(source = "bloodGroup", target = "bloodGroup")
    @Mapping(source = "officialContactNo", target = "officialContactNo")
    @Mapping(source = "officialEmail", target = "officialEmail")
    @Mapping(source = "gender", target = "gender")
    AddressBookDTO toDto(Employee employee);

    default Employee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
