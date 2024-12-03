package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.EmployeeResignationRepository;
import com.bits.hr.service.dto.EmployeeResignationDTO;
import com.bits.hr.service.mapper.EmployeeResignationMapper;
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
 * Integration tests for the {@link EmployeeResignationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeResignationResourceIT {

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RESIGNATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RESIGNATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Status DEFAULT_APPROVAL_STATUS = Status.PENDING;
    private static final Status UPDATED_APPROVAL_STATUS = Status.APPROVED;

    private static final String DEFAULT_APPROVAL_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_APPROVAL_COMMENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SALARY_HOLD = false;
    private static final Boolean UPDATED_IS_SALARY_HOLD = true;

    private static final Boolean DEFAULT_IS_FESTIVAL_BONUS_HOLD = false;
    private static final Boolean UPDATED_IS_FESTIVAL_BONUS_HOLD = true;

    private static final String DEFAULT_RESIGNATION_REASON = "AAAAAAAAAA";
    private static final String UPDATED_RESIGNATION_REASON = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LAST_WORKING_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_WORKING_DAY = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/employee-resignations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeResignationRepository employeeResignationRepository;

    @Autowired
    private EmployeeResignationMapper employeeResignationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeResignationMockMvc;

    private EmployeeResignation employeeResignation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeResignation createEntity(EntityManager em) {
        EmployeeResignation employeeResignation = new EmployeeResignation()
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .resignationDate(DEFAULT_RESIGNATION_DATE)
            .approvalStatus(DEFAULT_APPROVAL_STATUS)
            .approvalComment(DEFAULT_APPROVAL_COMMENT)
            .isSalaryHold(DEFAULT_IS_SALARY_HOLD)
            .isFestivalBonusHold(DEFAULT_IS_FESTIVAL_BONUS_HOLD)
            .resignationReason(DEFAULT_RESIGNATION_REASON)
            .lastWorkingDay(DEFAULT_LAST_WORKING_DAY);
        return employeeResignation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeResignation createUpdatedEntity(EntityManager em) {
        EmployeeResignation employeeResignation = new EmployeeResignation()
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .resignationDate(UPDATED_RESIGNATION_DATE)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .approvalComment(UPDATED_APPROVAL_COMMENT)
            .isSalaryHold(UPDATED_IS_SALARY_HOLD)
            .isFestivalBonusHold(UPDATED_IS_FESTIVAL_BONUS_HOLD)
            .resignationReason(UPDATED_RESIGNATION_REASON)
            .lastWorkingDay(UPDATED_LAST_WORKING_DAY);
        return employeeResignation;
    }

    @BeforeEach
    public void initTest() {
        employeeResignation = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeeResignation() throws Exception {
        int databaseSizeBeforeCreate = employeeResignationRepository.findAll().size();
        // Create the EmployeeResignation
        EmployeeResignationDTO employeeResignationDTO = employeeResignationMapper.toDto(employeeResignation);
        restEmployeeResignationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeResignationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EmployeeResignation in the database
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findAll();
        assertThat(employeeResignationList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeResignation testEmployeeResignation = employeeResignationList.get(employeeResignationList.size() - 1);
        assertThat(testEmployeeResignation.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEmployeeResignation.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testEmployeeResignation.getResignationDate()).isEqualTo(DEFAULT_RESIGNATION_DATE);
        assertThat(testEmployeeResignation.getApprovalStatus()).isEqualTo(DEFAULT_APPROVAL_STATUS);
        assertThat(testEmployeeResignation.getApprovalComment()).isEqualTo(DEFAULT_APPROVAL_COMMENT);
        assertThat(testEmployeeResignation.getIsSalaryHold()).isEqualTo(DEFAULT_IS_SALARY_HOLD);
        assertThat(testEmployeeResignation.getIsFestivalBonusHold()).isEqualTo(DEFAULT_IS_FESTIVAL_BONUS_HOLD);
        assertThat(testEmployeeResignation.getResignationReason()).isEqualTo(DEFAULT_RESIGNATION_REASON);
        assertThat(testEmployeeResignation.getLastWorkingDay()).isEqualTo(DEFAULT_LAST_WORKING_DAY);
    }

    @Test
    @Transactional
    void createEmployeeResignationWithExistingId() throws Exception {
        // Create the EmployeeResignation with an existing ID
        employeeResignation.setId(1L);
        EmployeeResignationDTO employeeResignationDTO = employeeResignationMapper.toDto(employeeResignation);

        int databaseSizeBeforeCreate = employeeResignationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeResignationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeResignationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeResignation in the database
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findAll();
        assertThat(employeeResignationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLastWorkingDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeResignationRepository.findAll().size();
        // set the field null
        employeeResignation.setLastWorkingDay(null);

        // Create the EmployeeResignation, which fails.
        EmployeeResignationDTO employeeResignationDTO = employeeResignationMapper.toDto(employeeResignation);

        restEmployeeResignationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeResignationDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findAll();
        assertThat(employeeResignationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeResignations() throws Exception {
        // Initialize the database
        employeeResignationRepository.saveAndFlush(employeeResignation);

        // Get all the employeeResignationList
        restEmployeeResignationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeResignation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].resignationDate").value(hasItem(DEFAULT_RESIGNATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].approvalStatus").value(hasItem(DEFAULT_APPROVAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].approvalComment").value(hasItem(DEFAULT_APPROVAL_COMMENT)))
            .andExpect(jsonPath("$.[*].isSalaryHold").value(hasItem(DEFAULT_IS_SALARY_HOLD.booleanValue())))
            .andExpect(jsonPath("$.[*].isFestivalBonusHold").value(hasItem(DEFAULT_IS_FESTIVAL_BONUS_HOLD.booleanValue())))
            .andExpect(jsonPath("$.[*].resignationReason").value(hasItem(DEFAULT_RESIGNATION_REASON)))
            .andExpect(jsonPath("$.[*].lastWorkingDay").value(hasItem(DEFAULT_LAST_WORKING_DAY.toString())));
    }

    @Test
    @Transactional
    void getEmployeeResignation() throws Exception {
        // Initialize the database
        employeeResignationRepository.saveAndFlush(employeeResignation);

        // Get the employeeResignation
        restEmployeeResignationMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeResignation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeResignation.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.resignationDate").value(DEFAULT_RESIGNATION_DATE.toString()))
            .andExpect(jsonPath("$.approvalStatus").value(DEFAULT_APPROVAL_STATUS.toString()))
            .andExpect(jsonPath("$.approvalComment").value(DEFAULT_APPROVAL_COMMENT))
            .andExpect(jsonPath("$.isSalaryHold").value(DEFAULT_IS_SALARY_HOLD.booleanValue()))
            .andExpect(jsonPath("$.isFestivalBonusHold").value(DEFAULT_IS_FESTIVAL_BONUS_HOLD.booleanValue()))
            .andExpect(jsonPath("$.resignationReason").value(DEFAULT_RESIGNATION_REASON))
            .andExpect(jsonPath("$.lastWorkingDay").value(DEFAULT_LAST_WORKING_DAY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeResignation() throws Exception {
        // Get the employeeResignation
        restEmployeeResignationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeResignation() throws Exception {
        // Initialize the database
        employeeResignationRepository.saveAndFlush(employeeResignation);

        int databaseSizeBeforeUpdate = employeeResignationRepository.findAll().size();

        // Update the employeeResignation
        EmployeeResignation updatedEmployeeResignation = employeeResignationRepository.findById(employeeResignation.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeeResignation are not directly saved in db
        em.detach(updatedEmployeeResignation);
        updatedEmployeeResignation
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .resignationDate(UPDATED_RESIGNATION_DATE)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .approvalComment(UPDATED_APPROVAL_COMMENT)
            .isSalaryHold(UPDATED_IS_SALARY_HOLD)
            .isFestivalBonusHold(UPDATED_IS_FESTIVAL_BONUS_HOLD)
            .resignationReason(UPDATED_RESIGNATION_REASON)
            .lastWorkingDay(UPDATED_LAST_WORKING_DAY);
        EmployeeResignationDTO employeeResignationDTO = employeeResignationMapper.toDto(updatedEmployeeResignation);

        restEmployeeResignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeResignationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeResignationDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeResignation in the database
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findAll();
        assertThat(employeeResignationList).hasSize(databaseSizeBeforeUpdate);
        EmployeeResignation testEmployeeResignation = employeeResignationList.get(employeeResignationList.size() - 1);
        assertThat(testEmployeeResignation.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployeeResignation.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmployeeResignation.getResignationDate()).isEqualTo(UPDATED_RESIGNATION_DATE);
        assertThat(testEmployeeResignation.getApprovalStatus()).isEqualTo(UPDATED_APPROVAL_STATUS);
        assertThat(testEmployeeResignation.getApprovalComment()).isEqualTo(UPDATED_APPROVAL_COMMENT);
        assertThat(testEmployeeResignation.getIsSalaryHold()).isEqualTo(UPDATED_IS_SALARY_HOLD);
        assertThat(testEmployeeResignation.getIsFestivalBonusHold()).isEqualTo(UPDATED_IS_FESTIVAL_BONUS_HOLD);
        assertThat(testEmployeeResignation.getResignationReason()).isEqualTo(UPDATED_RESIGNATION_REASON);
        assertThat(testEmployeeResignation.getLastWorkingDay()).isEqualTo(UPDATED_LAST_WORKING_DAY);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeResignation() throws Exception {
        int databaseSizeBeforeUpdate = employeeResignationRepository.findAll().size();
        employeeResignation.setId(count.incrementAndGet());

        // Create the EmployeeResignation
        EmployeeResignationDTO employeeResignationDTO = employeeResignationMapper.toDto(employeeResignation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeResignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeResignationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeResignationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeResignation in the database
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findAll();
        assertThat(employeeResignationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeResignation() throws Exception {
        int databaseSizeBeforeUpdate = employeeResignationRepository.findAll().size();
        employeeResignation.setId(count.incrementAndGet());

        // Create the EmployeeResignation
        EmployeeResignationDTO employeeResignationDTO = employeeResignationMapper.toDto(employeeResignation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeResignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeResignationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeResignation in the database
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findAll();
        assertThat(employeeResignationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeResignation() throws Exception {
        int databaseSizeBeforeUpdate = employeeResignationRepository.findAll().size();
        employeeResignation.setId(count.incrementAndGet());

        // Create the EmployeeResignation
        EmployeeResignationDTO employeeResignationDTO = employeeResignationMapper.toDto(employeeResignation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeResignationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeResignationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeResignation in the database
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findAll();
        assertThat(employeeResignationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeResignationWithPatch() throws Exception {
        // Initialize the database
        employeeResignationRepository.saveAndFlush(employeeResignation);

        int databaseSizeBeforeUpdate = employeeResignationRepository.findAll().size();

        // Update the employeeResignation using partial update
        EmployeeResignation partialUpdatedEmployeeResignation = new EmployeeResignation();
        partialUpdatedEmployeeResignation.setId(employeeResignation.getId());

        partialUpdatedEmployeeResignation
            .createdAt(UPDATED_CREATED_AT)
            .resignationDate(UPDATED_RESIGNATION_DATE)
            .approvalComment(UPDATED_APPROVAL_COMMENT)
            .isFestivalBonusHold(UPDATED_IS_FESTIVAL_BONUS_HOLD)
            .resignationReason(UPDATED_RESIGNATION_REASON);

        restEmployeeResignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeResignation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeResignation))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeResignation in the database
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findAll();
        assertThat(employeeResignationList).hasSize(databaseSizeBeforeUpdate);
        EmployeeResignation testEmployeeResignation = employeeResignationList.get(employeeResignationList.size() - 1);
        assertThat(testEmployeeResignation.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployeeResignation.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testEmployeeResignation.getResignationDate()).isEqualTo(UPDATED_RESIGNATION_DATE);
        assertThat(testEmployeeResignation.getApprovalStatus()).isEqualTo(DEFAULT_APPROVAL_STATUS);
        assertThat(testEmployeeResignation.getApprovalComment()).isEqualTo(UPDATED_APPROVAL_COMMENT);
        assertThat(testEmployeeResignation.getIsSalaryHold()).isEqualTo(DEFAULT_IS_SALARY_HOLD);
        assertThat(testEmployeeResignation.getIsFestivalBonusHold()).isEqualTo(UPDATED_IS_FESTIVAL_BONUS_HOLD);
        assertThat(testEmployeeResignation.getResignationReason()).isEqualTo(UPDATED_RESIGNATION_REASON);
        assertThat(testEmployeeResignation.getLastWorkingDay()).isEqualTo(DEFAULT_LAST_WORKING_DAY);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeResignationWithPatch() throws Exception {
        // Initialize the database
        employeeResignationRepository.saveAndFlush(employeeResignation);

        int databaseSizeBeforeUpdate = employeeResignationRepository.findAll().size();

        // Update the employeeResignation using partial update
        EmployeeResignation partialUpdatedEmployeeResignation = new EmployeeResignation();
        partialUpdatedEmployeeResignation.setId(employeeResignation.getId());

        partialUpdatedEmployeeResignation
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .resignationDate(UPDATED_RESIGNATION_DATE)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .approvalComment(UPDATED_APPROVAL_COMMENT)
            .isSalaryHold(UPDATED_IS_SALARY_HOLD)
            .isFestivalBonusHold(UPDATED_IS_FESTIVAL_BONUS_HOLD)
            .resignationReason(UPDATED_RESIGNATION_REASON)
            .lastWorkingDay(UPDATED_LAST_WORKING_DAY);

        restEmployeeResignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeResignation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeResignation))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeResignation in the database
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findAll();
        assertThat(employeeResignationList).hasSize(databaseSizeBeforeUpdate);
        EmployeeResignation testEmployeeResignation = employeeResignationList.get(employeeResignationList.size() - 1);
        assertThat(testEmployeeResignation.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployeeResignation.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmployeeResignation.getResignationDate()).isEqualTo(UPDATED_RESIGNATION_DATE);
        assertThat(testEmployeeResignation.getApprovalStatus()).isEqualTo(UPDATED_APPROVAL_STATUS);
        assertThat(testEmployeeResignation.getApprovalComment()).isEqualTo(UPDATED_APPROVAL_COMMENT);
        assertThat(testEmployeeResignation.getIsSalaryHold()).isEqualTo(UPDATED_IS_SALARY_HOLD);
        assertThat(testEmployeeResignation.getIsFestivalBonusHold()).isEqualTo(UPDATED_IS_FESTIVAL_BONUS_HOLD);
        assertThat(testEmployeeResignation.getResignationReason()).isEqualTo(UPDATED_RESIGNATION_REASON);
        assertThat(testEmployeeResignation.getLastWorkingDay()).isEqualTo(UPDATED_LAST_WORKING_DAY);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeResignation() throws Exception {
        int databaseSizeBeforeUpdate = employeeResignationRepository.findAll().size();
        employeeResignation.setId(count.incrementAndGet());

        // Create the EmployeeResignation
        EmployeeResignationDTO employeeResignationDTO = employeeResignationMapper.toDto(employeeResignation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeResignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeResignationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeResignationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeResignation in the database
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findAll();
        assertThat(employeeResignationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeResignation() throws Exception {
        int databaseSizeBeforeUpdate = employeeResignationRepository.findAll().size();
        employeeResignation.setId(count.incrementAndGet());

        // Create the EmployeeResignation
        EmployeeResignationDTO employeeResignationDTO = employeeResignationMapper.toDto(employeeResignation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeResignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeResignationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeResignation in the database
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findAll();
        assertThat(employeeResignationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeResignation() throws Exception {
        int databaseSizeBeforeUpdate = employeeResignationRepository.findAll().size();
        employeeResignation.setId(count.incrementAndGet());

        // Create the EmployeeResignation
        EmployeeResignationDTO employeeResignationDTO = employeeResignationMapper.toDto(employeeResignation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeResignationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeResignationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeResignation in the database
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findAll();
        assertThat(employeeResignationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeResignation() throws Exception {
        // Initialize the database
        employeeResignationRepository.saveAndFlush(employeeResignation);

        int databaseSizeBeforeDelete = employeeResignationRepository.findAll().size();

        // Delete the employeeResignation
        restEmployeeResignationMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeResignation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findAll();
        assertThat(employeeResignationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
