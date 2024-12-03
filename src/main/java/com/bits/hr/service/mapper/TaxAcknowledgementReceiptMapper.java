package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.TaxAcknowledgementReceiptDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link TaxAcknowledgementReceipt} and its DTO {@link TaxAcknowledgementReceiptDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { AitConfigMapper.class, EmployeeMapper.class, UserMapper.class }
)
public interface TaxAcknowledgementReceiptMapper extends EntityMapper<TaxAcknowledgementReceiptDTO, TaxAcknowledgementReceipt> {
    @Mapping(source = "fiscalYear.id", target = "fiscalYearId")
    @Mapping(source = "fiscalYear.startDate.year", target = "startYear")
    @Mapping(source = "fiscalYear.endDate.year", target = "endYear")
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "receivedBy.id", target = "receivedById")
    @Mapping(source = "receivedBy.login", target = "receivedByLogin")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "name")
    @Mapping(source = "employee.designation.designationName", target = "designation")
    TaxAcknowledgementReceiptDTO toDto(TaxAcknowledgementReceipt taxAcknowledgementReceipt);

    @Mapping(source = "fiscalYearId", target = "fiscalYear")
    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "receivedById", target = "receivedBy")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    TaxAcknowledgementReceipt toEntity(TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO);

    default TaxAcknowledgementReceipt fromId(Long id) {
        if (id == null) {
            return null;
        }
        TaxAcknowledgementReceipt taxAcknowledgementReceipt = new TaxAcknowledgementReceipt();
        taxAcknowledgementReceipt.setId(id);
        return taxAcknowledgementReceipt;
    }
}
