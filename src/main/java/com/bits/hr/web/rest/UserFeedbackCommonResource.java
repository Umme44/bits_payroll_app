package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.UserFeedback;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.UserFeedbackRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.UserFeedbackService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.UserFeedbackDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/common/user-feedbacks")
public class UserFeedbackCommonResource {

    private final Logger log = LoggerFactory.getLogger(UserFeedbackCommonResource.class);

    private static final String ENTITY_NAME = "userFeedback";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private UserFeedbackService userFeedbackService;

    @Autowired
    CurrentEmployeeService employeeService;

    @Autowired
    UserFeedbackRepository userFeedbackRepository;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @GetMapping("/is-allowed")
    public boolean checkUserIsAllowToShareFeedback() {
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "UserFeedback");
        try {
            return false;
            /*User user = employeeService.getCurrentUser().get();
            Optional<UserFeedback> userFeedback = userFeedbackRepository.findFeedbackByUserId(user.getId());
            if (!userFeedback.isPresent()){
                return true;
            }else return false;*/
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @PostMapping("")
    public ResponseEntity<UserFeedbackDTO> createUserFeedback(@RequestBody UserFeedbackDTO userFeedbackDTO) throws URISyntaxException {
        log.debug("REST request to save UserFeedback : {}", userFeedbackDTO);

        if (userFeedbackDTO.getId() != null) {
            throw new BadRequestAlertException("A new userFeedback cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (userFeedbackDTO.getId() == null) {
            userFeedbackDTO.setUserId(employeeService.getCurrentUserId().get());
        }
        UserFeedbackDTO result = userFeedbackService.save(userFeedbackDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "UserFeedback");
        return ResponseEntity
            .created(new URI("/api/common/user-feedbacks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
