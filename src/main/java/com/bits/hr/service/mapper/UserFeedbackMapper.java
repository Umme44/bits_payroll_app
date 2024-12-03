package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.UserFeedbackDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link UserFeedback} and its DTO {@link UserFeedbackDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { UserMapper.class })
public interface UserFeedbackMapper extends EntityMapper<UserFeedbackDTO, UserFeedback> {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "user.lastName", target = "userLastName")
    UserFeedbackDTO toDto(UserFeedback userFeedback);

    @Mapping(source = "userId", target = "user")
    UserFeedback toEntity(UserFeedbackDTO userFeedbackDTO);

    default UserFeedback fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setId(id);
        return userFeedback;
    }
}
