package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.TimeSlot;
import com.bits.hr.repository.TimeSlotRepository;
import com.bits.hr.service.dto.TimeSlotDTO;
import com.bits.hr.service.mapper.TimeSlotMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TimeSlotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TimeSlotResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_IN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_IN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_OUT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OUT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_APPLICABLE_BY_EMPLOYEE = false;
    private static final Boolean UPDATED_IS_APPLICABLE_BY_EMPLOYEE = true;

    private static final Boolean DEFAULT_IS_DEFAULT_SHIFT = false;
    private static final Boolean UPDATED_IS_DEFAULT_SHIFT = true;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_WEEK_ENDS = "AAAAAAAAAA";
    private static final String UPDATED_WEEK_ENDS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/time-slots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private TimeSlotMapper timeSlotMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimeSlotMockMvc;

    private TimeSlot timeSlot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeSlot createEntity(EntityManager em) {
        TimeSlot timeSlot = new TimeSlot()
            .title(DEFAULT_TITLE)
            .inTime(DEFAULT_IN_TIME)
            .outTime(DEFAULT_OUT_TIME)
            .isApplicableByEmployee(DEFAULT_IS_APPLICABLE_BY_EMPLOYEE)
            .isDefaultShift(DEFAULT_IS_DEFAULT_SHIFT)
            .code(DEFAULT_CODE)
            .weekEnds(DEFAULT_WEEK_ENDS);
        return timeSlot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeSlot createUpdatedEntity(EntityManager em) {
        TimeSlot timeSlot = new TimeSlot()
            .title(UPDATED_TITLE)
            .inTime(UPDATED_IN_TIME)
            .outTime(UPDATED_OUT_TIME)
            .isApplicableByEmployee(UPDATED_IS_APPLICABLE_BY_EMPLOYEE)
            .isDefaultShift(UPDATED_IS_DEFAULT_SHIFT)
            .code(UPDATED_CODE)
            .weekEnds(UPDATED_WEEK_ENDS);
        return timeSlot;
    }

    @BeforeEach
    public void initTest() {
        timeSlot = createEntity(em);
    }

    @Test
    @Transactional
    void createTimeSlot() throws Exception {
        int databaseSizeBeforeCreate = timeSlotRepository.findAll().size();
        // Create the TimeSlot
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);
        restTimeSlotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeSlotDTO)))
            .andExpect(status().isCreated());

        // Validate the TimeSlot in the database
        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        assertThat(timeSlotList).hasSize(databaseSizeBeforeCreate + 1);
        TimeSlot testTimeSlot = timeSlotList.get(timeSlotList.size() - 1);
        assertThat(testTimeSlot.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTimeSlot.getInTime()).isEqualTo(DEFAULT_IN_TIME);
        assertThat(testTimeSlot.getOutTime()).isEqualTo(DEFAULT_OUT_TIME);
        assertThat(testTimeSlot.getIsApplicableByEmployee()).isEqualTo(DEFAULT_IS_APPLICABLE_BY_EMPLOYEE);
        assertThat(testTimeSlot.getIsDefaultShift()).isEqualTo(DEFAULT_IS_DEFAULT_SHIFT);
        assertThat(testTimeSlot.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTimeSlot.getWeekEnds()).isEqualTo(DEFAULT_WEEK_ENDS);
    }

    @Test
    @Transactional
    void createTimeSlotWithExistingId() throws Exception {
        // Create the TimeSlot with an existing ID
        timeSlot.setId(1L);
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        int databaseSizeBeforeCreate = timeSlotRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeSlotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeSlotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TimeSlot in the database
        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        assertThat(timeSlotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeSlotRepository.findAll().size();
        // set the field null
        timeSlot.setTitle(null);

        // Create the TimeSlot, which fails.
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        restTimeSlotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeSlotDTO)))
            .andExpect(status().isBadRequest());

        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        assertThat(timeSlotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeSlotRepository.findAll().size();
        // set the field null
        timeSlot.setInTime(null);

        // Create the TimeSlot, which fails.
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        restTimeSlotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeSlotDTO)))
            .andExpect(status().isBadRequest());

        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        assertThat(timeSlotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOutTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeSlotRepository.findAll().size();
        // set the field null
        timeSlot.setOutTime(null);

        // Create the TimeSlot, which fails.
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        restTimeSlotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeSlotDTO)))
            .andExpect(status().isBadRequest());

        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        assertThat(timeSlotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTimeSlots() throws Exception {
        // Initialize the database
        timeSlotRepository.saveAndFlush(timeSlot);

        // Get all the timeSlotList
        restTimeSlotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeSlot.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].inTime").value(hasItem(DEFAULT_IN_TIME.toString())))
            .andExpect(jsonPath("$.[*].outTime").value(hasItem(DEFAULT_OUT_TIME.toString())))
            .andExpect(jsonPath("$.[*].isApplicableByEmployee").value(hasItem(DEFAULT_IS_APPLICABLE_BY_EMPLOYEE.booleanValue())))
            .andExpect(jsonPath("$.[*].isDefaultShift").value(hasItem(DEFAULT_IS_DEFAULT_SHIFT.booleanValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].weekEnds").value(hasItem(DEFAULT_WEEK_ENDS.toString())));
    }

    @Test
    @Transactional
    void getTimeSlot() throws Exception {
        // Initialize the database
        timeSlotRepository.saveAndFlush(timeSlot);

        // Get the timeSlot
        restTimeSlotMockMvc
            .perform(get(ENTITY_API_URL_ID, timeSlot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timeSlot.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.inTime").value(DEFAULT_IN_TIME.toString()))
            .andExpect(jsonPath("$.outTime").value(DEFAULT_OUT_TIME.toString()))
            .andExpect(jsonPath("$.isApplicableByEmployee").value(DEFAULT_IS_APPLICABLE_BY_EMPLOYEE.booleanValue()))
            .andExpect(jsonPath("$.isDefaultShift").value(DEFAULT_IS_DEFAULT_SHIFT.booleanValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.weekEnds").value(DEFAULT_WEEK_ENDS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTimeSlot() throws Exception {
        // Get the timeSlot
        restTimeSlotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTimeSlot() throws Exception {
        // Initialize the database
        timeSlotRepository.saveAndFlush(timeSlot);

        int databaseSizeBeforeUpdate = timeSlotRepository.findAll().size();

        // Update the timeSlot
        TimeSlot updatedTimeSlot = timeSlotRepository.findById(timeSlot.getId()).get();
        // Disconnect from session so that the updates on updatedTimeSlot are not directly saved in db
        em.detach(updatedTimeSlot);
        updatedTimeSlot
            .title(UPDATED_TITLE)
            .inTime(UPDATED_IN_TIME)
            .outTime(UPDATED_OUT_TIME)
            .isApplicableByEmployee(UPDATED_IS_APPLICABLE_BY_EMPLOYEE)
            .isDefaultShift(UPDATED_IS_DEFAULT_SHIFT)
            .code(UPDATED_CODE)
            .weekEnds(UPDATED_WEEK_ENDS);
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(updatedTimeSlot);

        restTimeSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timeSlotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeSlotDTO))
            )
            .andExpect(status().isOk());

        // Validate the TimeSlot in the database
        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        assertThat(timeSlotList).hasSize(databaseSizeBeforeUpdate);
        TimeSlot testTimeSlot = timeSlotList.get(timeSlotList.size() - 1);
        assertThat(testTimeSlot.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTimeSlot.getInTime()).isEqualTo(UPDATED_IN_TIME);
        assertThat(testTimeSlot.getOutTime()).isEqualTo(UPDATED_OUT_TIME);
        assertThat(testTimeSlot.getIsApplicableByEmployee()).isEqualTo(UPDATED_IS_APPLICABLE_BY_EMPLOYEE);
        assertThat(testTimeSlot.getIsDefaultShift()).isEqualTo(UPDATED_IS_DEFAULT_SHIFT);
        assertThat(testTimeSlot.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTimeSlot.getWeekEnds()).isEqualTo(UPDATED_WEEK_ENDS);
    }

    @Test
    @Transactional
    void putNonExistingTimeSlot() throws Exception {
        int databaseSizeBeforeUpdate = timeSlotRepository.findAll().size();
        timeSlot.setId(count.incrementAndGet());

        // Create the TimeSlot
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timeSlotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeSlotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSlot in the database
        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        assertThat(timeSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimeSlot() throws Exception {
        int databaseSizeBeforeUpdate = timeSlotRepository.findAll().size();
        timeSlot.setId(count.incrementAndGet());

        // Create the TimeSlot
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeSlotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSlot in the database
        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        assertThat(timeSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimeSlot() throws Exception {
        int databaseSizeBeforeUpdate = timeSlotRepository.findAll().size();
        timeSlot.setId(count.incrementAndGet());

        // Create the TimeSlot
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSlotMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeSlotDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeSlot in the database
        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        assertThat(timeSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimeSlotWithPatch() throws Exception {
        // Initialize the database
        timeSlotRepository.saveAndFlush(timeSlot);

        int databaseSizeBeforeUpdate = timeSlotRepository.findAll().size();

        // Update the timeSlot using partial update
        TimeSlot partialUpdatedTimeSlot = new TimeSlot();
        partialUpdatedTimeSlot.setId(timeSlot.getId());

        partialUpdatedTimeSlot.title(UPDATED_TITLE).outTime(UPDATED_OUT_TIME).isApplicableByEmployee(UPDATED_IS_APPLICABLE_BY_EMPLOYEE);

        restTimeSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeSlot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTimeSlot))
            )
            .andExpect(status().isOk());

        // Validate the TimeSlot in the database
        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        assertThat(timeSlotList).hasSize(databaseSizeBeforeUpdate);
        TimeSlot testTimeSlot = timeSlotList.get(timeSlotList.size() - 1);
        assertThat(testTimeSlot.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTimeSlot.getInTime()).isEqualTo(DEFAULT_IN_TIME);
        assertThat(testTimeSlot.getOutTime()).isEqualTo(UPDATED_OUT_TIME);
        assertThat(testTimeSlot.getIsApplicableByEmployee()).isEqualTo(UPDATED_IS_APPLICABLE_BY_EMPLOYEE);
        assertThat(testTimeSlot.getIsDefaultShift()).isEqualTo(DEFAULT_IS_DEFAULT_SHIFT);
        assertThat(testTimeSlot.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTimeSlot.getWeekEnds()).isEqualTo(DEFAULT_WEEK_ENDS);
    }

    @Test
    @Transactional
    void fullUpdateTimeSlotWithPatch() throws Exception {
        // Initialize the database
        timeSlotRepository.saveAndFlush(timeSlot);

        int databaseSizeBeforeUpdate = timeSlotRepository.findAll().size();

        // Update the timeSlot using partial update
        TimeSlot partialUpdatedTimeSlot = new TimeSlot();
        partialUpdatedTimeSlot.setId(timeSlot.getId());

        partialUpdatedTimeSlot
            .title(UPDATED_TITLE)
            .inTime(UPDATED_IN_TIME)
            .outTime(UPDATED_OUT_TIME)
            .isApplicableByEmployee(UPDATED_IS_APPLICABLE_BY_EMPLOYEE)
            .isDefaultShift(UPDATED_IS_DEFAULT_SHIFT)
            .code(UPDATED_CODE)
            .weekEnds(UPDATED_WEEK_ENDS);

        restTimeSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeSlot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTimeSlot))
            )
            .andExpect(status().isOk());

        // Validate the TimeSlot in the database
        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        assertThat(timeSlotList).hasSize(databaseSizeBeforeUpdate);
        TimeSlot testTimeSlot = timeSlotList.get(timeSlotList.size() - 1);
        assertThat(testTimeSlot.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTimeSlot.getInTime()).isEqualTo(UPDATED_IN_TIME);
        assertThat(testTimeSlot.getOutTime()).isEqualTo(UPDATED_OUT_TIME);
        assertThat(testTimeSlot.getIsApplicableByEmployee()).isEqualTo(UPDATED_IS_APPLICABLE_BY_EMPLOYEE);
        assertThat(testTimeSlot.getIsDefaultShift()).isEqualTo(UPDATED_IS_DEFAULT_SHIFT);
        assertThat(testTimeSlot.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTimeSlot.getWeekEnds()).isEqualTo(UPDATED_WEEK_ENDS);
    }

    @Test
    @Transactional
    void patchNonExistingTimeSlot() throws Exception {
        int databaseSizeBeforeUpdate = timeSlotRepository.findAll().size();
        timeSlot.setId(count.incrementAndGet());

        // Create the TimeSlot
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timeSlotDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(timeSlotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSlot in the database
        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        assertThat(timeSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimeSlot() throws Exception {
        int databaseSizeBeforeUpdate = timeSlotRepository.findAll().size();
        timeSlot.setId(count.incrementAndGet());

        // Create the TimeSlot
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(timeSlotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSlot in the database
        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        assertThat(timeSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimeSlot() throws Exception {
        int databaseSizeBeforeUpdate = timeSlotRepository.findAll().size();
        timeSlot.setId(count.incrementAndGet());

        // Create the TimeSlot
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSlotMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(timeSlotDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeSlot in the database
        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        assertThat(timeSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimeSlot() throws Exception {
        // Initialize the database
        timeSlotRepository.saveAndFlush(timeSlot);

        int databaseSizeBeforeDelete = timeSlotRepository.findAll().size();

        // Delete the timeSlot
        restTimeSlotMockMvc
            .perform(delete(ENTITY_API_URL_ID, timeSlot.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        assertThat(timeSlotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
