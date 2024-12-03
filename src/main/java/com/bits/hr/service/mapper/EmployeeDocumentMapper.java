package com.bits.hr.service.mapper;

import com.bits.hr.domain.EmployeeDocument;
import com.bits.hr.service.dto.EmployeeDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmployeeDocument} and its DTO {@link EmployeeDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeDocumentMapper extends EntityMapper<EmployeeDocumentDTO, EmployeeDocument> {}
