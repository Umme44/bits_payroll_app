package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.AitPaymentDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link AitPayment} and its DTO {@link AitPaymentDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface AitPaymentMapper extends EntityMapper<AitPaymentDTO, AitPayment> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    AitPaymentDTO toDto(AitPayment aitPayment);

    @Mapping(source = "employeeId", target = "employee")
    AitPayment toEntity(AitPaymentDTO aitPaymentDTO);

    default AitPayment fromId(Long id) {
        if (id == null) {
            return null;
        }
        AitPayment aitPayment = new AitPayment();
        aitPayment.setId(id);
        return aitPayment;
    }
}
