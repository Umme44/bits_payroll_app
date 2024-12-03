package com.bits.hr.service.mapper;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.EmployeeDetailsNomineeReportDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { DesignationMapper.class, DepartmentMapper.class, BandMapper.class, UnitMapper.class }
)
public interface EmployeeDetailsMapperForPFNomineePrintPage extends EntityMapper<EmployeeDetailsNomineeReportDTO, Employee> {
    @Mapping(source = "designation.designationName", target = "designationName")
    @Mapping(source = "department.departmentName", target = "departmentName")
    @Mapping(source = "band.bandName", target = "bandName")
    @Mapping(source = "unit.unitName", target = "unitName")
    EmployeeDetailsNomineeReportDTO toDTO(Employee employee);

    @Override
    default List<EmployeeDetailsNomineeReportDTO> toDto(List<Employee> entityList) {
        return null;
    }

    default Employee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
