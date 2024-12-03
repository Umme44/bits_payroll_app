package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.ProRataFestivalBonusDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link ProRataFestivalBonus} and its DTO {@link ProRataFestivalBonusDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface ProRataFestivalBonusMapper extends EntityMapper<ProRataFestivalBonusDTO, ProRataFestivalBonus> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    ProRataFestivalBonusDTO toDto(ProRataFestivalBonus proRataFestivalBonus);

    @Mapping(source = "employeeId", target = "employee")
    ProRataFestivalBonus toEntity(ProRataFestivalBonusDTO proRataFestivalBonusDTO);

    default ProRataFestivalBonus fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProRataFestivalBonus proRataFestivalBonus = new ProRataFestivalBonus();
        proRataFestivalBonus.setId(id);
        return proRataFestivalBonus;
    }
}
