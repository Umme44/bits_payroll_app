package com.bits.hr.service.mapper;

import com.bits.hr.domain.MobileBill;
import com.bits.hr.service.dto.MobileBillDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link MobileBill} and its DTO {@link MobileBillDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface MobileBillMapper extends EntityMapper<MobileBillDTO, MobileBill> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    MobileBillDTO toDto(MobileBill mobileBill);

    @Mapping(source = "employeeId", target = "employee")
    MobileBill toEntity(MobileBillDTO mobileBillDTO);

    default MobileBill fromId(Long id) {
        if (id == null) {
            return null;
        }
        MobileBill mobileBill = new MobileBill();
        mobileBill.setId(id);
        return mobileBill;
    }
}
