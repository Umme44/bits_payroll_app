package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.NomineeDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Nominee} and its DTO {@link NomineeDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface NomineeMapper extends EntityMapper<NomineeDTO, Nominee> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "approvedBy.id", target = "approvedById")
    @Mapping(source = "witness.id", target = "witnessId")
    @Mapping(source = "member.id", target = "memberId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    @Mapping(source = "employee.employeeCategory", target = "employeeCategory")
    @Mapping(source = "employee.dateOfJoining", target = "dateOfJoining")
    @Mapping(source = "employee.dateOfConfirmation", target = "dateOfConfirmation")
    @Mapping(source = "approvedBy.fullName", target = "approvedByFullName")
    NomineeDTO toDto(Nominee nominee);

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "approvedById", target = "approvedBy")
    @Mapping(source = "witnessId", target = "witness")
    @Mapping(source = "memberId", target = "member")
    Nominee toEntity(NomineeDTO nomineeDTO);

    default Nominee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Nominee nominee = new Nominee();
        nominee.setId(id);
        return nominee;
    }
}
