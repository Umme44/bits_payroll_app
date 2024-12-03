package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.FinalSettlementDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link FinalSettlement} and its DTO {@link FinalSettlementDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { EmployeeMapper.class, UserMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface FinalSettlementMapper extends EntityMapper<FinalSettlementDTO, FinalSettlement> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.fullName", target = "employeeFullName")
    @Mapping(source = "employee.pin", target = "employeePin")
    @Mapping(source = "employee.dateOfJoining", target = "dateOfJoining")
    @Mapping(source = "employee.dateOfConfirmation", target = "dateOfConfirmation")
    @Mapping(source = "employee.employeeCategory", target = "employeeCategory")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    FinalSettlementDTO toDto(FinalSettlement finalSettlement);

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    FinalSettlement toEntity(FinalSettlementDTO finalSettlementDTO);

    default FinalSettlement fromId(Long id) {
        if (id == null) {
            return null;
        }
        FinalSettlement finalSettlement = new FinalSettlement();
        finalSettlement.setId(id);
        return finalSettlement;
    }
}
