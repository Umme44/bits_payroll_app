package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Holidays;
import com.bits.hr.domain.enumeration.HolidayType;
import com.bits.hr.repository.HolidaysRepository;
import com.bits.hr.service.dto.HolidaysDTO;
import com.bits.hr.service.mapper.HolidaysMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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

/**
 * Integration tests for the {@link HolidaysResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HolidaysResourceIT {

    private static final HolidayType DEFAULT_HOLIDAY_TYPE = HolidayType.General;
    private static final HolidayType UPDATED_HOLIDAY_TYPE = HolidayType.Govt;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_MOON_DEPENDENT = false;
    private static final Boolean UPDATED_IS_MOON_DEPENDENT = true;

    private static final String ENTITY_API_URL = "/api/holidays";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HolidaysRepository holidaysRepository;

    @Autowired
    private HolidaysMapper holidaysMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHolidaysMockMvc;

    private Holidays holidays;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Holidays createEntity(EntityManager em) {
        Holidays holidays = new Holidays()
            .holidayType(DEFAULT_HOLIDAY_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .isMoonDependent(DEFAULT_IS_MOON_DEPENDENT);
        return holidays;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Holidays createUpdatedEntity(EntityManager em) {
        Holidays holidays = new Holidays()
            .holidayType(UPDATED_HOLIDAY_TYPE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isMoonDependent(UPDATED_IS_MOON_DEPENDENT);
        return holidays;
    }

    @BeforeEach
    public void initTest() {
        holidays = createEntity(em);
    }

    @Test
    @Transactional
    void createHolidays() throws Exception {
        int databaseSizeBeforeCreate = holidaysRepository.findAll().size();
        // Create the Holidays
        HolidaysDTO holidaysDTO = holidaysMapper.toDto(holidays);
        restHolidaysMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidaysDTO)))
            .andExpect(status().isCreated());

        // Validate the Holidays in the database
        List<Holidays> holidaysList = holidaysRepository.findAll();
        assertThat(holidaysList).hasSize(databaseSizeBeforeCreate + 1);
        Holidays testHolidays = holidaysList.get(holidaysList.size() - 1);
        assertThat(testHolidays.getHolidayType()).isEqualTo(DEFAULT_HOLIDAY_TYPE);
        assertThat(testHolidays.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testHolidays.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testHolidays.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testHolidays.getIsMoonDependent()).isEqualTo(DEFAULT_IS_MOON_DEPENDENT);
    }

    @Test
    @Transactional
    void createHolidaysWithExistingId() throws Exception {
        // Create the Holidays with an existing ID
        holidays.setId(1L);
        HolidaysDTO holidaysDTO = holidaysMapper.toDto(holidays);

        int databaseSizeBeforeCreate = holidaysRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHolidaysMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidaysDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Holidays in the database
        List<Holidays> holidaysList = holidaysRepository.findAll();
        assertThat(holidaysList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHolidays() throws Exception {
        // Initialize the database
        holidaysRepository.saveAndFlush(holidays);

        // Get all the holidaysList
        restHolidaysMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holidays.getId().intValue())))
            .andExpect(jsonPath("$.[*].holidayType").value(hasItem(DEFAULT_HOLIDAY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isMoonDependent").value(hasItem(DEFAULT_IS_MOON_DEPENDENT.booleanValue())));
    }

    @Test
    @Transactional
    void getHolidays() throws Exception {
        // Initialize the database
        holidaysRepository.saveAndFlush(holidays);

        // Get the holidays
        restHolidaysMockMvc
            .perform(get(ENTITY_API_URL_ID, holidays.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(holidays.getId().intValue()))
            .andExpect(jsonPath("$.holidayType").value(DEFAULT_HOLIDAY_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.isMoonDependent").value(DEFAULT_IS_MOON_DEPENDENT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingHolidays() throws Exception {
        // Get the holidays
        restHolidaysMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHolidays() throws Exception {
        // Initialize the database
        holidaysRepository.saveAndFlush(holidays);

        int databaseSizeBeforeUpdate = holidaysRepository.findAll().size();

        // Update the holidays
        Holidays updatedHolidays = holidaysRepository.findById(holidays.getId()).get();
        // Disconnect from session so that the updates on updatedHolidays are not directly saved in db
        em.detach(updatedHolidays);
        updatedHolidays
            .holidayType(UPDATED_HOLIDAY_TYPE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isMoonDependent(UPDATED_IS_MOON_DEPENDENT);
        HolidaysDTO holidaysDTO = holidaysMapper.toDto(updatedHolidays);

        restHolidaysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, holidaysDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holidaysDTO))
            )
            .andExpect(status().isOk());

        // Validate the Holidays in the database
        List<Holidays> holidaysList = holidaysRepository.findAll();
        assertThat(holidaysList).hasSize(databaseSizeBeforeUpdate);
        Holidays testHolidays = holidaysList.get(holidaysList.size() - 1);
        assertThat(testHolidays.getHolidayType()).isEqualTo(UPDATED_HOLIDAY_TYPE);
        assertThat(testHolidays.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHolidays.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testHolidays.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testHolidays.getIsMoonDependent()).isEqualTo(UPDATED_IS_MOON_DEPENDENT);
    }

    @Test
    @Transactional
    void putNonExistingHolidays() throws Exception {
        int databaseSizeBeforeUpdate = holidaysRepository.findAll().size();
        holidays.setId(count.incrementAndGet());

        // Create the Holidays
        HolidaysDTO holidaysDTO = holidaysMapper.toDto(holidays);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHolidaysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, holidaysDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holidaysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holidays in the database
        List<Holidays> holidaysList = holidaysRepository.findAll();
        assertThat(holidaysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHolidays() throws Exception {
        int databaseSizeBeforeUpdate = holidaysRepository.findAll().size();
        holidays.setId(count.incrementAndGet());

        // Create the Holidays
        HolidaysDTO holidaysDTO = holidaysMapper.toDto(holidays);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidaysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holidaysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holidays in the database
        List<Holidays> holidaysList = holidaysRepository.findAll();
        assertThat(holidaysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHolidays() throws Exception {
        int databaseSizeBeforeUpdate = holidaysRepository.findAll().size();
        holidays.setId(count.incrementAndGet());

        // Create the Holidays
        HolidaysDTO holidaysDTO = holidaysMapper.toDto(holidays);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidaysMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidaysDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Holidays in the database
        List<Holidays> holidaysList = holidaysRepository.findAll();
        assertThat(holidaysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHolidaysWithPatch() throws Exception {
        // Initialize the database
        holidaysRepository.saveAndFlush(holidays);

        int databaseSizeBeforeUpdate = holidaysRepository.findAll().size();

        // Update the holidays using partial update
        Holidays partialUpdatedHolidays = new Holidays();
        partialUpdatedHolidays.setId(holidays.getId());

        partialUpdatedHolidays
            .holidayType(UPDATED_HOLIDAY_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isMoonDependent(UPDATED_IS_MOON_DEPENDENT);

        restHolidaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHolidays.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHolidays))
            )
            .andExpect(status().isOk());

        // Validate the Holidays in the database
        List<Holidays> holidaysList = holidaysRepository.findAll();
        assertThat(holidaysList).hasSize(databaseSizeBeforeUpdate);
        Holidays testHolidays = holidaysList.get(holidaysList.size() - 1);
        assertThat(testHolidays.getHolidayType()).isEqualTo(UPDATED_HOLIDAY_TYPE);
        assertThat(testHolidays.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testHolidays.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testHolidays.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testHolidays.getIsMoonDependent()).isEqualTo(UPDATED_IS_MOON_DEPENDENT);
    }

    @Test
    @Transactional
    void fullUpdateHolidaysWithPatch() throws Exception {
        // Initialize the database
        holidaysRepository.saveAndFlush(holidays);

        int databaseSizeBeforeUpdate = holidaysRepository.findAll().size();

        // Update the holidays using partial update
        Holidays partialUpdatedHolidays = new Holidays();
        partialUpdatedHolidays.setId(holidays.getId());

        partialUpdatedHolidays
            .holidayType(UPDATED_HOLIDAY_TYPE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isMoonDependent(UPDATED_IS_MOON_DEPENDENT);

        restHolidaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHolidays.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHolidays))
            )
            .andExpect(status().isOk());

        // Validate the Holidays in the database
        List<Holidays> holidaysList = holidaysRepository.findAll();
        assertThat(holidaysList).hasSize(databaseSizeBeforeUpdate);
        Holidays testHolidays = holidaysList.get(holidaysList.size() - 1);
        assertThat(testHolidays.getHolidayType()).isEqualTo(UPDATED_HOLIDAY_TYPE);
        assertThat(testHolidays.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHolidays.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testHolidays.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testHolidays.getIsMoonDependent()).isEqualTo(UPDATED_IS_MOON_DEPENDENT);
    }

    @Test
    @Transactional
    void patchNonExistingHolidays() throws Exception {
        int databaseSizeBeforeUpdate = holidaysRepository.findAll().size();
        holidays.setId(count.incrementAndGet());

        // Create the Holidays
        HolidaysDTO holidaysDTO = holidaysMapper.toDto(holidays);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHolidaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, holidaysDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holidaysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holidays in the database
        List<Holidays> holidaysList = holidaysRepository.findAll();
        assertThat(holidaysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHolidays() throws Exception {
        int databaseSizeBeforeUpdate = holidaysRepository.findAll().size();
        holidays.setId(count.incrementAndGet());

        // Create the Holidays
        HolidaysDTO holidaysDTO = holidaysMapper.toDto(holidays);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holidaysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holidays in the database
        List<Holidays> holidaysList = holidaysRepository.findAll();
        assertThat(holidaysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHolidays() throws Exception {
        int databaseSizeBeforeUpdate = holidaysRepository.findAll().size();
        holidays.setId(count.incrementAndGet());

        // Create the Holidays
        HolidaysDTO holidaysDTO = holidaysMapper.toDto(holidays);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidaysMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(holidaysDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Holidays in the database
        List<Holidays> holidaysList = holidaysRepository.findAll();
        assertThat(holidaysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHolidays() throws Exception {
        // Initialize the database
        holidaysRepository.saveAndFlush(holidays);

        int databaseSizeBeforeDelete = holidaysRepository.findAll().size();

        // Delete the holidays
        restHolidaysMockMvc
            .perform(delete(ENTITY_API_URL_ID, holidays.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Holidays> holidaysList = holidaysRepository.findAll();
        assertThat(holidaysList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
