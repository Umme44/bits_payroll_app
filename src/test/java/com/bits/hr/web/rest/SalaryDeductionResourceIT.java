package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.DeductionType;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.SalaryDeduction;
import com.bits.hr.repository.SalaryDeductionRepository;
import com.bits.hr.service.dto.SalaryDeductionDTO;
import com.bits.hr.service.mapper.SalaryDeductionMapper;
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
 * Integration tests for the {@link SalaryDeductionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SalaryDeductionResourceIT {

    private static final Double DEFAULT_DEDUCTION_AMOUNT = 0D;
    private static final Double UPDATED_DEDUCTION_AMOUNT = 1D;

    private static final Integer DEFAULT_DEDUCTION_YEAR = 1990;
    private static final Integer UPDATED_DEDUCTION_YEAR = 1991;

    private static final Integer DEFAULT_DEDUCTION_MONTH = 1;
    private static final Integer UPDATED_DEDUCTION_MONTH = 2;

    private static final String ENTITY_API_URL = "/api/salary-deductions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SalaryDeductionRepository salaryDeductionRepository;

    @Autowired
    private SalaryDeductionMapper salaryDeductionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalaryDeductionMockMvc;

    private SalaryDeduction salaryDeduction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalaryDeduction createEntity(EntityManager em) {
        SalaryDeduction salaryDeduction = new SalaryDeduction()
            .deductionAmount(DEFAULT_DEDUCTION_AMOUNT)
            .deductionYear(DEFAULT_DEDUCTION_YEAR)
            .deductionMonth(DEFAULT_DEDUCTION_MONTH);
        // Add required entity
        DeductionType deductionType;
        if (TestUtil.findAll(em, DeductionType.class).isEmpty()) {
            deductionType = DeductionTypeResourceIT.createEntity(em);
            em.persist(deductionType);
            em.flush();
        } else {
            deductionType = TestUtil.findAll(em, DeductionType.class).get(0);
        }
        salaryDeduction.setDeductionType(deductionType);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        salaryDeduction.setEmployee(employee);
        return salaryDeduction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalaryDeduction createUpdatedEntity(EntityManager em) {
        SalaryDeduction salaryDeduction = new SalaryDeduction()
            .deductionAmount(UPDATED_DEDUCTION_AMOUNT)
            .deductionYear(UPDATED_DEDUCTION_YEAR)
            .deductionMonth(UPDATED_DEDUCTION_MONTH);
        // Add required entity
        DeductionType deductionType;
        if (TestUtil.findAll(em, DeductionType.class).isEmpty()) {
            deductionType = DeductionTypeResourceIT.createUpdatedEntity(em);
            em.persist(deductionType);
            em.flush();
        } else {
            deductionType = TestUtil.findAll(em, DeductionType.class).get(0);
        }
        salaryDeduction.setDeductionType(deductionType);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        salaryDeduction.setEmployee(employee);
        return salaryDeduction;
    }

    @BeforeEach
    public void initTest() {
        salaryDeduction = createEntity(em);
    }

    @Test
    @Transactional
    void createSalaryDeduction() throws Exception {
        int databaseSizeBeforeCreate = salaryDeductionRepository.findAll().size();
        // Create the SalaryDeduction
        SalaryDeductionDTO salaryDeductionDTO = salaryDeductionMapper.toDto(salaryDeduction);
        restSalaryDeductionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salaryDeductionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SalaryDeduction in the database
        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAll();
        assertThat(salaryDeductionList).hasSize(databaseSizeBeforeCreate + 1);
        SalaryDeduction testSalaryDeduction = salaryDeductionList.get(salaryDeductionList.size() - 1);
        assertThat(testSalaryDeduction.getDeductionAmount()).isEqualTo(DEFAULT_DEDUCTION_AMOUNT);
        assertThat(testSalaryDeduction.getDeductionYear()).isEqualTo(DEFAULT_DEDUCTION_YEAR);
        assertThat(testSalaryDeduction.getDeductionMonth()).isEqualTo(DEFAULT_DEDUCTION_MONTH);
    }

    @Test
    @Transactional
    void createSalaryDeductionWithExistingId() throws Exception {
        // Create the SalaryDeduction with an existing ID
        salaryDeduction.setId(1L);
        SalaryDeductionDTO salaryDeductionDTO = salaryDeductionMapper.toDto(salaryDeduction);

        int databaseSizeBeforeCreate = salaryDeductionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalaryDeductionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salaryDeductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryDeduction in the database
        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAll();
        assertThat(salaryDeductionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDeductionAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryDeductionRepository.findAll().size();
        // set the field null
        salaryDeduction.setDeductionAmount(null);

        // Create the SalaryDeduction, which fails.
        SalaryDeductionDTO salaryDeductionDTO = salaryDeductionMapper.toDto(salaryDeduction);

        restSalaryDeductionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salaryDeductionDTO))
            )
            .andExpect(status().isBadRequest());

        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAll();
        assertThat(salaryDeductionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeductionYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryDeductionRepository.findAll().size();
        // set the field null
        salaryDeduction.setDeductionYear(null);

        // Create the SalaryDeduction, which fails.
        SalaryDeductionDTO salaryDeductionDTO = salaryDeductionMapper.toDto(salaryDeduction);

        restSalaryDeductionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salaryDeductionDTO))
            )
            .andExpect(status().isBadRequest());

        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAll();
        assertThat(salaryDeductionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeductionMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryDeductionRepository.findAll().size();
        // set the field null
        salaryDeduction.setDeductionMonth(null);

        // Create the SalaryDeduction, which fails.
        SalaryDeductionDTO salaryDeductionDTO = salaryDeductionMapper.toDto(salaryDeduction);

        restSalaryDeductionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salaryDeductionDTO))
            )
            .andExpect(status().isBadRequest());

        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAll();
        assertThat(salaryDeductionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSalaryDeductions() throws Exception {
        // Initialize the database
        salaryDeductionRepository.saveAndFlush(salaryDeduction);

        // Get all the salaryDeductionList
        restSalaryDeductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salaryDeduction.getId().intValue())))
            .andExpect(jsonPath("$.[*].deductionAmount").value(hasItem(DEFAULT_DEDUCTION_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].deductionYear").value(hasItem(DEFAULT_DEDUCTION_YEAR)))
            .andExpect(jsonPath("$.[*].deductionMonth").value(hasItem(DEFAULT_DEDUCTION_MONTH)));
    }

    @Test
    @Transactional
    void getSalaryDeduction() throws Exception {
        // Initialize the database
        salaryDeductionRepository.saveAndFlush(salaryDeduction);

        // Get the salaryDeduction
        restSalaryDeductionMockMvc
            .perform(get(ENTITY_API_URL_ID, salaryDeduction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salaryDeduction.getId().intValue()))
            .andExpect(jsonPath("$.deductionAmount").value(DEFAULT_DEDUCTION_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.deductionYear").value(DEFAULT_DEDUCTION_YEAR))
            .andExpect(jsonPath("$.deductionMonth").value(DEFAULT_DEDUCTION_MONTH));
    }

    @Test
    @Transactional
    void getNonExistingSalaryDeduction() throws Exception {
        // Get the salaryDeduction
        restSalaryDeductionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSalaryDeduction() throws Exception {
        // Initialize the database
        salaryDeductionRepository.saveAndFlush(salaryDeduction);

        int databaseSizeBeforeUpdate = salaryDeductionRepository.findAll().size();

        // Update the salaryDeduction
        SalaryDeduction updatedSalaryDeduction = salaryDeductionRepository.findById(salaryDeduction.getId()).get();
        // Disconnect from session so that the updates on updatedSalaryDeduction are not directly saved in db
        em.detach(updatedSalaryDeduction);
        updatedSalaryDeduction
            .deductionAmount(UPDATED_DEDUCTION_AMOUNT)
            .deductionYear(UPDATED_DEDUCTION_YEAR)
            .deductionMonth(UPDATED_DEDUCTION_MONTH);
        SalaryDeductionDTO salaryDeductionDTO = salaryDeductionMapper.toDto(updatedSalaryDeduction);

        restSalaryDeductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salaryDeductionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryDeductionDTO))
            )
            .andExpect(status().isOk());

        // Validate the SalaryDeduction in the database
        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAll();
        assertThat(salaryDeductionList).hasSize(databaseSizeBeforeUpdate);
        SalaryDeduction testSalaryDeduction = salaryDeductionList.get(salaryDeductionList.size() - 1);
        assertThat(testSalaryDeduction.getDeductionAmount()).isEqualTo(UPDATED_DEDUCTION_AMOUNT);
        assertThat(testSalaryDeduction.getDeductionYear()).isEqualTo(UPDATED_DEDUCTION_YEAR);
        assertThat(testSalaryDeduction.getDeductionMonth()).isEqualTo(UPDATED_DEDUCTION_MONTH);
    }

    @Test
    @Transactional
    void putNonExistingSalaryDeduction() throws Exception {
        int databaseSizeBeforeUpdate = salaryDeductionRepository.findAll().size();
        salaryDeduction.setId(count.incrementAndGet());

        // Create the SalaryDeduction
        SalaryDeductionDTO salaryDeductionDTO = salaryDeductionMapper.toDto(salaryDeduction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaryDeductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salaryDeductionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryDeductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryDeduction in the database
        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAll();
        assertThat(salaryDeductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalaryDeduction() throws Exception {
        int databaseSizeBeforeUpdate = salaryDeductionRepository.findAll().size();
        salaryDeduction.setId(count.incrementAndGet());

        // Create the SalaryDeduction
        SalaryDeductionDTO salaryDeductionDTO = salaryDeductionMapper.toDto(salaryDeduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryDeductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryDeductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryDeduction in the database
        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAll();
        assertThat(salaryDeductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalaryDeduction() throws Exception {
        int databaseSizeBeforeUpdate = salaryDeductionRepository.findAll().size();
        salaryDeduction.setId(count.incrementAndGet());

        // Create the SalaryDeduction
        SalaryDeductionDTO salaryDeductionDTO = salaryDeductionMapper.toDto(salaryDeduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryDeductionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salaryDeductionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalaryDeduction in the database
        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAll();
        assertThat(salaryDeductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalaryDeductionWithPatch() throws Exception {
        // Initialize the database
        salaryDeductionRepository.saveAndFlush(salaryDeduction);

        int databaseSizeBeforeUpdate = salaryDeductionRepository.findAll().size();

        // Update the salaryDeduction using partial update
        SalaryDeduction partialUpdatedSalaryDeduction = new SalaryDeduction();
        partialUpdatedSalaryDeduction.setId(salaryDeduction.getId());

        partialUpdatedSalaryDeduction.deductionYear(UPDATED_DEDUCTION_YEAR);

        restSalaryDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalaryDeduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalaryDeduction))
            )
            .andExpect(status().isOk());

        // Validate the SalaryDeduction in the database
        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAll();
        assertThat(salaryDeductionList).hasSize(databaseSizeBeforeUpdate);
        SalaryDeduction testSalaryDeduction = salaryDeductionList.get(salaryDeductionList.size() - 1);
        assertThat(testSalaryDeduction.getDeductionAmount()).isEqualTo(DEFAULT_DEDUCTION_AMOUNT);
        assertThat(testSalaryDeduction.getDeductionYear()).isEqualTo(UPDATED_DEDUCTION_YEAR);
        assertThat(testSalaryDeduction.getDeductionMonth()).isEqualTo(DEFAULT_DEDUCTION_MONTH);
    }

    @Test
    @Transactional
    void fullUpdateSalaryDeductionWithPatch() throws Exception {
        // Initialize the database
        salaryDeductionRepository.saveAndFlush(salaryDeduction);

        int databaseSizeBeforeUpdate = salaryDeductionRepository.findAll().size();

        // Update the salaryDeduction using partial update
        SalaryDeduction partialUpdatedSalaryDeduction = new SalaryDeduction();
        partialUpdatedSalaryDeduction.setId(salaryDeduction.getId());

        partialUpdatedSalaryDeduction
            .deductionAmount(UPDATED_DEDUCTION_AMOUNT)
            .deductionYear(UPDATED_DEDUCTION_YEAR)
            .deductionMonth(UPDATED_DEDUCTION_MONTH);

        restSalaryDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalaryDeduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalaryDeduction))
            )
            .andExpect(status().isOk());

        // Validate the SalaryDeduction in the database
        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAll();
        assertThat(salaryDeductionList).hasSize(databaseSizeBeforeUpdate);
        SalaryDeduction testSalaryDeduction = salaryDeductionList.get(salaryDeductionList.size() - 1);
        assertThat(testSalaryDeduction.getDeductionAmount()).isEqualTo(UPDATED_DEDUCTION_AMOUNT);
        assertThat(testSalaryDeduction.getDeductionYear()).isEqualTo(UPDATED_DEDUCTION_YEAR);
        assertThat(testSalaryDeduction.getDeductionMonth()).isEqualTo(UPDATED_DEDUCTION_MONTH);
    }

    @Test
    @Transactional
    void patchNonExistingSalaryDeduction() throws Exception {
        int databaseSizeBeforeUpdate = salaryDeductionRepository.findAll().size();
        salaryDeduction.setId(count.incrementAndGet());

        // Create the SalaryDeduction
        SalaryDeductionDTO salaryDeductionDTO = salaryDeductionMapper.toDto(salaryDeduction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaryDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salaryDeductionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaryDeductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryDeduction in the database
        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAll();
        assertThat(salaryDeductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalaryDeduction() throws Exception {
        int databaseSizeBeforeUpdate = salaryDeductionRepository.findAll().size();
        salaryDeduction.setId(count.incrementAndGet());

        // Create the SalaryDeduction
        SalaryDeductionDTO salaryDeductionDTO = salaryDeductionMapper.toDto(salaryDeduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaryDeductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryDeduction in the database
        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAll();
        assertThat(salaryDeductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalaryDeduction() throws Exception {
        int databaseSizeBeforeUpdate = salaryDeductionRepository.findAll().size();
        salaryDeduction.setId(count.incrementAndGet());

        // Create the SalaryDeduction
        SalaryDeductionDTO salaryDeductionDTO = salaryDeductionMapper.toDto(salaryDeduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaryDeductionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalaryDeduction in the database
        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAll();
        assertThat(salaryDeductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalaryDeduction() throws Exception {
        // Initialize the database
        salaryDeductionRepository.saveAndFlush(salaryDeduction);

        int databaseSizeBeforeDelete = salaryDeductionRepository.findAll().size();

        // Delete the salaryDeduction
        restSalaryDeductionMockMvc
            .perform(delete(ENTITY_API_URL_ID, salaryDeduction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAll();
        assertThat(salaryDeductionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
