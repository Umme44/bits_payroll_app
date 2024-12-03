package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.FileTemplatesDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link FileTemplates} and its DTO {@link FileTemplatesDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface FileTemplatesMapper extends EntityMapper<FileTemplatesDTO, FileTemplates> {
    default FileTemplates fromId(Long id) {
        if (id == null) {
            return null;
        }
        FileTemplates fileTemplates = new FileTemplates();
        fileTemplates.setId(id);
        return fileTemplates;
    }
}
