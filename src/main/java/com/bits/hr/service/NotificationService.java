package com.bits.hr.service;

import com.bits.hr.service.dto.NotificationDTO;

/**
 * A Service Interface for manageing {@link NotificationDTO}
 */
public interface NotificationService {
    NotificationDTO getApprovalNotificationsCommon(long employeeId);

    NotificationDTO getApprovalNotificationsHR();
}
