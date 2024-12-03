package com.bits.hr.service.mapper;

import com.bits.hr.domain.LeaveAllocation;
import com.bits.hr.service.dto.LeaveAllocationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link LeaveAllocation} and its DTO {@link LeaveAllocationDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface LeaveAllocationMapper extends EntityMapper<LeaveAllocationDTO, LeaveAllocation> {
    default LeaveAllocation fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeaveAllocation leaveAllocation = new LeaveAllocation();
        leaveAllocation.setId(id);
        return leaveAllocation;
    }
}
