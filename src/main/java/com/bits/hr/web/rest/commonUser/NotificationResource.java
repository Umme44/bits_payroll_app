package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.NotificationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link com.bits.hr.service.dto.NotificationDTO}
 */
@RestController
@RequestMapping("/api")
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);
    private final NotificationService notificationService;
    private final CurrentEmployeeService currentEmployeeService;

    private static final String RESOURCE_NAME = "NotificationResource";
    private final EventLoggingPublisher eventLoggingPublisher;

    public NotificationResource(
        NotificationService notificationService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.notificationService = notificationService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code GET  /notification} : get the notification.
     */
    @GetMapping("/common/approval-notification")
    public ResponseEntity<NotificationDTO> getApprovalNotificationsCommon() {
        log.debug("REST request to get NotificationDTO");
        long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
        NotificationDTO result = notificationService.getApprovalNotificationsCommon(employeeId);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/employee-mgt/approval-notification")
    public ResponseEntity<NotificationDTO> getApprovalNotificationsHR() {
        log.debug("REST request to get NotificationDTO");
        NotificationDTO result = notificationService.getApprovalNotificationsHR();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().body(result);
    }
}
