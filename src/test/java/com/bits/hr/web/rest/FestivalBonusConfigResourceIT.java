package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.FestivalBonusConfig;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.repository.FestivalBonusConfigRepository;
import com.bits.hr.service.dto.FestivalBonusConfigDTO;
import com.bits.hr.service.mapper.FestivalBonusConfigMapper;
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
 * Integration tests for the {@link FestivalBonusConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FestivalBonusConfigResourceIT {

    private static final EmployeeCategory DEFAULT_EMPLOYEE_CATEGORY = EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE;
    private static final EmployeeCategory UPDATED_EMPLOYEE_CATEGORY = EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE;

    private static final Double DEFAULT_PERCENTAGE_FROM_GROSS = 0D;
    private static final Double UPDATED_PERCENTAGE_FROM_GROSS = 1D;

    private static final String ENTITY_API_URL = "/api/festival-bonus-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FestivalBonusConfigRepository festivalBonusConfigRepository;

    @Autowired
    private FestivalBonusConfigMapper festivalBonusConfigMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFestivalBonusConfigMockMvc;

    private FestivalBonusConfig festivalBonusConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FestivalBonusConfig createEntity(EntityManager em) {
        FestivalBonusConfig festivalBonusConfig = new FestivalBonusConfig()
            .employeeCategory(DEFAULT_EMPLOYEE_CATEGORY)
            .percentageFromGross(DEFAULT_PERCENTAGE_FROM_GROSS);
        return festivalBonusConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FestivalBonusConfig createUpdatedEntity(EntityManager em) {
        FestivalBonusConfig festivalBonusConfig = new FestivalBonusConfig()
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .percentageFromGross(UPDATED_PERCENTAGE_FROM_GROSS);
        return festivalBonusConfig;
    }

    @BeforeEach
    public void initTest() {
        festivalBonusConfig = createEntity(em);
    }

    @Test
    @Transactional
    void createFestivalBonusConfig() throws Exception {
        int databaseSizeBeforeCreate = festivalBonusConfigRepository.findAll().size();
        // Create the FestivalBonusConfig
        FestivalBonusConfigDTO festivalBonusConfigDTO = festivalBonusConfigMapper.toDto(festivalBonusConfig);
        restFestivalBonusConfigMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusConfigDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FestivalBonusConfig in the database
        List<FestivalBonusConfig> festivalBonusConfigList = festivalBonusConfigRepository.findAll();
        assertThat(festivalBonusConfigList).hasSize(databaseSizeBeforeCreate + 1);
        FestivalBonusConfig testFestivalBonusConfig = festivalBonusConfigList.get(festivalBonusConfigList.size() - 1);
        assertThat(testFestivalBonusConfig.getEmployeeCategory()).isEqualTo(DEFAULT_EMPLOYEE_CATEGORY);
        assertThat(testFestivalBonusConfig.getPercentageFromGross()).isEqualTo(DEFAULT_PERCENTAGE_FROM_GROSS);
    }

    @Test
    @Transactional
    void createFestivalBonusConfigWithExistingId() throws Exception {
        // Create the FestivalBonusConfig with an existing ID
        festivalBonusConfig.setId(1L);
        FestivalBonusConfigDTO festivalBonusConfigDTO = festivalBonusConfigMapper.toDto(festivalBonusConfig);

        int databaseSizeBeforeCreate = festivalBonusConfigRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFestivalBonusConfigMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FestivalBonusConfig in the database
        List<FestivalBonusConfig> festivalBonusConfigList = festivalBonusConfigRepository.findAll();
        assertThat(festivalBonusConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmployeeCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = festivalBonusConfigRepository.findAll().size();
        // set the field null
        festivalBonusConfig.setEmployeeCategory(null);

        // Create the FestivalBonusConfig, which fails.
        FestivalBonusConfigDTO festivalBonusConfigDTO = festivalBonusConfigMapper.toDto(festivalBonusConfig);

        restFestivalBonusConfigMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusConfigDTO))
            )
            .andExpect(status().isBadRequest());

        List<FestivalBonusConfig> festivalBonusConfigList = festivalBonusConfigRepository.findAll();
        assertThat(festivalBonusConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPercentageFromGrossIsRequired() throws Exception {
        int databaseSizeBeforeTest = festivalBonusConfigRepository.findAll().size();
        // set the field null
        festivalBonusConfig.setPercentageFromGross(null);

        // Create the FestivalBonusConfig, which fails.
        FestivalBonusConfigDTO festivalBonusConfigDTO = festivalBonusConfigMapper.toDto(festivalBonusConfig);

        restFestivalBonusConfigMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusConfigDTO))
            )
            .andExpect(status().isBadRequest());

        List<FestivalBonusConfig> festivalBonusConfigList = festivalBonusConfigRepository.findAll();
        assertThat(festivalBonusConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFestivalBonusConfigs() throws Exception {
        // Initialize the database
        festivalBonusConfigRepository.saveAndFlush(festivalBonusConfig);

        // Get all the festivalBonusConfigList
        restFestivalBonusConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(festivalBonusConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeCategory").value(hasItem(DEFAULT_EMPLOYEE_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].percentageFromGross").value(hasItem(DEFAULT_PERCENTAGE_FROM_GROSS.doubleValue())));
    }

    @Test
    @Transactional
    void getFestivalBonusConfig() throws Exception {
        // Initialize the database
        festivalBonusConfigRepository.saveAndFlush(festivalBonusConfig);

        // Get the festivalBonusConfig
        restFestivalBonusConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, festivalBonusConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(festivalBonusConfig.getId().intValue()))
            .andExpect(jsonPath("$.employeeCategory").value(DEFAULT_EMPLOYEE_CATEGORY.toString()))
            .andExpect(jsonPath("$.percentageFromGross").value(DEFAULT_PERCENTAGE_FROM_GROSS.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingFestivalBonusConfig() throws Exception {
        // Get the festivalBonusConfig
        restFestivalBonusConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFestivalBonusConfig() throws Exception {
        // Initialize the database
        festivalBonusConfigRepository.saveAndFlush(festivalBonusConfig);

        int databaseSizeBeforeUpdate = festivalBonusConfigRepository.findAll().size();

        // Update the festivalBonusConfig
        FestivalBonusConfig updatedFestivalBonusConfig = festivalBonusConfigRepository.findById(festivalBonusConfig.getId()).get();
        // Disconnect from session so that the updates on updatedFestivalBonusConfig are not directly saved in db
        em.detach(updatedFestivalBonusConfig);
        updatedFestivalBonusConfig.employeeCategory(UPDATED_EMPLOYEE_CATEGORY).percentageFromGross(UPDATED_PERCENTAGE_FROM_GROSS);
        FestivalBonusConfigDTO festivalBonusConfigDTO = festivalBonusConfigMapper.toDto(updatedFestivalBonusConfig);

        restFestivalBonusConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, festivalBonusConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the FestivalBonusConfig in the database
        List<FestivalBonusConfig> festivalBonusConfigList = festivalBonusConfigRepository.findAll();
        assertThat(festivalBonusConfigList).hasSize(databaseSizeBeforeUpdate);
        FestivalBonusConfig testFestivalBonusConfig = festivalBonusConfigList.get(festivalBonusConfigList.size() - 1);
        assertThat(testFestivalBonusConfig.getEmployeeCategory()).isEqualTo(UPDATED_EMPLOYEE_CATEGORY);
        assertThat(testFestivalBonusConfig.getPercentageFromGross()).isEqualTo(UPDATED_PERCENTAGE_FROM_GROSS);
    }

    @Test
    @Transactional
    void putNonExistingFestivalBonusConfig() throws Exception {
        int databaseSizeBeforeUpdate = festivalBonusConfigRepository.findAll().size();
        festivalBonusConfig.setId(count.incrementAndGet());

        // Create the FestivalBonusConfig
        FestivalBonusConfigDTO festivalBonusConfigDTO = festivalBonusConfigMapper.toDto(festivalBonusConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFestivalBonusConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, festivalBonusConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FestivalBonusConfig in the database
        List<FestivalBonusConfig> festivalBonusConfigList = festivalBonusConfigRepository.findAll();
        assertThat(festivalBonusConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFestivalBonusConfig() throws Exception {
        int databaseSizeBeforeUpdate = festivalBonusConfigRepository.findAll().size();
        festivalBonusConfig.setId(count.incrementAndGet());

        // Create the FestivalBonusConfig
        FestivalBonusConfigDTO festivalBonusConfigDTO = festivalBonusConfigMapper.toDto(festivalBonusConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFestivalBonusConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FestivalBonusConfig in the database
        List<FestivalBonusConfig> festivalBonusConfigList = festivalBonusConfigRepository.findAll();
        assertThat(festivalBonusConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFestivalBonusConfig() throws Exception {
        int databaseSizeBeforeUpdate = festivalBonusConfigRepository.findAll().size();
        festivalBonusConfig.setId(count.incrementAndGet());

        // Create the FestivalBonusConfig
        FestivalBonusConfigDTO festivalBonusConfigDTO = festivalBonusConfigMapper.toDto(festivalBonusConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFestivalBonusConfigMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusConfigDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FestivalBonusConfig in the database
        List<FestivalBonusConfig> festivalBonusConfigList = festivalBonusConfigRepository.findAll();
        assertThat(festivalBonusConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFestivalBonusConfigWithPatch() throws Exception {
        // Initialize the database
        festivalBonusConfigRepository.saveAndFlush(festivalBonusConfig);

        int databaseSizeBeforeUpdate = festivalBonusConfigRepository.findAll().size();

        // Update the festivalBonusConfig using partial update
        FestivalBonusConfig partialUpdatedFestivalBonusConfig = new FestivalBonusConfig();
        partialUpdatedFestivalBonusConfig.setId(festivalBonusConfig.getId());

        partialUpdatedFestivalBonusConfig.employeeCategory(UPDATED_EMPLOYEE_CATEGORY).percentageFromGross(UPDATED_PERCENTAGE_FROM_GROSS);

        restFestivalBonusConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFestivalBonusConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFestivalBonusConfig))
            )
            .andExpect(status().isOk());

        // Validate the FestivalBonusConfig in the database
        List<FestivalBonusConfig> festivalBonusConfigList = festivalBonusConfigRepository.findAll();
        assertThat(festivalBonusConfigList).hasSize(databaseSizeBeforeUpdate);
        FestivalBonusConfig testFestivalBonusConfig = festivalBonusConfigList.get(festivalBonusConfigList.size() - 1);
        assertThat(testFestivalBonusConfig.getEmployeeCategory()).isEqualTo(UPDATED_EMPLOYEE_CATEGORY);
        assertThat(testFestivalBonusConfig.getPercentageFromGross()).isEqualTo(UPDATED_PERCENTAGE_FROM_GROSS);
    }

    @Test
    @Transactional
    void fullUpdateFestivalBonusConfigWithPatch() throws Exception {
        // Initialize the database
        festivalBonusConfigRepository.saveAndFlush(festivalBonusConfig);

        int databaseSizeBeforeUpdate = festivalBonusConfigRepository.findAll().size();

        // Update the festivalBonusConfig using partial update
        FestivalBonusConfig partialUpdatedFestivalBonusConfig = new FestivalBonusConfig();
        partialUpdatedFestivalBonusConfig.setId(festivalBonusConfig.getId());

        partialUpdatedFestivalBonusConfig.employeeCategory(UPDATED_EMPLOYEE_CATEGORY).percentageFromGross(UPDATED_PERCENTAGE_FROM_GROSS);

        restFestivalBonusConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFestivalBonusConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFestivalBonusConfig))
            )
            .andExpect(status().isOk());

        // Validate the FestivalBonusConfig in the database
        List<FestivalBonusConfig> festivalBonusConfigList = festivalBonusConfigRepository.findAll();
        assertThat(festivalBonusConfigList).hasSize(databaseSizeBeforeUpdate);
        FestivalBonusConfig testFestivalBonusConfig = festivalBonusConfigList.get(festivalBonusConfigList.size() - 1);
        assertThat(testFestivalBonusConfig.getEmployeeCategory()).isEqualTo(UPDATED_EMPLOYEE_CATEGORY);
        assertThat(testFestivalBonusConfig.getPercentageFromGross()).isEqualTo(UPDATED_PERCENTAGE_FROM_GROSS);
    }

    @Test
    @Transactional
    void patchNonExistingFestivalBonusConfig() throws Exception {
        int databaseSizeBeforeUpdate = festivalBonusConfigRepository.findAll().size();
        festivalBonusConfig.setId(count.incrementAndGet());

        // Create the FestivalBonusConfig
        FestivalBonusConfigDTO festivalBonusConfigDTO = festivalBonusConfigMapper.toDto(festivalBonusConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFestivalBonusConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, festivalBonusConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FestivalBonusConfig in the database
        List<FestivalBonusConfig> festivalBonusConfigList = festivalBonusConfigRepository.findAll();
        assertThat(festivalBonusConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFestivalBonusConfig() throws Exception {
        int databaseSizeBeforeUpdate = festivalBonusConfigRepository.findAll().size();
        festivalBonusConfig.setId(count.incrementAndGet());

        // Create the FestivalBonusConfig
        FestivalBonusConfigDTO festivalBonusConfigDTO = festivalBonusConfigMapper.toDto(festivalBonusConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFestivalBonusConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FestivalBonusConfig in the database
        List<FestivalBonusConfig> festivalBonusConfigList = festivalBonusConfigRepository.findAll();
        assertThat(festivalBonusConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFestivalBonusConfig() throws Exception {
        int databaseSizeBeforeUpdate = festivalBonusConfigRepository.findAll().size();
        festivalBonusConfig.setId(count.incrementAndGet());

        // Create the FestivalBonusConfig
        FestivalBonusConfigDTO festivalBonusConfigDTO = festivalBonusConfigMapper.toDto(festivalBonusConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFestivalBonusConfigMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusConfigDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FestivalBonusConfig in the database
        List<FestivalBonusConfig> festivalBonusConfigList = festivalBonusConfigRepository.findAll();
        assertThat(festivalBonusConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFestivalBonusConfig() throws Exception {
        // Initialize the database
        festivalBonusConfigRepository.saveAndFlush(festivalBonusConfig);

        int databaseSizeBeforeDelete = festivalBonusConfigRepository.findAll().size();

        // Delete the festivalBonusConfig
        restFestivalBonusConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, festivalBonusConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FestivalBonusConfig> festivalBonusConfigList = festivalBonusConfigRepository.findAll();
        assertThat(festivalBonusConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
