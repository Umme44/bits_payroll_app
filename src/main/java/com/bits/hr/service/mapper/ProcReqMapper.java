package com.bits.hr.service.mapper;

import com.bits.hr.domain.ProcReq;
import com.bits.hr.service.dto.ProcReqDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link ProcReq} and its DTO {@link ProcReqDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { ItemInformationMapper.class, ProcReqMasterMapper.class }
)
public interface ProcReqMapper extends EntityMapper<ProcReqDTO, ProcReq> {
    @Mapping(source = "itemInformation.id", target = "itemInformationId")
    @Mapping(source = "itemInformation.name", target = "itemInformationName")
    @Mapping(source = "itemInformation.code", target = "itemInformationCode")
    @Mapping(source = "itemInformation.specification", target = "itemInformationSpecification")
    @Mapping(source = "itemInformation.unitOfMeasurement.name", target = "unitOfMeasurementName")
    @Mapping(source = "procReqMaster.id", target = "procReqMasterId")
    @Mapping(source = "procReqMaster.requisitionNo", target = "procReqMasterRequisitionNo")
    ProcReqDTO toDto(ProcReq procReq);

    @Mapping(source = "itemInformationId", target = "itemInformation")
    @Mapping(source = "procReqMasterId", target = "procReqMaster")
    ProcReq toEntity(ProcReqDTO procReqDTO);

    default ProcReq fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProcReq procReq = new ProcReq();
        procReq.setId(id);
        return procReq;
    }
}
