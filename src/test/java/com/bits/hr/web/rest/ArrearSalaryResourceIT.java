package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.ArrearSalary;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.ArrearSalaryRepository;
import com.bits.hr.service.dto.ArrearSalaryDTO;
import com.bits.hr.service.mapper.ArrearSalaryMapper;
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
 * Integration tests for the {@link ArrearSalaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArrearSalaryResourceIT {

    private static final Month DEFAULT_MONTH = Month.JANUARY;
    private static final Month UPDATED_MONTH = Month.FEBRUARY;

    private static final Integer DEFAULT_YEAR = 1990;
    private static final Integer UPDATED_YEAR = 1991;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String DEFAULT_ARREAR_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ARREAR_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/arrear-salaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArrearSalaryRepository arrearSalaryRepository;

    @Autowired
    private ArrearSalaryMapper arrearSalaryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArrearSalaryMockMvc;

    private ArrearSalary arrearSalary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArrearSalary createEntity(EntityManager em) {
        ArrearSalary arrearSalary = new ArrearSalary()
            .month(DEFAULT_MONTH)
            .year(DEFAULT_YEAR)
            .amount(DEFAULT_AMOUNT)
            .arrearType(DEFAULT_ARREAR_TYPE);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        arrearSalary.setEmployee(employee);
        return arrearSalary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArrearSalary createUpdatedEntity(EntityManager em) {
        ArrearSalary arrearSalary = new ArrearSalary()
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .amount(UPDATED_AMOUNT)
            .arrearType(UPDATED_ARREAR_TYPE);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        arrearSalary.setEmployee(employee);
        return arrearSalary;
    }

    @BeforeEach
    public void initTest() {
        arrearSalary = createEntity(em);
    }

    @Test
    @Transactional
    void createArrearSalary() throws Exception {
        int databaseSizeBeforeCreate = arrearSalaryRepository.findAll().size();
        // Create the ArrearSalary
        ArrearSalaryDTO arrearSalaryDTO = arrearSalaryMapper.toDto(arrearSalary);
        restArrearSalaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearSalaryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ArrearSalary in the database
        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findAll();
        assertThat(arrearSalaryList).hasSize(databaseSizeBeforeCreate + 1);
        ArrearSalary testArrearSalary = arrearSalaryList.get(arrearSalaryList.size() - 1);
        assertThat(testArrearSalary.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testArrearSalary.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testArrearSalary.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testArrearSalary.getArrearType()).isEqualTo(DEFAULT_ARREAR_TYPE);
    }

    @Test
    @Transactional
    void createArrearSalaryWithExistingId() throws Exception {
        // Create the ArrearSalary with an existing ID
        arrearSalary.setId(1L);
        ArrearSalaryDTO arrearSalaryDTO = arrearSalaryMapper.toDto(arrearSalary);

        int databaseSizeBeforeCreate = arrearSalaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArrearSalaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearSalary in the database
        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findAll();
        assertThat(arrearSalaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = arrearSalaryRepository.findAll().size();
        // set the field null
        arrearSalary.setMonth(null);

        // Create the ArrearSalary, which fails.
        ArrearSalaryDTO arrearSalaryDTO = arrearSalaryMapper.toDto(arrearSalary);

        restArrearSalaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findAll();
        assertThat(arrearSalaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = arrearSalaryRepository.findAll().size();
        // set the field null
        arrearSalary.setYear(null);

        // Create the ArrearSalary, which fails.
        ArrearSalaryDTO arrearSalaryDTO = arrearSalaryMapper.toDto(arrearSalary);

        restArrearSalaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findAll();
        assertThat(arrearSalaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = arrearSalaryRepository.findAll().size();
        // set the field null
        arrearSalary.setAmount(null);

        // Create the ArrearSalary, which fails.
        ArrearSalaryDTO arrearSalaryDTO = arrearSalaryMapper.toDto(arrearSalary);

        restArrearSalaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findAll();
        assertThat(arrearSalaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArrearSalaries() throws Exception {
        // Initialize the database
        arrearSalaryRepository.saveAndFlush(arrearSalary);

        // Get all the arrearSalaryList
        restArrearSalaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arrearSalary.getId().intValue())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].arrearType").value(hasItem(DEFAULT_ARREAR_TYPE)));
    }

    @Test
    @Transactional
    void getArrearSalary() throws Exception {
        // Initialize the database
        arrearSalaryRepository.saveAndFlush(arrearSalary);

        // Get the arrearSalary
        restArrearSalaryMockMvc
            .perform(get(ENTITY_API_URL_ID, arrearSalary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(arrearSalary.getId().intValue()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.arrearType").value(DEFAULT_ARREAR_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingArrearSalary() throws Exception {
        // Get the arrearSalary
        restArrearSalaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArrearSalary() throws Exception {
        // Initialize the database
        arrearSalaryRepository.saveAndFlush(arrearSalary);

        int databaseSizeBeforeUpdate = arrearSalaryRepository.findAll().size();

        // Update the arrearSalary
        ArrearSalary updatedArrearSalary = arrearSalaryRepository.findById(arrearSalary.getId()).get();
        // Disconnect from session so that the updates on updatedArrearSalary are not directly saved in db
        em.detach(updatedArrearSalary);
        updatedArrearSalary.month(UPDATED_MONTH).year(UPDATED_YEAR).amount(UPDATED_AMOUNT).arrearType(UPDATED_ARREAR_TYPE);
        ArrearSalaryDTO arrearSalaryDTO = arrearSalaryMapper.toDto(updatedArrearSalary);

        restArrearSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arrearSalaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArrearSalary in the database
        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findAll();
        assertThat(arrearSalaryList).hasSize(databaseSizeBeforeUpdate);
        ArrearSalary testArrearSalary = arrearSalaryList.get(arrearSalaryList.size() - 1);
        assertThat(testArrearSalary.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testArrearSalary.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testArrearSalary.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testArrearSalary.getArrearType()).isEqualTo(UPDATED_ARREAR_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingArrearSalary() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryRepository.findAll().size();
        arrearSalary.setId(count.incrementAndGet());

        // Create the ArrearSalary
        ArrearSalaryDTO arrearSalaryDTO = arrearSalaryMapper.toDto(arrearSalary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArrearSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arrearSalaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearSalary in the database
        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findAll();
        assertThat(arrearSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArrearSalary() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryRepository.findAll().size();
        arrearSalary.setId(count.incrementAndGet());

        // Create the ArrearSalary
        ArrearSalaryDTO arrearSalaryDTO = arrearSalaryMapper.toDto(arrearSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearSalary in the database
        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findAll();
        assertThat(arrearSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArrearSalary() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryRepository.findAll().size();
        arrearSalary.setId(count.incrementAndGet());

        // Create the ArrearSalary
        ArrearSalaryDTO arrearSalaryDTO = arrearSalaryMapper.toDto(arrearSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearSalaryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearSalaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArrearSalary in the database
        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findAll();
        assertThat(arrearSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArrearSalaryWithPatch() throws Exception {
        // Initialize the database
        arrearSalaryRepository.saveAndFlush(arrearSalary);

        int databaseSizeBeforeUpdate = arrearSalaryRepository.findAll().size();

        // Update the arrearSalary using partial update
        ArrearSalary partialUpdatedArrearSalary = new ArrearSalary();
        partialUpdatedArrearSalary.setId(arrearSalary.getId());

        partialUpdatedArrearSalary.month(UPDATED_MONTH).year(UPDATED_YEAR);

        restArrearSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArrearSalary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArrearSalary))
            )
            .andExpect(status().isOk());

        // Validate the ArrearSalary in the database
        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findAll();
        assertThat(arrearSalaryList).hasSize(databaseSizeBeforeUpdate);
        ArrearSalary testArrearSalary = arrearSalaryList.get(arrearSalaryList.size() - 1);
        assertThat(testArrearSalary.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testArrearSalary.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testArrearSalary.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testArrearSalary.getArrearType()).isEqualTo(DEFAULT_ARREAR_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateArrearSalaryWithPatch() throws Exception {
        // Initialize the database
        arrearSalaryRepository.saveAndFlush(arrearSalary);

        int databaseSizeBeforeUpdate = arrearSalaryRepository.findAll().size();

        // Update the arrearSalary using partial update
        ArrearSalary partialUpdatedArrearSalary = new ArrearSalary();
        partialUpdatedArrearSalary.setId(arrearSalary.getId());

        partialUpdatedArrearSalary.month(UPDATED_MONTH).year(UPDATED_YEAR).amount(UPDATED_AMOUNT).arrearType(UPDATED_ARREAR_TYPE);

        restArrearSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArrearSalary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArrearSalary))
            )
            .andExpect(status().isOk());

        // Validate the ArrearSalary in the database
        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findAll();
        assertThat(arrearSalaryList).hasSize(databaseSizeBeforeUpdate);
        ArrearSalary testArrearSalary = arrearSalaryList.get(arrearSalaryList.size() - 1);
        assertThat(testArrearSalary.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testArrearSalary.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testArrearSalary.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testArrearSalary.getArrearType()).isEqualTo(UPDATED_ARREAR_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingArrearSalary() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryRepository.findAll().size();
        arrearSalary.setId(count.incrementAndGet());

        // Create the ArrearSalary
        ArrearSalaryDTO arrearSalaryDTO = arrearSalaryMapper.toDto(arrearSalary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArrearSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, arrearSalaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearSalary in the database
        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findAll();
        assertThat(arrearSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArrearSalary() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryRepository.findAll().size();
        arrearSalary.setId(count.incrementAndGet());

        // Create the ArrearSalary
        ArrearSalaryDTO arrearSalaryDTO = arrearSalaryMapper.toDto(arrearSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearSalary in the database
        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findAll();
        assertThat(arrearSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArrearSalary() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryRepository.findAll().size();
        arrearSalary.setId(count.incrementAndGet());

        // Create the ArrearSalary
        ArrearSalaryDTO arrearSalaryDTO = arrearSalaryMapper.toDto(arrearSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArrearSalary in the database
        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findAll();
        assertThat(arrearSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArrearSalary() throws Exception {
        // Initialize the database
        arrearSalaryRepository.saveAndFlush(arrearSalary);

        int databaseSizeBeforeDelete = arrearSalaryRepository.findAll().size();

        // Delete the arrearSalary
        restArrearSalaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, arrearSalary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findAll();
        assertThat(arrearSalaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
