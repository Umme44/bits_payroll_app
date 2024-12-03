package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.OfferDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Offer} and its DTO {@link OfferDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface OfferMapper extends EntityMapper<OfferDTO, Offer> {
    default Offer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Offer offer = new Offer();
        offer.setId(id);
        return offer;
    }
}
