package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.EmployeeStaticFileDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link EmployeeStaticFile} and its DTO {@link EmployeeStaticFileDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface EmployeeStaticFileMapper extends EntityMapper<EmployeeStaticFileDTO, EmployeeStaticFile> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(target = "getByteStreamFromFilePath", ignore = true)
    EmployeeStaticFileDTO toDto(EmployeeStaticFile employeeStaticFile);

    @Mapping(source = "employeeId", target = "employee")
    EmployeeStaticFile toEntity(EmployeeStaticFileDTO employeeStaticFileDTO);

    default EmployeeStaticFile fromId(Long id) {
        if (id == null) {
            return null;
        }
        EmployeeStaticFile employeeStaticFile = new EmployeeStaticFile();
        employeeStaticFile.setId(id);
        return employeeStaticFile;
    }
}
