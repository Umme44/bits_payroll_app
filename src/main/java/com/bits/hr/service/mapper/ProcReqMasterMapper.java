package com.bits.hr.service.mapper;

import com.bits.hr.domain.ProcReqMaster;
import com.bits.hr.service.dto.ProcReqMasterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link ProcReqMaster} and its DTO {@link ProcReqMasterDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { DepartmentMapper.class, EmployeeMapper.class, UserMapper.class }
)
public interface ProcReqMasterMapper extends EntityMapper<ProcReqMasterDTO, ProcReqMaster> {
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.departmentName", target = "departmentName")
    @Mapping(source = "requestedBy.id", target = "requestedById")
    @Mapping(source = "requestedBy.fullName", target = "requestedByFullName")
    @Mapping(source = "requestedBy.pin", target = "requestedByPIN")
    @Mapping(source = "requestedBy.officialContactNo", target = "requestedByOfficialContactNo")
    @Mapping(source = "requestedBy.designation.designationName", target = "requestedByDesignationName")
    @Mapping(source = "requestedBy.department.departmentName", target = "requestedByDepartmentName")
    @Mapping(source = "recommendedBy01.id", target = "recommendedBy01Id")
    @Mapping(source = "recommendedBy01.fullName", target = "recommendedBy01FullName")
    @Mapping(source = "recommendedBy01.department.departmentName", target = "recommendedBy01Designation")
    @Mapping(source = "recommendedBy01.designation.designationName", target = "recommendedBy01Department")
    @Mapping(source = "recommendedBy02.id", target = "recommendedBy02Id")
    @Mapping(source = "recommendedBy02.fullName", target = "recommendedBy02FullName")
    @Mapping(source = "recommendedBy02.department.departmentName", target = "recommendedBy02Designation")
    @Mapping(source = "recommendedBy02.designation.designationName", target = "recommendedBy02Department")
    @Mapping(source = "recommendedBy03.id", target = "recommendedBy03Id")
    @Mapping(source = "recommendedBy03.fullName", target = "recommendedBy03FullName")
    @Mapping(source = "recommendedBy03.department.departmentName", target = "recommendedBy03Designation")
    @Mapping(source = "recommendedBy03.designation.designationName", target = "recommendedBy03Department")
    @Mapping(source = "recommendedBy04.id", target = "recommendedBy04Id")
    @Mapping(source = "recommendedBy04.fullName", target = "recommendedBy04FullName")
    @Mapping(source = "recommendedBy04.department.departmentName", target = "recommendedBy04Designation")
    @Mapping(source = "recommendedBy04.designation.designationName", target = "recommendedBy04Department")
    @Mapping(source = "recommendedBy05.id", target = "recommendedBy05Id")
    @Mapping(source = "recommendedBy05.fullName", target = "recommendedBy05FullName")
    @Mapping(source = "recommendedBy05.department.departmentName", target = "recommendedBy05Designation")
    @Mapping(source = "recommendedBy05.designation.designationName", target = "recommendedBy05Department")
    @Mapping(source = "nextApprovalFrom.id", target = "nextApprovalFromId")
    @Mapping(source = "nextApprovalFrom.fullName", target = "nextApprovalFromFullName")
    @Mapping(source = "nextApprovalFrom.officialEmail", target = "nextApprovalFromOfficialEmail")
    @Mapping(source = "rejectedBy.id", target = "rejectedById")
    @Mapping(source = "rejectedBy.fullName", target = "rejectedByFullName")
    @Mapping(source = "closedBy.id", target = "closedById")
    @Mapping(source = "closedBy.fullName", target = "closedByFullName")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    ProcReqMasterDTO toDto(ProcReqMaster procReqMaster);

    @Mapping(source = "departmentId", target = "department")
    @Mapping(source = "requestedById", target = "requestedBy")
    @Mapping(source = "recommendedBy01Id", target = "recommendedBy01")
    @Mapping(source = "recommendedBy02Id", target = "recommendedBy02")
    @Mapping(source = "recommendedBy03Id", target = "recommendedBy03")
    @Mapping(source = "recommendedBy04Id", target = "recommendedBy04")
    @Mapping(source = "recommendedBy05Id", target = "recommendedBy05")
    @Mapping(source = "nextApprovalFromId", target = "nextApprovalFrom")
    @Mapping(source = "rejectedById", target = "rejectedBy")
    @Mapping(source = "closedById", target = "closedBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(target = "procReqs", ignore = true)
    @Mapping(target = "removeProcReq", ignore = true)
    ProcReqMaster toEntity(ProcReqMasterDTO procReqMasterDTO);

    default ProcReqMaster fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProcReqMaster procReqMaster = new ProcReqMaster();
        procReqMaster.setId(id);
        return procReqMaster;
    }
}
