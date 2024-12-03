package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.FestivalBonusDetailsDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link FestivalBonusDetails} and its DTO {@link FestivalBonusDetailsDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class, FestivalMapper.class })
public interface FestivalBonusDetailsMapper extends EntityMapper<FestivalBonusDetailsDTO, FestivalBonusDetails> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    @Mapping(source = "employee.band.bandName", target = "bandName")
    @Mapping(source = "employee.dateOfJoining", target = "doj")
    @Mapping(source = "employee.dateOfConfirmation", target = "doc")
    @Mapping(source = "festival.id", target = "festivalId")
    @Mapping(source = "festival.title", target = "title")
    @Mapping(source = "festival.festivalName", target = "festivalName")
    @Mapping(source = "festival.festivalDate", target = "festivalDate")
    @Mapping(source = "festival.bonusDisbursementDate", target = "bonusDisbursementDate")
    @Mapping(source = "employee.contractPeriodEndDate", target = "contractPeriodEndDate")
    @Mapping(source = "employee.contractPeriodExtendedTo", target = "contractPeriodExtendedTo")
    @Mapping(source = "employee.employeeCategory", target = "employeeCategory")
    FestivalBonusDetailsDTO toDto(FestivalBonusDetails festivalBonusDetails);

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "festivalId", target = "festival")
    FestivalBonusDetails toEntity(FestivalBonusDetailsDTO festivalBonusDetailsDTO);

    default FestivalBonusDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        FestivalBonusDetails festivalBonusDetails = new FestivalBonusDetails();
        festivalBonusDetails.setId(id);
        return festivalBonusDetails;
    }
}
