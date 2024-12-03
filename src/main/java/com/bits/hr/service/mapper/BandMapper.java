package com.bits.hr.service.mapper;

import com.bits.hr.domain.Band;
import com.bits.hr.service.dto.BandDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Band} and its DTO {@link BandDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { UserMapper.class })
public interface BandMapper extends EntityMapper<BandDTO, Band> {
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    BandDTO toDto(Band band);

    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    Band toEntity(BandDTO bandDTO);

    default Band fromId(Long id) {
        if (id == null) {
            return null;
        }
        Band band = new Band();
        band.setId(id);
        return band;
    }
}
