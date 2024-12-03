package com.bits.hr.service.mapper;

import com.bits.hr.domain.TrainingHistory;
import com.bits.hr.service.dto.TrainingHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link TrainingHistory} and its DTO {@link TrainingHistoryDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface TrainingHistoryMapper extends EntityMapper<TrainingHistoryDTO, TrainingHistory> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "employeeName")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    TrainingHistoryDTO toDto(TrainingHistory trainingHistory);

    @Mapping(source = "employeeId", target = "employee")
    TrainingHistory toEntity(TrainingHistoryDTO trainingHistoryDTO);

    default TrainingHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        TrainingHistory trainingHistory = new TrainingHistory();
        trainingHistory.setId(id);
        return trainingHistory;
    }
}
