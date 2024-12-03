package com.bits.hr.web.rest.commonUser;

import com.bits.hr.service.HolidaysService;
import com.bits.hr.service.dto.HolidaysDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.HolidayEvent;
import com.bits.hr.service.event.LeaveApplicationEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/common/holidays")
public class HolidaysCommonResource {

    private static final String ENTITY_NAME = "holidaysCommon";
    private final Logger log = LoggerFactory.getLogger(HolidaysCommonResource.class);
    private final HolidaysService holidaysService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public HolidaysCommonResource(HolidaysService holidaysService) {
        this.holidaysService = holidaysService;
    }

    /**
     * List of Holiday of Current Year
     *
     * @return
     */
    @GetMapping("")
    public ResponseEntity<List<HolidaysDTO>> getAllHolidaysOfCurrentYear() {
        log.debug("REST request to get all Holidays");
        int currentYear = LocalDate.now().getYear();
        return ResponseEntity.ok().body(holidaysService.findAllOfAYear(currentYear));
    }

    /**
     * {@code GET  /holidays/:id} : get the "id" holidays.
     *
     * @param id the id of the holidaysDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the holidaysDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HolidaysDTO> getHolidays(@PathVariable Long id) {
        log.debug("REST request to get Holidays : {}", id);
        Optional<HolidaysDTO> holidaysDTO = holidaysService.findOne(id);
        publishEvent(holidaysDTO.get(), EventType.CREATED);
        return ResponseUtil.wrapOrNotFound(holidaysDTO);
    }

    private void publishEvent(HolidaysDTO holidaysDTO, EventType event) {
        log.debug("publishing holiday event with event: " + event);
        HolidayEvent holidayEvent = new HolidayEvent(this, holidaysDTO, event);
        applicationEventPublisher.publishEvent(holidayEvent);
    }
}
