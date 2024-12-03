package com.bits.hr.service.mapper;

import com.bits.hr.domain.HoldFbDisbursement;
import com.bits.hr.service.dto.HoldFbDisbursementDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link HoldFbDisbursement} and its DTO {@link HoldFbDisbursementDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { UserMapper.class, FestivalBonusDetailsMapper.class }
)
public interface HoldFbDisbursementMapper extends EntityMapper<HoldFbDisbursementDTO, HoldFbDisbursement> {
    @Mapping(source = "disbursedById", target = "disbursedBy")
    @Mapping(source = "festivalBonusDetailId", target = "festivalBonusDetail")
    HoldFbDisbursement toEntity(HoldFbDisbursementDTO holdFbDisbursementDTO);

    @Mapping(source = "disbursedBy.id", target = "disbursedById")
    @Mapping(source = "disbursedBy.login", target = "disbursedByLogin")
    @Mapping(source = "festivalBonusDetail.id", target = "festivalBonusDetailId")
    @Mapping(source = "festivalBonusDetail.bonusAmount", target = "bonusAmount")
    @Mapping(source = "festivalBonusDetail.employee.pin", target = "pin")
    @Mapping(source = "festivalBonusDetail.employee.fullName", target = "employeeName")
    @Mapping(source = "festivalBonusDetail.festival.title", target = "festivalTitle")
    @Mapping(source = "festivalBonusDetail.festival.festivalName", target = "festivalName")
    HoldFbDisbursementDTO toDto(HoldFbDisbursement holdFbDisbursement);

    default HoldFbDisbursement fromId(Long id) {
        if (id == null) {
            return null;
        }
        HoldFbDisbursement holdFbDisbursement = new HoldFbDisbursement();
        holdFbDisbursement.setId(id);
        return holdFbDisbursement;
    }
}
