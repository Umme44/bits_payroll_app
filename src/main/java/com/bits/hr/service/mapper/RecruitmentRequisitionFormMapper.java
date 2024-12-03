package com.bits.hr.service.mapper;

import com.bits.hr.domain.RecruitmentRequisitionForm;
import com.bits.hr.service.dto.RecruitmentRequisitionFormDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link RecruitmentRequisitionForm} and its DTO {@link RecruitmentRequisitionFormDTO}.
 */
@Mapper(componentModel = "spring", uses = {DesignationMapper.class, BandMapper.class, DepartmentMapper.class, UnitMapper.class, EmployeeMapper.class, UserMapper.class}, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RecruitmentRequisitionFormMapper extends EntityMapper<RecruitmentRequisitionFormDTO, RecruitmentRequisitionForm> {

    @Mapping(target = "rejectedAt", ignore = true)

    @Mapping(source = "functionalDesignation.id", target = "functionalDesignationId")
    @Mapping(source = "functionalDesignation.designationName", target = "functionalDesignationName")

    @Mapping(source = "band.id", target = "bandId")
    @Mapping(source = "band.bandName", target = "bandName")
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.departmentName", target = "departmentName")
    @Mapping(source = "unit.id", target = "unitId")
    @Mapping(source = "unit.unitName", target = "unitName")

    @Mapping(source = "recommendedBy01.id", target = "recommendedBy01Id")
    @Mapping(source = "recommendedBy02.id", target = "recommendedBy02Id")
    @Mapping(source = "recommendedBy03.id", target = "recommendedBy03Id")
    @Mapping(source = "recommendedBy04.id", target = "recommendedBy04Id")
    @Mapping(source = "recommendedBy05.id", target = "recommendedBy05Id")

    @Mapping(source = "recommendedBy01.fullName", target = "recommendedBy01FullName")
    @Mapping(source = "recommendedBy02.fullName", target = "recommendedBy02FullName")
    @Mapping(source = "recommendedBy03.fullName", target = "recommendedBy03FullName")
    @Mapping(source = "recommendedBy04.fullName", target = "recommendedBy04FullName")
    @Mapping(source = "recommendedBy05.fullName", target = "recommendedBy05FullName")

    @Mapping(source = "requester.id", target = "requesterId")
    @Mapping(source = "requester.fullName", target = "requesterFullName")
    @Mapping(source = "requester.pin", target = "requesterPin")
    @Mapping(source = "requester.designation.designationName", target = "requesterDesignationName")

    @Mapping(source = "rejectedBy.id", target = "rejectedById")
    @Mapping(source = "rejectedBy.fullName", target = "rejectedByFullName")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")

    @Mapping(source = "deletedBy.id", target = "deletedById")
    @Mapping(source = "employeeToBeReplaced.id", target = "employeeToBeReplacedId")
    @Mapping(source = "employeeToBeReplaced.fullName", target = "employeeToBeReplacedFullName")
    @Mapping(source = "employeeToBeReplaced.pin", target = "employeeToBeReplacedPin")
    RecruitmentRequisitionFormDTO toDto(RecruitmentRequisitionForm recruitmentRequisitionForm);

    @Mapping(source = "functionalDesignationId", target = "functionalDesignation")
    @Mapping(source = "bandId", target = "band")
    @Mapping(source = "departmentId", target = "department")
    @Mapping(source = "unitId", target = "unit")
    @Mapping(source = "recommendedBy01Id", target = "recommendedBy01")
    @Mapping(source = "recommendedBy02Id", target = "recommendedBy02")
    @Mapping(source = "recommendedBy03Id", target = "recommendedBy03")
    @Mapping(source = "recommendedBy04Id", target = "recommendedBy04")
    @Mapping(source = "recommendedBy05Id", target = "recommendedBy05")
    @Mapping(source = "requesterId", target = "requester")
    @Mapping(source = "employeeToBeReplacedId", target = "employeeToBeReplaced")
    @Mapping(source = "rejectedById", target = "rejectedBy")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    @Mapping(source = "deletedById", target = "deletedBy")
    RecruitmentRequisitionForm toEntity(RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO);

    default RecruitmentRequisitionForm fromId(Long id) {
        if (id == null) {
            return null;
        }
        RecruitmentRequisitionForm recruitmentRequisitionForm = new RecruitmentRequisitionForm();
        recruitmentRequisitionForm.setId(id);
        return recruitmentRequisitionForm;
    }
}
