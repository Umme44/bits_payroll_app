package com.bits.hr.service.mapper;

import com.bits.hr.domain.EducationDetails;
import com.bits.hr.service.dto.EducationDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link EducationDetails} and its DTO {@link EducationDetailsDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface EducationDetailsMapper extends EntityMapper<EducationDetailsDTO, EducationDetails> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "employeeName")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    EducationDetailsDTO toDto(EducationDetails educationDetails);

    @Mapping(source = "employeeId", target = "employee")
    EducationDetails toEntity(EducationDetailsDTO educationDetailsDTO);

    default EducationDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        EducationDetails educationDetails = new EducationDetails();
        educationDetails.setId(id);
        return educationDetails;
    }
}
