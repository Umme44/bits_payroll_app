package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.EmploymentHistory;
import com.bits.hr.domain.enumeration.EventType;
import com.bits.hr.repository.EmploymentHistoryRepository;
import com.bits.hr.service.dto.EmploymentHistoryDTO;
import com.bits.hr.service.mapper.EmploymentHistoryMapper;
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
 * Integration tests for the {@link EmploymentHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmploymentHistoryResourceIT {

    private static final String DEFAULT_REFERENCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PIN = "AAAAAAAAAA";
    private static final String UPDATED_PIN = "BBBBBBBBBB";

    private static final EventType DEFAULT_EVENT_TYPE = EventType.TRANSFER;
    private static final EventType UPDATED_EVENT_TYPE = EventType.PROMOTION;

    private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_PREVIOUS_MAIN_GROSS_SALARY = 1D;
    private static final Double UPDATED_PREVIOUS_MAIN_GROSS_SALARY = 2D;

    private static final Double DEFAULT_CURRENT_MAIN_GROSS_SALARY = 1D;
    private static final Double UPDATED_CURRENT_MAIN_GROSS_SALARY = 2D;

    private static final String DEFAULT_PREVIOUS_WORKING_HOUR = "AAAAAAAAAA";
    private static final String UPDATED_PREVIOUS_WORKING_HOUR = "BBBBBBBBBB";

    private static final String DEFAULT_CHANGED_WORKING_HOUR = "AAAAAAAAAA";
    private static final String UPDATED_CHANGED_WORKING_HOUR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_MODIFIABLE = false;
    private static final Boolean UPDATED_IS_MODIFIABLE = true;

    private static final String ENTITY_API_URL = "/api/employment-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmploymentHistoryRepository employmentHistoryRepository;

    @Autowired
    private EmploymentHistoryMapper employmentHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmploymentHistoryMockMvc;

    private EmploymentHistory employmentHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmploymentHistory createEntity(EntityManager em) {
        EmploymentHistory employmentHistory = new EmploymentHistory()
            .referenceId(DEFAULT_REFERENCE_ID)
            .pin(DEFAULT_PIN)
            .eventType(DEFAULT_EVENT_TYPE)
            .effectiveDate(DEFAULT_EFFECTIVE_DATE)
            .previousMainGrossSalary(DEFAULT_PREVIOUS_MAIN_GROSS_SALARY)
            .currentMainGrossSalary(DEFAULT_CURRENT_MAIN_GROSS_SALARY)
            .previousWorkingHour(DEFAULT_PREVIOUS_WORKING_HOUR)
            .changedWorkingHour(DEFAULT_CHANGED_WORKING_HOUR)
            .isModifiable(DEFAULT_IS_MODIFIABLE);
        return employmentHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmploymentHistory createUpdatedEntity(EntityManager em) {
        EmploymentHistory employmentHistory = new EmploymentHistory()
            .referenceId(UPDATED_REFERENCE_ID)
            .pin(UPDATED_PIN)
            .eventType(UPDATED_EVENT_TYPE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .previousMainGrossSalary(UPDATED_PREVIOUS_MAIN_GROSS_SALARY)
            .currentMainGrossSalary(UPDATED_CURRENT_MAIN_GROSS_SALARY)
            .previousWorkingHour(UPDATED_PREVIOUS_WORKING_HOUR)
            .changedWorkingHour(UPDATED_CHANGED_WORKING_HOUR)
            .isModifiable(UPDATED_IS_MODIFIABLE);
        return employmentHistory;
    }

    @BeforeEach
    public void initTest() {
        employmentHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createEmploymentHistory() throws Exception {
        int databaseSizeBeforeCreate = employmentHistoryRepository.findAll().size();
        // Create the EmploymentHistory
        EmploymentHistoryDTO employmentHistoryDTO = employmentHistoryMapper.toDto(employmentHistory);
        restEmploymentHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EmploymentHistory in the database
        List<EmploymentHistory> employmentHistoryList = employmentHistoryRepository.findAll();
        assertThat(employmentHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        EmploymentHistory testEmploymentHistory = employmentHistoryList.get(employmentHistoryList.size() - 1);
        assertThat(testEmploymentHistory.getReferenceId()).isEqualTo(DEFAULT_REFERENCE_ID);
        assertThat(testEmploymentHistory.getPin()).isEqualTo(DEFAULT_PIN);
        assertThat(testEmploymentHistory.getEventType()).isEqualTo(DEFAULT_EVENT_TYPE);
        assertThat(testEmploymentHistory.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testEmploymentHistory.getPreviousMainGrossSalary()).isEqualTo(DEFAULT_PREVIOUS_MAIN_GROSS_SALARY);
        assertThat(testEmploymentHistory.getCurrentMainGrossSalary()).isEqualTo(DEFAULT_CURRENT_MAIN_GROSS_SALARY);
        assertThat(testEmploymentHistory.getPreviousWorkingHour()).isEqualTo(DEFAULT_PREVIOUS_WORKING_HOUR);
        assertThat(testEmploymentHistory.getChangedWorkingHour()).isEqualTo(DEFAULT_CHANGED_WORKING_HOUR);
        assertThat(testEmploymentHistory.getIsModifiable()).isEqualTo(DEFAULT_IS_MODIFIABLE);
    }

    @Test
    @Transactional
    void createEmploymentHistoryWithExistingId() throws Exception {
        // Create the EmploymentHistory with an existing ID
        employmentHistory.setId(1L);
        EmploymentHistoryDTO employmentHistoryDTO = employmentHistoryMapper.toDto(employmentHistory);

        int databaseSizeBeforeCreate = employmentHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmploymentHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentHistory in the database
        List<EmploymentHistory> employmentHistoryList = employmentHistoryRepository.findAll();
        assertThat(employmentHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmploymentHistories() throws Exception {
        // Initialize the database
        employmentHistoryRepository.saveAndFlush(employmentHistory);

        // Get all the employmentHistoryList
        restEmploymentHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employmentHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].referenceId").value(hasItem(DEFAULT_REFERENCE_ID)))
            .andExpect(jsonPath("$.[*].pin").value(hasItem(DEFAULT_PIN)))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].previousMainGrossSalary").value(hasItem(DEFAULT_PREVIOUS_MAIN_GROSS_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].currentMainGrossSalary").value(hasItem(DEFAULT_CURRENT_MAIN_GROSS_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].previousWorkingHour").value(hasItem(DEFAULT_PREVIOUS_WORKING_HOUR)))
            .andExpect(jsonPath("$.[*].changedWorkingHour").value(hasItem(DEFAULT_CHANGED_WORKING_HOUR)))
            .andExpect(jsonPath("$.[*].isModifiable").value(hasItem(DEFAULT_IS_MODIFIABLE.booleanValue())));
    }

    @Test
    @Transactional
    void getEmploymentHistory() throws Exception {
        // Initialize the database
        employmentHistoryRepository.saveAndFlush(employmentHistory);

        // Get the employmentHistory
        restEmploymentHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, employmentHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employmentHistory.getId().intValue()))
            .andExpect(jsonPath("$.referenceId").value(DEFAULT_REFERENCE_ID))
            .andExpect(jsonPath("$.pin").value(DEFAULT_PIN))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE.toString()))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.previousMainGrossSalary").value(DEFAULT_PREVIOUS_MAIN_GROSS_SALARY.doubleValue()))
            .andExpect(jsonPath("$.currentMainGrossSalary").value(DEFAULT_CURRENT_MAIN_GROSS_SALARY.doubleValue()))
            .andExpect(jsonPath("$.previousWorkingHour").value(DEFAULT_PREVIOUS_WORKING_HOUR))
            .andExpect(jsonPath("$.changedWorkingHour").value(DEFAULT_CHANGED_WORKING_HOUR))
            .andExpect(jsonPath("$.isModifiable").value(DEFAULT_IS_MODIFIABLE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEmploymentHistory() throws Exception {
        // Get the employmentHistory
        restEmploymentHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmploymentHistory() throws Exception {
        // Initialize the database
        employmentHistoryRepository.saveAndFlush(employmentHistory);

        int databaseSizeBeforeUpdate = employmentHistoryRepository.findAll().size();

        // Update the employmentHistory
        EmploymentHistory updatedEmploymentHistory = employmentHistoryRepository.findById(employmentHistory.getId()).get();
        // Disconnect from session so that the updates on updatedEmploymentHistory are not directly saved in db
        em.detach(updatedEmploymentHistory);
        updatedEmploymentHistory
            .referenceId(UPDATED_REFERENCE_ID)
            .pin(UPDATED_PIN)
            .eventType(UPDATED_EVENT_TYPE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .previousMainGrossSalary(UPDATED_PREVIOUS_MAIN_GROSS_SALARY)
            .currentMainGrossSalary(UPDATED_CURRENT_MAIN_GROSS_SALARY)
            .previousWorkingHour(UPDATED_PREVIOUS_WORKING_HOUR)
            .changedWorkingHour(UPDATED_CHANGED_WORKING_HOUR)
            .isModifiable(UPDATED_IS_MODIFIABLE);
        EmploymentHistoryDTO employmentHistoryDTO = employmentHistoryMapper.toDto(updatedEmploymentHistory);

        restEmploymentHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employmentHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentHistory in the database
        List<EmploymentHistory> employmentHistoryList = employmentHistoryRepository.findAll();
        assertThat(employmentHistoryList).hasSize(databaseSizeBeforeUpdate);
        EmploymentHistory testEmploymentHistory = employmentHistoryList.get(employmentHistoryList.size() - 1);
        assertThat(testEmploymentHistory.getReferenceId()).isEqualTo(UPDATED_REFERENCE_ID);
        assertThat(testEmploymentHistory.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testEmploymentHistory.getEventType()).isEqualTo(UPDATED_EVENT_TYPE);
        assertThat(testEmploymentHistory.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testEmploymentHistory.getPreviousMainGrossSalary()).isEqualTo(UPDATED_PREVIOUS_MAIN_GROSS_SALARY);
        assertThat(testEmploymentHistory.getCurrentMainGrossSalary()).isEqualTo(UPDATED_CURRENT_MAIN_GROSS_SALARY);
        assertThat(testEmploymentHistory.getPreviousWorkingHour()).isEqualTo(UPDATED_PREVIOUS_WORKING_HOUR);
        assertThat(testEmploymentHistory.getChangedWorkingHour()).isEqualTo(UPDATED_CHANGED_WORKING_HOUR);
        assertThat(testEmploymentHistory.getIsModifiable()).isEqualTo(UPDATED_IS_MODIFIABLE);
    }

    @Test
    @Transactional
    void putNonExistingEmploymentHistory() throws Exception {
        int databaseSizeBeforeUpdate = employmentHistoryRepository.findAll().size();
        employmentHistory.setId(count.incrementAndGet());

        // Create the EmploymentHistory
        EmploymentHistoryDTO employmentHistoryDTO = employmentHistoryMapper.toDto(employmentHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employmentHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentHistory in the database
        List<EmploymentHistory> employmentHistoryList = employmentHistoryRepository.findAll();
        assertThat(employmentHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmploymentHistory() throws Exception {
        int databaseSizeBeforeUpdate = employmentHistoryRepository.findAll().size();
        employmentHistory.setId(count.incrementAndGet());

        // Create the EmploymentHistory
        EmploymentHistoryDTO employmentHistoryDTO = employmentHistoryMapper.toDto(employmentHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentHistory in the database
        List<EmploymentHistory> employmentHistoryList = employmentHistoryRepository.findAll();
        assertThat(employmentHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmploymentHistory() throws Exception {
        int databaseSizeBeforeUpdate = employmentHistoryRepository.findAll().size();
        employmentHistory.setId(count.incrementAndGet());

        // Create the EmploymentHistory
        EmploymentHistoryDTO employmentHistoryDTO = employmentHistoryMapper.toDto(employmentHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentHistoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmploymentHistory in the database
        List<EmploymentHistory> employmentHistoryList = employmentHistoryRepository.findAll();
        assertThat(employmentHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmploymentHistoryWithPatch() throws Exception {
        // Initialize the database
        employmentHistoryRepository.saveAndFlush(employmentHistory);

        int databaseSizeBeforeUpdate = employmentHistoryRepository.findAll().size();

        // Update the employmentHistory using partial update
        EmploymentHistory partialUpdatedEmploymentHistory = new EmploymentHistory();
        partialUpdatedEmploymentHistory.setId(employmentHistory.getId());

        partialUpdatedEmploymentHistory
            .referenceId(UPDATED_REFERENCE_ID)
            .pin(UPDATED_PIN)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .previousMainGrossSalary(UPDATED_PREVIOUS_MAIN_GROSS_SALARY)
            .isModifiable(UPDATED_IS_MODIFIABLE);

        restEmploymentHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploymentHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmploymentHistory))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentHistory in the database
        List<EmploymentHistory> employmentHistoryList = employmentHistoryRepository.findAll();
        assertThat(employmentHistoryList).hasSize(databaseSizeBeforeUpdate);
        EmploymentHistory testEmploymentHistory = employmentHistoryList.get(employmentHistoryList.size() - 1);
        assertThat(testEmploymentHistory.getReferenceId()).isEqualTo(UPDATED_REFERENCE_ID);
        assertThat(testEmploymentHistory.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testEmploymentHistory.getEventType()).isEqualTo(DEFAULT_EVENT_TYPE);
        assertThat(testEmploymentHistory.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testEmploymentHistory.getPreviousMainGrossSalary()).isEqualTo(UPDATED_PREVIOUS_MAIN_GROSS_SALARY);
        assertThat(testEmploymentHistory.getCurrentMainGrossSalary()).isEqualTo(DEFAULT_CURRENT_MAIN_GROSS_SALARY);
        assertThat(testEmploymentHistory.getPreviousWorkingHour()).isEqualTo(DEFAULT_PREVIOUS_WORKING_HOUR);
        assertThat(testEmploymentHistory.getChangedWorkingHour()).isEqualTo(DEFAULT_CHANGED_WORKING_HOUR);
        assertThat(testEmploymentHistory.getIsModifiable()).isEqualTo(UPDATED_IS_MODIFIABLE);
    }

    @Test
    @Transactional
    void fullUpdateEmploymentHistoryWithPatch() throws Exception {
        // Initialize the database
        employmentHistoryRepository.saveAndFlush(employmentHistory);

        int databaseSizeBeforeUpdate = employmentHistoryRepository.findAll().size();

        // Update the employmentHistory using partial update
        EmploymentHistory partialUpdatedEmploymentHistory = new EmploymentHistory();
        partialUpdatedEmploymentHistory.setId(employmentHistory.getId());

        partialUpdatedEmploymentHistory
            .referenceId(UPDATED_REFERENCE_ID)
            .pin(UPDATED_PIN)
            .eventType(UPDATED_EVENT_TYPE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .previousMainGrossSalary(UPDATED_PREVIOUS_MAIN_GROSS_SALARY)
            .currentMainGrossSalary(UPDATED_CURRENT_MAIN_GROSS_SALARY)
            .previousWorkingHour(UPDATED_PREVIOUS_WORKING_HOUR)
            .changedWorkingHour(UPDATED_CHANGED_WORKING_HOUR)
            .isModifiable(UPDATED_IS_MODIFIABLE);

        restEmploymentHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploymentHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmploymentHistory))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentHistory in the database
        List<EmploymentHistory> employmentHistoryList = employmentHistoryRepository.findAll();
        assertThat(employmentHistoryList).hasSize(databaseSizeBeforeUpdate);
        EmploymentHistory testEmploymentHistory = employmentHistoryList.get(employmentHistoryList.size() - 1);
        assertThat(testEmploymentHistory.getReferenceId()).isEqualTo(UPDATED_REFERENCE_ID);
        assertThat(testEmploymentHistory.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testEmploymentHistory.getEventType()).isEqualTo(UPDATED_EVENT_TYPE);
        assertThat(testEmploymentHistory.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testEmploymentHistory.getPreviousMainGrossSalary()).isEqualTo(UPDATED_PREVIOUS_MAIN_GROSS_SALARY);
        assertThat(testEmploymentHistory.getCurrentMainGrossSalary()).isEqualTo(UPDATED_CURRENT_MAIN_GROSS_SALARY);
        assertThat(testEmploymentHistory.getPreviousWorkingHour()).isEqualTo(UPDATED_PREVIOUS_WORKING_HOUR);
        assertThat(testEmploymentHistory.getChangedWorkingHour()).isEqualTo(UPDATED_CHANGED_WORKING_HOUR);
        assertThat(testEmploymentHistory.getIsModifiable()).isEqualTo(UPDATED_IS_MODIFIABLE);
    }

    @Test
    @Transactional
    void patchNonExistingEmploymentHistory() throws Exception {
        int databaseSizeBeforeUpdate = employmentHistoryRepository.findAll().size();
        employmentHistory.setId(count.incrementAndGet());

        // Create the EmploymentHistory
        EmploymentHistoryDTO employmentHistoryDTO = employmentHistoryMapper.toDto(employmentHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employmentHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentHistory in the database
        List<EmploymentHistory> employmentHistoryList = employmentHistoryRepository.findAll();
        assertThat(employmentHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmploymentHistory() throws Exception {
        int databaseSizeBeforeUpdate = employmentHistoryRepository.findAll().size();
        employmentHistory.setId(count.incrementAndGet());

        // Create the EmploymentHistory
        EmploymentHistoryDTO employmentHistoryDTO = employmentHistoryMapper.toDto(employmentHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentHistory in the database
        List<EmploymentHistory> employmentHistoryList = employmentHistoryRepository.findAll();
        assertThat(employmentHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmploymentHistory() throws Exception {
        int databaseSizeBeforeUpdate = employmentHistoryRepository.findAll().size();
        employmentHistory.setId(count.incrementAndGet());

        // Create the EmploymentHistory
        EmploymentHistoryDTO employmentHistoryDTO = employmentHistoryMapper.toDto(employmentHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmploymentHistory in the database
        List<EmploymentHistory> employmentHistoryList = employmentHistoryRepository.findAll();
        assertThat(employmentHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmploymentHistory() throws Exception {
        // Initialize the database
        employmentHistoryRepository.saveAndFlush(employmentHistory);

        int databaseSizeBeforeDelete = employmentHistoryRepository.findAll().size();

        // Delete the employmentHistory
        restEmploymentHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, employmentHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmploymentHistory> employmentHistoryList = employmentHistoryRepository.findAll();
        assertThat(employmentHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
