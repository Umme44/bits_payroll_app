package com.bits.hr.service.mapper;

import com.bits.hr.domain.Department;
import com.bits.hr.service.dto.DepartmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Department} and its DTO {@link DepartmentDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface DepartmentMapper extends EntityMapper<DepartmentDTO, Department> {
    @Mapping(source = "departmentHead.id", target = "departmentHeadId")
    @Mapping(source = "departmentHead.fullName", target = "departmentHeadFullName")
    @Mapping(source = "departmentHead.pin", target = "departmentHeadPin")
    DepartmentDTO toDto(Department department);

    @Mapping(source = "departmentHeadId", target = "departmentHead")
    Department toEntity(DepartmentDTO departmentDTO);

    default Department fromId(Long id) {
        if (id == null) {
            return null;
        }
        Department department = new Department();
        department.setId(id);
        return department;
    }
}
