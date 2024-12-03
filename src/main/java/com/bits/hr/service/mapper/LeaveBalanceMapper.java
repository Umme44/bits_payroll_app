package com.bits.hr.service.mapper;

import com.bits.hr.domain.LeaveBalance;
import com.bits.hr.service.dto.LeaveBalanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link LeaveBalance} and its DTO {@link LeaveBalanceDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface LeaveBalanceMapper extends EntityMapper<LeaveBalanceDTO, LeaveBalance> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    LeaveBalanceDTO toDto(LeaveBalance leaveBalance);

    @Mapping(source = "employeeId", target = "employee")
    LeaveBalance toEntity(LeaveBalanceDTO leaveBalanceDTO);

    default LeaveBalance fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeaveBalance leaveBalance = new LeaveBalance();
        leaveBalance.setId(id);
        return leaveBalance;
    }
}
