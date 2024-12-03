package com.bits.hr.service.mapper;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.EmployeeCommonDTO;
import com.bits.hr.service.dto.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {
        DesignationMapper.class,
        DepartmentMapper.class,
        NationalityMapper.class,
        BankBranchMapper.class,
        BandMapper.class,
        UnitMapper.class,
    }
)
public interface EmployeeCommonMapper extends EntityMapper<EmployeeCommonDTO, Employee> {
    @Mapping(source = "designation.designationName", target = "designationName")
    @Mapping(source = "designation.id", target = "designationId")
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.departmentName", target = "departmentName")
    @Mapping(source = "reportingTo.fullName", target = "reportingToName")
    @Mapping(source = "nationality.id", target = "nationalityId")
    @Mapping(source = "nationality.nationalityName", target = "nationalityNationalityName")
    @Mapping(source = "bankBranch.id", target = "bankBranchId")
    @Mapping(source = "bankBranch.branchName", target = "bankBranchName")
    @Mapping(source = "band.id", target = "bandId")
    @Mapping(source = "band.bandName", target = "bandName")
    @Mapping(source = "unit.unitName", target = "unitName")
    @Mapping(source = "unit.id", target = "unitId")
    @Mapping(source = "currentInTime", target = "currentInTime")
    @Mapping(source = "currentOutTime", target = "currentOutTime")
    @Mapping(source = "isCurrentlyResigned", target = "currentlyResigned")
    //    @Mapping(source = "isAllowedToGiveOnlineAttendance", target = "isAllowedToGiveOnlineAttendance")
    EmployeeCommonDTO toDto(Employee employee);

    //    @Mapping(source = "designationId", target = "designation")
    //    @Mapping(source = "departmentId", target = "department")
    //    @Mapping(source = "reportingToId", target = "reportingTo")
    //    @Mapping(source = "nationalityId", target = "nationality")
    //    @Mapping(source = "bankBranchId", target = "bankBranch")
    //    @Mapping(source = "bandId", target = "band")
    //    @Mapping(source = "unitId", target = "unit")
    //    @Mapping(source = "probationaryPeriodExtended", target = "isProbationaryPeriodExtended")
    //    Employee toEntity(EmployeeCommonDTO employeeCommonDTO);

    default Employee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
