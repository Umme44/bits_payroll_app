package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.HoldSalaryDisbursementDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link HoldSalaryDisbursement} and its DTO {@link HoldSalaryDisbursementDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { UserMapper.class, EmployeeSalaryMapper.class })
public interface HoldSalaryDisbursementMapper extends EntityMapper<HoldSalaryDisbursementDTO, HoldSalaryDisbursement> {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "employeeSalary.id", target = "employeeSalaryId")
    @Mapping(source = "employeeSalary.month", target = "salaryMonth")
    @Mapping(source = "employeeSalary.year", target = "salaryYear")
    @Mapping(source = "employeeSalary.netPay", target = "netPay")
    @Mapping(source = "employeeSalary.totalDeduction", target = "totalDeduction")
    @Mapping(source = "employeeSalary.otherDeduction", target = "otherDeduction")
    @Mapping(source = "employeeSalary.pin", target = "pin")
    @Mapping(source = "employeeSalary.employee.fullName", target = "employeeName")
    HoldSalaryDisbursementDTO toDto(HoldSalaryDisbursement holdSalaryDisbursement);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "employeeSalaryId", target = "employeeSalary")
    HoldSalaryDisbursement toEntity(HoldSalaryDisbursementDTO holdSalaryDisbursementDTO);

    default HoldSalaryDisbursement fromId(Long id) {
        if (id == null) {
            return null;
        }
        HoldSalaryDisbursement holdSalaryDisbursement = new HoldSalaryDisbursement();
        holdSalaryDisbursement.setId(id);
        return holdSalaryDisbursement;
    }
}
