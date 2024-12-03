package com.bits.hr.service.mapper;

import com.bits.hr.domain.EmploymentHistory;
import com.bits.hr.service.dto.EmploymentHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link EmploymentHistory} and its DTO {@link EmploymentHistoryDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { DesignationMapper.class, DepartmentMapper.class, EmployeeMapper.class, UnitMapper.class, BandMapper.class }
)
public interface EmploymentHistoryMapper extends EntityMapper<EmploymentHistoryDTO, EmploymentHistory> {
    @Mapping(source = "previousDesignation.id", target = "previousDesignationId")
    @Mapping(source = "changedDesignation.id", target = "changedDesignationId")
    @Mapping(source = "previousDepartment.id", target = "previousDepartmentId")
    @Mapping(source = "changedDepartment.id", target = "changedDepartmentId")
    @Mapping(source = "previousReportingTo.id", target = "previousReportingToId")
    @Mapping(source = "changedReportingTo.id", target = "changedReportingToId")
    @Mapping(source = "previousUnit.id", target = "previousUnitId")
    @Mapping(source = "changedUnit.id", target = "changedUnitId")
    @Mapping(source = "previousBand.id", target = "previousBandId")
    @Mapping(source = "changedBand.id", target = "changedBandId")
    @Mapping(source = "previousDesignation.designationName", target = "previousDesignationName")
    @Mapping(source = "changedDesignation.designationName", target = "changedDesignationName")
    @Mapping(source = "previousDepartment.departmentName", target = "previousDepartmentName")
    @Mapping(source = "changedDepartment.departmentName", target = "changedDepartmentName")
    @Mapping(source = "previousReportingTo.fullName", target = "previousReportingToName")
    @Mapping(source = "changedReportingTo.fullName", target = "changedReportingToName")
    @Mapping(source = "employee.fullName", target = "employeeName")
    @Mapping(source = "previousUnit.unitName", target = "previousUnitName")
    @Mapping(source = "changedUnit.unitName", target = "changedUnitName")
    @Mapping(source = "previousBand.bandName", target = "previousBandName")
    @Mapping(source = "changedBand.bandName", target = "changedBandName")
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.employeeCategory", target = "employeeCategory")
    @Mapping(source = "employee.location", target = "location")
    @Mapping(source = "employee.dateOfJoining", target = "dateOfJoining")
    @Mapping(source = "employee.dateOfConfirmation", target = "dateOfConfirmation")
    @Mapping(source = "employee.designation.designationName", target = "currentDesignationName")
    @Mapping(source = "employee.department.departmentName", target = "currentDepartmentName")
    @Mapping(source = "employee.band.bandName", target = "currentBandName")
    @Mapping(source = "employee.unit.unitName", target = "currentUnitName")
    @Mapping(source = "employee.reportingTo.fullName", target = "currentReportingToName")
    @Mapping(source = "employee.reportingTo.pin", target = "currentReportingToPIN")
    EmploymentHistoryDTO toDto(EmploymentHistory employmentHistory);

    @Mapping(source = "previousDesignationId", target = "previousDesignation")
    @Mapping(source = "changedDesignationId", target = "changedDesignation")
    @Mapping(source = "previousDepartmentId", target = "previousDepartment")
    @Mapping(source = "changedDepartmentId", target = "changedDepartment")
    @Mapping(source = "previousReportingToId", target = "previousReportingTo")
    @Mapping(source = "changedReportingToId", target = "changedReportingTo")
    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "previousUnitId", target = "previousUnit")
    @Mapping(source = "changedUnitId", target = "changedUnit")
    @Mapping(source = "previousBandId", target = "previousBand")
    @Mapping(source = "changedBandId", target = "changedBand")
    EmploymentHistory toEntity(EmploymentHistoryDTO employmentHistoryDTO);

    default EmploymentHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        EmploymentHistory employmentHistory = new EmploymentHistory();
        employmentHistory.setId(id);
        return employmentHistory;
    }
}
