package com.bits.hr.service.event;

import com.bits.hr.service.dto.MovementEntryDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MovementEntryApplicationEvent extends ApplicationEvent {

    MovementEntryDTO movementEntryDTO;
    boolean isLmApproved;
    EventType type;

    public MovementEntryApplicationEvent(Object source, MovementEntryDTO movementEntryDTO, boolean isLmApproved, EventType type) {
        super(source);
        this.movementEntryDTO = movementEntryDTO;
        this.isLmApproved = isLmApproved;
        this.type = type;
    }

    public MovementEntryApplicationEvent(Object source, MovementEntryDTO movementEntryDTO, EventType type) {
        super(source);
        this.movementEntryDTO = movementEntryDTO;
        this.type = type;
    }
}
