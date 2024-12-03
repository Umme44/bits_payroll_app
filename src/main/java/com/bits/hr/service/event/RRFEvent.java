package com.bits.hr.service.event;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.MovementEntryDTO;
import com.bits.hr.service.dto.RecruitmentRequisitionFormDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RRFEvent extends ApplicationEvent {

    RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO;
    EventType type;
    boolean hasRaisedOnBehalf;
    Employee createdBy;
    long nextRecommendedById;

    public RRFEvent(Object source, RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO, EventType type) {
        super(source);
        this.recruitmentRequisitionFormDTO = recruitmentRequisitionFormDTO;
        this.type = type;
    }

    public RRFEvent(
        Object source,
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO,
        EventType type,
        boolean hasRaisedOnBehalf,
        Employee createdBy
    ) {
        super(source);
        this.recruitmentRequisitionFormDTO = recruitmentRequisitionFormDTO;
        this.hasRaisedOnBehalf = hasRaisedOnBehalf;
        this.createdBy = createdBy;
        this.type = type;
    }

    public RRFEvent(Object source, RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO, long nextRecommendedById, EventType type) {
        super(source);
        this.recruitmentRequisitionFormDTO = recruitmentRequisitionFormDTO;
        this.nextRecommendedById = nextRecommendedById;
        this.type = type;
    }
}
