package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.CalenderYear;
import com.bits.hr.repository.CalenderYearRepository;
import com.bits.hr.service.dto.CalenderYearDTO;
import com.bits.hr.service.mapper.CalenderYearMapper;
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
 * Integration tests for the {@link CalenderYearResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CalenderYearResourceIT {

    private static final Integer DEFAULT_YEAR = 1900;
    private static final Integer UPDATED_YEAR = 1901;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/calender-years";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CalenderYearRepository calenderYearRepository;

    @Autowired
    private CalenderYearMapper calenderYearMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCalenderYearMockMvc;

    private CalenderYear calenderYear;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalenderYear createEntity(EntityManager em) {
        CalenderYear calenderYear = new CalenderYear().year(DEFAULT_YEAR).startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE);
        return calenderYear;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalenderYear createUpdatedEntity(EntityManager em) {
        CalenderYear calenderYear = new CalenderYear().year(UPDATED_YEAR).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);
        return calenderYear;
    }

    @BeforeEach
    public void initTest() {
        calenderYear = createEntity(em);
    }

    @Test
    @Transactional
    void createCalenderYear() throws Exception {
        int databaseSizeBeforeCreate = calenderYearRepository.findAll().size();
        // Create the CalenderYear
        CalenderYearDTO calenderYearDTO = calenderYearMapper.toDto(calenderYear);
        restCalenderYearMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calenderYearDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CalenderYear in the database
        List<CalenderYear> calenderYearList = calenderYearRepository.findAll();
        assertThat(calenderYearList).hasSize(databaseSizeBeforeCreate + 1);
        CalenderYear testCalenderYear = calenderYearList.get(calenderYearList.size() - 1);
        assertThat(testCalenderYear.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testCalenderYear.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCalenderYear.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createCalenderYearWithExistingId() throws Exception {
        // Create the CalenderYear with an existing ID
        calenderYear.setId(1L);
        CalenderYearDTO calenderYearDTO = calenderYearMapper.toDto(calenderYear);

        int databaseSizeBeforeCreate = calenderYearRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalenderYearMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calenderYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalenderYear in the database
        List<CalenderYear> calenderYearList = calenderYearRepository.findAll();
        assertThat(calenderYearList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = calenderYearRepository.findAll().size();
        // set the field null
        calenderYear.setYear(null);

        // Create the CalenderYear, which fails.
        CalenderYearDTO calenderYearDTO = calenderYearMapper.toDto(calenderYear);

        restCalenderYearMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calenderYearDTO))
            )
            .andExpect(status().isBadRequest());

        List<CalenderYear> calenderYearList = calenderYearRepository.findAll();
        assertThat(calenderYearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = calenderYearRepository.findAll().size();
        // set the field null
        calenderYear.setStartDate(null);

        // Create the CalenderYear, which fails.
        CalenderYearDTO calenderYearDTO = calenderYearMapper.toDto(calenderYear);

        restCalenderYearMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calenderYearDTO))
            )
            .andExpect(status().isBadRequest());

        List<CalenderYear> calenderYearList = calenderYearRepository.findAll();
        assertThat(calenderYearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = calenderYearRepository.findAll().size();
        // set the field null
        calenderYear.setEndDate(null);

        // Create the CalenderYear, which fails.
        CalenderYearDTO calenderYearDTO = calenderYearMapper.toDto(calenderYear);

        restCalenderYearMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calenderYearDTO))
            )
            .andExpect(status().isBadRequest());

        List<CalenderYear> calenderYearList = calenderYearRepository.findAll();
        assertThat(calenderYearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCalenderYears() throws Exception {
        // Initialize the database
        calenderYearRepository.saveAndFlush(calenderYear);

        // Get all the calenderYearList
        restCalenderYearMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calenderYear.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getCalenderYear() throws Exception {
        // Initialize the database
        calenderYearRepository.saveAndFlush(calenderYear);

        // Get the calenderYear
        restCalenderYearMockMvc
            .perform(get(ENTITY_API_URL_ID, calenderYear.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calenderYear.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCalenderYear() throws Exception {
        // Get the calenderYear
        restCalenderYearMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCalenderYear() throws Exception {
        // Initialize the database
        calenderYearRepository.saveAndFlush(calenderYear);

        int databaseSizeBeforeUpdate = calenderYearRepository.findAll().size();

        // Update the calenderYear
        CalenderYear updatedCalenderYear = calenderYearRepository.findById(calenderYear.getId()).get();
        // Disconnect from session so that the updates on updatedCalenderYear are not directly saved in db
        em.detach(updatedCalenderYear);
        updatedCalenderYear.year(UPDATED_YEAR).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);
        CalenderYearDTO calenderYearDTO = calenderYearMapper.toDto(updatedCalenderYear);

        restCalenderYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calenderYearDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calenderYearDTO))
            )
            .andExpect(status().isOk());

        // Validate the CalenderYear in the database
        List<CalenderYear> calenderYearList = calenderYearRepository.findAll();
        assertThat(calenderYearList).hasSize(databaseSizeBeforeUpdate);
        CalenderYear testCalenderYear = calenderYearList.get(calenderYearList.size() - 1);
        assertThat(testCalenderYear.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testCalenderYear.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCalenderYear.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCalenderYear() throws Exception {
        int databaseSizeBeforeUpdate = calenderYearRepository.findAll().size();
        calenderYear.setId(count.incrementAndGet());

        // Create the CalenderYear
        CalenderYearDTO calenderYearDTO = calenderYearMapper.toDto(calenderYear);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalenderYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calenderYearDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calenderYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalenderYear in the database
        List<CalenderYear> calenderYearList = calenderYearRepository.findAll();
        assertThat(calenderYearList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCalenderYear() throws Exception {
        int databaseSizeBeforeUpdate = calenderYearRepository.findAll().size();
        calenderYear.setId(count.incrementAndGet());

        // Create the CalenderYear
        CalenderYearDTO calenderYearDTO = calenderYearMapper.toDto(calenderYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalenderYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calenderYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalenderYear in the database
        List<CalenderYear> calenderYearList = calenderYearRepository.findAll();
        assertThat(calenderYearList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCalenderYear() throws Exception {
        int databaseSizeBeforeUpdate = calenderYearRepository.findAll().size();
        calenderYear.setId(count.incrementAndGet());

        // Create the CalenderYear
        CalenderYearDTO calenderYearDTO = calenderYearMapper.toDto(calenderYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalenderYearMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calenderYearDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CalenderYear in the database
        List<CalenderYear> calenderYearList = calenderYearRepository.findAll();
        assertThat(calenderYearList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCalenderYearWithPatch() throws Exception {
        // Initialize the database
        calenderYearRepository.saveAndFlush(calenderYear);

        int databaseSizeBeforeUpdate = calenderYearRepository.findAll().size();

        // Update the calenderYear using partial update
        CalenderYear partialUpdatedCalenderYear = new CalenderYear();
        partialUpdatedCalenderYear.setId(calenderYear.getId());

        partialUpdatedCalenderYear.startDate(UPDATED_START_DATE);

        restCalenderYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalenderYear.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalenderYear))
            )
            .andExpect(status().isOk());

        // Validate the CalenderYear in the database
        List<CalenderYear> calenderYearList = calenderYearRepository.findAll();
        assertThat(calenderYearList).hasSize(databaseSizeBeforeUpdate);
        CalenderYear testCalenderYear = calenderYearList.get(calenderYearList.size() - 1);
        assertThat(testCalenderYear.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testCalenderYear.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCalenderYear.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCalenderYearWithPatch() throws Exception {
        // Initialize the database
        calenderYearRepository.saveAndFlush(calenderYear);

        int databaseSizeBeforeUpdate = calenderYearRepository.findAll().size();

        // Update the calenderYear using partial update
        CalenderYear partialUpdatedCalenderYear = new CalenderYear();
        partialUpdatedCalenderYear.setId(calenderYear.getId());

        partialUpdatedCalenderYear.year(UPDATED_YEAR).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restCalenderYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalenderYear.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalenderYear))
            )
            .andExpect(status().isOk());

        // Validate the CalenderYear in the database
        List<CalenderYear> calenderYearList = calenderYearRepository.findAll();
        assertThat(calenderYearList).hasSize(databaseSizeBeforeUpdate);
        CalenderYear testCalenderYear = calenderYearList.get(calenderYearList.size() - 1);
        assertThat(testCalenderYear.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testCalenderYear.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCalenderYear.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCalenderYear() throws Exception {
        int databaseSizeBeforeUpdate = calenderYearRepository.findAll().size();
        calenderYear.setId(count.incrementAndGet());

        // Create the CalenderYear
        CalenderYearDTO calenderYearDTO = calenderYearMapper.toDto(calenderYear);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalenderYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, calenderYearDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calenderYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalenderYear in the database
        List<CalenderYear> calenderYearList = calenderYearRepository.findAll();
        assertThat(calenderYearList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCalenderYear() throws Exception {
        int databaseSizeBeforeUpdate = calenderYearRepository.findAll().size();
        calenderYear.setId(count.incrementAndGet());

        // Create the CalenderYear
        CalenderYearDTO calenderYearDTO = calenderYearMapper.toDto(calenderYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalenderYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calenderYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalenderYear in the database
        List<CalenderYear> calenderYearList = calenderYearRepository.findAll();
        assertThat(calenderYearList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCalenderYear() throws Exception {
        int databaseSizeBeforeUpdate = calenderYearRepository.findAll().size();
        calenderYear.setId(count.incrementAndGet());

        // Create the CalenderYear
        CalenderYearDTO calenderYearDTO = calenderYearMapper.toDto(calenderYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalenderYearMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calenderYearDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CalenderYear in the database
        List<CalenderYear> calenderYearList = calenderYearRepository.findAll();
        assertThat(calenderYearList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCalenderYear() throws Exception {
        // Initialize the database
        calenderYearRepository.saveAndFlush(calenderYear);

        int databaseSizeBeforeDelete = calenderYearRepository.findAll().size();

        // Delete the calenderYear
        restCalenderYearMockMvc
            .perform(delete(ENTITY_API_URL_ID, calenderYear.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CalenderYear> calenderYearList = calenderYearRepository.findAll();
        assertThat(calenderYearList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
