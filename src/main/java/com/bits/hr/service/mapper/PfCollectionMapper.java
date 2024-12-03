package com.bits.hr.service.mapper;

import com.bits.hr.domain.PfCollection;
import com.bits.hr.service.dto.PfCollectionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link PfCollection} and its DTO {@link PfCollectionDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { PfAccountMapper.class })
public interface PfCollectionMapper extends EntityMapper<PfCollectionDTO, PfCollection> {
    @Mapping(source = "pfAccountId", target = "pfAccount")
    PfCollection toEntity(PfCollectionDTO pfCollectionDTO);

    @Mapping(source = "pfAccount.id", target = "pfAccountId")
    @Mapping(source = "pfAccount.pfCode", target = "pfCode")
    @Mapping(source = "pfAccount.status", target = "status")
    @Mapping(source = "pfAccount.designationName", target = "designationName")
    @Mapping(source = "pfAccount.departmentName", target = "departmentName")
    @Mapping(source = "pfAccount.unitName", target = "unitName")
    @Mapping(source = "pfAccount.accHolderName", target = "accHolderName")
    @Mapping(source = "pfAccount.pin", target = "pin")
    PfCollectionDTO toDto(PfCollection pfCollection);

    default PfCollection fromId(Long id) {
        if (id == null) {
            return null;
        }
        PfCollection pfCollection = new PfCollection();
        pfCollection.setId(id);
        return pfCollection;
    }
}
