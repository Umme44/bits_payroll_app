package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.ArrearSalaryItemDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link ArrearSalaryItem} and its DTO {@link ArrearSalaryItemDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { ArrearSalaryMasterMapper.class, EmployeeMapper.class }
)
public interface ArrearSalaryItemMapper extends EntityMapper<ArrearSalaryItemDTO, ArrearSalaryItem> {
    @Mapping(source = "arrearSalaryMaster.id", target = "arrearSalaryMasterId")
    @Mapping(source = "arrearSalaryMaster.title", target = "arrearSalaryMasterTitle")
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.fullName", target = "employeeName")
    @Mapping(source = "employee.pin", target = "pin")
    ArrearSalaryItemDTO toDto(ArrearSalaryItem arrearSalaryItem);

    @Mapping(source = "arrearSalaryMasterId", target = "arrearSalaryMaster")
    @Mapping(source = "employeeId", target = "employee")
    ArrearSalaryItem toEntity(ArrearSalaryItemDTO arrearSalaryItemDTO);

    default ArrearSalaryItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        ArrearSalaryItem arrearSalaryItem = new ArrearSalaryItem();
        arrearSalaryItem.setId(id);
        return arrearSalaryItem;
    }
}
