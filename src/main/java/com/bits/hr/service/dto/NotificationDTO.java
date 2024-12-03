package com.bits.hr.service.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * A DTO for total notification
 */

@Data
@ToString
@EqualsAndHashCode
public class NotificationDTO implements Serializable {

    private int pendingLeaveApprovals;
    private int pendingManualAttendanceApprovals;
    private int pendingMovementEntriesApprovals;
    private int pendingFlexScheduleApprovals;
    private int pendingWorkFromHomeApprovals;
    private int pendingRRFApprovals;
    private int pendingPRFApprovals;
}
