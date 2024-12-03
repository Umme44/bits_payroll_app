package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.ArrearPaymentDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link ArrearPayment} and its DTO {@link ArrearPaymentDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { ArrearSalaryItemMapper.class })
public interface ArrearPaymentMapper extends EntityMapper<ArrearPaymentDTO, ArrearPayment> {
    @Mapping(source = "arrearSalaryItem.id", target = "arrearSalaryItemId")
    @Mapping(source = "arrearSalaryItem.title", target = "arrearSalaryItemTitle")
    ArrearPaymentDTO toDto(ArrearPayment arrearPayment);

    @Mapping(source = "arrearSalaryItemId", target = "arrearSalaryItem")
    ArrearPayment toEntity(ArrearPaymentDTO arrearPaymentDTO);

    default ArrearPayment fromId(Long id) {
        if (id == null) {
            return null;
        }
        ArrearPayment arrearPayment = new ArrearPayment();
        arrearPayment.setId(id);
        return arrearPayment;
    }
}
