package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.InsuranceConfiguration;
import com.bits.hr.repository.InsuranceConfigurationRepository;
import com.bits.hr.service.dto.InsuranceConfigurationDTO;
import com.bits.hr.service.mapper.InsuranceConfigurationMapper;
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
 * Integration tests for the {@link InsuranceConfigurationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsuranceConfigurationResourceIT {

    private static final Double DEFAULT_MAX_TOTAL_CLAIM_LIMIT_PER_YEAR = 1D;
    private static final Double UPDATED_MAX_TOTAL_CLAIM_LIMIT_PER_YEAR = 2D;

    private static final Double DEFAULT_MAX_ALLOWED_CHILD_AGE = 1D;
    private static final Double UPDATED_MAX_ALLOWED_CHILD_AGE = 2D;

    private static final String DEFAULT_INSURANCE_CLAIM_LINK = "AAAAAAAAAA";
    private static final String UPDATED_INSURANCE_CLAIM_LINK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/insurance-configurations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InsuranceConfigurationRepository insuranceConfigurationRepository;

    @Autowired
    private InsuranceConfigurationMapper insuranceConfigurationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceConfigurationMockMvc;

    private InsuranceConfiguration insuranceConfiguration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceConfiguration createEntity(EntityManager em) {
        InsuranceConfiguration insuranceConfiguration = new InsuranceConfiguration()
            .maxTotalClaimLimitPerYear(DEFAULT_MAX_TOTAL_CLAIM_LIMIT_PER_YEAR)
            .maxAllowedChildAge(DEFAULT_MAX_ALLOWED_CHILD_AGE)
            .insuranceClaimLink(DEFAULT_INSURANCE_CLAIM_LINK);
        return insuranceConfiguration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceConfiguration createUpdatedEntity(EntityManager em) {
        InsuranceConfiguration insuranceConfiguration = new InsuranceConfiguration()
            .maxTotalClaimLimitPerYear(UPDATED_MAX_TOTAL_CLAIM_LIMIT_PER_YEAR)
            .maxAllowedChildAge(UPDATED_MAX_ALLOWED_CHILD_AGE)
            .insuranceClaimLink(UPDATED_INSURANCE_CLAIM_LINK);
        return insuranceConfiguration;
    }

    @BeforeEach
    public void initTest() {
        insuranceConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    void createInsuranceConfiguration() throws Exception {
        int databaseSizeBeforeCreate = insuranceConfigurationRepository.findAll().size();
        // Create the InsuranceConfiguration
        InsuranceConfigurationDTO insuranceConfigurationDTO = insuranceConfigurationMapper.toDto(insuranceConfiguration);
        restInsuranceConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceConfigurationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InsuranceConfiguration in the database
        List<InsuranceConfiguration> insuranceConfigurationList = insuranceConfigurationRepository.findAll();
        assertThat(insuranceConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        InsuranceConfiguration testInsuranceConfiguration = insuranceConfigurationList.get(insuranceConfigurationList.size() - 1);
        assertThat(testInsuranceConfiguration.getMaxTotalClaimLimitPerYear()).isEqualTo(DEFAULT_MAX_TOTAL_CLAIM_LIMIT_PER_YEAR);
        assertThat(testInsuranceConfiguration.getMaxAllowedChildAge()).isEqualTo(DEFAULT_MAX_ALLOWED_CHILD_AGE);
        assertThat(testInsuranceConfiguration.getInsuranceClaimLink()).isEqualTo(DEFAULT_INSURANCE_CLAIM_LINK);
    }

    @Test
    @Transactional
    void createInsuranceConfigurationWithExistingId() throws Exception {
        // Create the InsuranceConfiguration with an existing ID
        insuranceConfiguration.setId(1L);
        InsuranceConfigurationDTO insuranceConfigurationDTO = insuranceConfigurationMapper.toDto(insuranceConfiguration);

        int databaseSizeBeforeCreate = insuranceConfigurationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceConfiguration in the database
        List<InsuranceConfiguration> insuranceConfigurationList = insuranceConfigurationRepository.findAll();
        assertThat(insuranceConfigurationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMaxTotalClaimLimitPerYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceConfigurationRepository.findAll().size();
        // set the field null
        insuranceConfiguration.setMaxTotalClaimLimitPerYear(null);

        // Create the InsuranceConfiguration, which fails.
        InsuranceConfigurationDTO insuranceConfigurationDTO = insuranceConfigurationMapper.toDto(insuranceConfiguration);

        restInsuranceConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        List<InsuranceConfiguration> insuranceConfigurationList = insuranceConfigurationRepository.findAll();
        assertThat(insuranceConfigurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInsuranceConfigurations() throws Exception {
        // Initialize the database
        insuranceConfigurationRepository.saveAndFlush(insuranceConfiguration);

        // Get all the insuranceConfigurationList
        restInsuranceConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxTotalClaimLimitPerYear").value(hasItem(DEFAULT_MAX_TOTAL_CLAIM_LIMIT_PER_YEAR.doubleValue())))
            .andExpect(jsonPath("$.[*].maxAllowedChildAge").value(hasItem(DEFAULT_MAX_ALLOWED_CHILD_AGE.doubleValue())))
            .andExpect(jsonPath("$.[*].insuranceClaimLink").value(hasItem(DEFAULT_INSURANCE_CLAIM_LINK)));
    }

    @Test
    @Transactional
    void getInsuranceConfiguration() throws Exception {
        // Initialize the database
        insuranceConfigurationRepository.saveAndFlush(insuranceConfiguration);

        // Get the insuranceConfiguration
        restInsuranceConfigurationMockMvc
            .perform(get(ENTITY_API_URL_ID, insuranceConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.maxTotalClaimLimitPerYear").value(DEFAULT_MAX_TOTAL_CLAIM_LIMIT_PER_YEAR.doubleValue()))
            .andExpect(jsonPath("$.maxAllowedChildAge").value(DEFAULT_MAX_ALLOWED_CHILD_AGE.doubleValue()))
            .andExpect(jsonPath("$.insuranceClaimLink").value(DEFAULT_INSURANCE_CLAIM_LINK));
    }

    @Test
    @Transactional
    void getNonExistingInsuranceConfiguration() throws Exception {
        // Get the insuranceConfiguration
        restInsuranceConfigurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsuranceConfiguration() throws Exception {
        // Initialize the database
        insuranceConfigurationRepository.saveAndFlush(insuranceConfiguration);

        int databaseSizeBeforeUpdate = insuranceConfigurationRepository.findAll().size();

        // Update the insuranceConfiguration
        InsuranceConfiguration updatedInsuranceConfiguration = insuranceConfigurationRepository
            .findById(insuranceConfiguration.getId())
            .get();
        // Disconnect from session so that the updates on updatedInsuranceConfiguration are not directly saved in db
        em.detach(updatedInsuranceConfiguration);
        updatedInsuranceConfiguration
            .maxTotalClaimLimitPerYear(UPDATED_MAX_TOTAL_CLAIM_LIMIT_PER_YEAR)
            .maxAllowedChildAge(UPDATED_MAX_ALLOWED_CHILD_AGE)
            .insuranceClaimLink(UPDATED_INSURANCE_CLAIM_LINK);
        InsuranceConfigurationDTO insuranceConfigurationDTO = insuranceConfigurationMapper.toDto(updatedInsuranceConfiguration);

        restInsuranceConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceConfigurationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceConfigurationDTO))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceConfiguration in the database
        List<InsuranceConfiguration> insuranceConfigurationList = insuranceConfigurationRepository.findAll();
        assertThat(insuranceConfigurationList).hasSize(databaseSizeBeforeUpdate);
        InsuranceConfiguration testInsuranceConfiguration = insuranceConfigurationList.get(insuranceConfigurationList.size() - 1);
        assertThat(testInsuranceConfiguration.getMaxTotalClaimLimitPerYear()).isEqualTo(UPDATED_MAX_TOTAL_CLAIM_LIMIT_PER_YEAR);
        assertThat(testInsuranceConfiguration.getMaxAllowedChildAge()).isEqualTo(UPDATED_MAX_ALLOWED_CHILD_AGE);
        assertThat(testInsuranceConfiguration.getInsuranceClaimLink()).isEqualTo(UPDATED_INSURANCE_CLAIM_LINK);
    }

    @Test
    @Transactional
    void putNonExistingInsuranceConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = insuranceConfigurationRepository.findAll().size();
        insuranceConfiguration.setId(count.incrementAndGet());

        // Create the InsuranceConfiguration
        InsuranceConfigurationDTO insuranceConfigurationDTO = insuranceConfigurationMapper.toDto(insuranceConfiguration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceConfigurationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceConfiguration in the database
        List<InsuranceConfiguration> insuranceConfigurationList = insuranceConfigurationRepository.findAll();
        assertThat(insuranceConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsuranceConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = insuranceConfigurationRepository.findAll().size();
        insuranceConfiguration.setId(count.incrementAndGet());

        // Create the InsuranceConfiguration
        InsuranceConfigurationDTO insuranceConfigurationDTO = insuranceConfigurationMapper.toDto(insuranceConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceConfiguration in the database
        List<InsuranceConfiguration> insuranceConfigurationList = insuranceConfigurationRepository.findAll();
        assertThat(insuranceConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsuranceConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = insuranceConfigurationRepository.findAll().size();
        insuranceConfiguration.setId(count.incrementAndGet());

        // Create the InsuranceConfiguration
        InsuranceConfigurationDTO insuranceConfigurationDTO = insuranceConfigurationMapper.toDto(insuranceConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceConfigurationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceConfiguration in the database
        List<InsuranceConfiguration> insuranceConfigurationList = insuranceConfigurationRepository.findAll();
        assertThat(insuranceConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsuranceConfigurationWithPatch() throws Exception {
        // Initialize the database
        insuranceConfigurationRepository.saveAndFlush(insuranceConfiguration);

        int databaseSizeBeforeUpdate = insuranceConfigurationRepository.findAll().size();

        // Update the insuranceConfiguration using partial update
        InsuranceConfiguration partialUpdatedInsuranceConfiguration = new InsuranceConfiguration();
        partialUpdatedInsuranceConfiguration.setId(insuranceConfiguration.getId());

        partialUpdatedInsuranceConfiguration
            .maxAllowedChildAge(UPDATED_MAX_ALLOWED_CHILD_AGE)
            .insuranceClaimLink(UPDATED_INSURANCE_CLAIM_LINK);

        restInsuranceConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInsuranceConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceConfiguration in the database
        List<InsuranceConfiguration> insuranceConfigurationList = insuranceConfigurationRepository.findAll();
        assertThat(insuranceConfigurationList).hasSize(databaseSizeBeforeUpdate);
        InsuranceConfiguration testInsuranceConfiguration = insuranceConfigurationList.get(insuranceConfigurationList.size() - 1);
        assertThat(testInsuranceConfiguration.getMaxTotalClaimLimitPerYear()).isEqualTo(DEFAULT_MAX_TOTAL_CLAIM_LIMIT_PER_YEAR);
        assertThat(testInsuranceConfiguration.getMaxAllowedChildAge()).isEqualTo(UPDATED_MAX_ALLOWED_CHILD_AGE);
        assertThat(testInsuranceConfiguration.getInsuranceClaimLink()).isEqualTo(UPDATED_INSURANCE_CLAIM_LINK);
    }

    @Test
    @Transactional
    void fullUpdateInsuranceConfigurationWithPatch() throws Exception {
        // Initialize the database
        insuranceConfigurationRepository.saveAndFlush(insuranceConfiguration);

        int databaseSizeBeforeUpdate = insuranceConfigurationRepository.findAll().size();

        // Update the insuranceConfiguration using partial update
        InsuranceConfiguration partialUpdatedInsuranceConfiguration = new InsuranceConfiguration();
        partialUpdatedInsuranceConfiguration.setId(insuranceConfiguration.getId());

        partialUpdatedInsuranceConfiguration
            .maxTotalClaimLimitPerYear(UPDATED_MAX_TOTAL_CLAIM_LIMIT_PER_YEAR)
            .maxAllowedChildAge(UPDATED_MAX_ALLOWED_CHILD_AGE)
            .insuranceClaimLink(UPDATED_INSURANCE_CLAIM_LINK);

        restInsuranceConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInsuranceConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceConfiguration in the database
        List<InsuranceConfiguration> insuranceConfigurationList = insuranceConfigurationRepository.findAll();
        assertThat(insuranceConfigurationList).hasSize(databaseSizeBeforeUpdate);
        InsuranceConfiguration testInsuranceConfiguration = insuranceConfigurationList.get(insuranceConfigurationList.size() - 1);
        assertThat(testInsuranceConfiguration.getMaxTotalClaimLimitPerYear()).isEqualTo(UPDATED_MAX_TOTAL_CLAIM_LIMIT_PER_YEAR);
        assertThat(testInsuranceConfiguration.getMaxAllowedChildAge()).isEqualTo(UPDATED_MAX_ALLOWED_CHILD_AGE);
        assertThat(testInsuranceConfiguration.getInsuranceClaimLink()).isEqualTo(UPDATED_INSURANCE_CLAIM_LINK);
    }

    @Test
    @Transactional
    void patchNonExistingInsuranceConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = insuranceConfigurationRepository.findAll().size();
        insuranceConfiguration.setId(count.incrementAndGet());

        // Create the InsuranceConfiguration
        InsuranceConfigurationDTO insuranceConfigurationDTO = insuranceConfigurationMapper.toDto(insuranceConfiguration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insuranceConfigurationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insuranceConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceConfiguration in the database
        List<InsuranceConfiguration> insuranceConfigurationList = insuranceConfigurationRepository.findAll();
        assertThat(insuranceConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsuranceConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = insuranceConfigurationRepository.findAll().size();
        insuranceConfiguration.setId(count.incrementAndGet());

        // Create the InsuranceConfiguration
        InsuranceConfigurationDTO insuranceConfigurationDTO = insuranceConfigurationMapper.toDto(insuranceConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insuranceConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceConfiguration in the database
        List<InsuranceConfiguration> insuranceConfigurationList = insuranceConfigurationRepository.findAll();
        assertThat(insuranceConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsuranceConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = insuranceConfigurationRepository.findAll().size();
        insuranceConfiguration.setId(count.incrementAndGet());

        // Create the InsuranceConfiguration
        InsuranceConfigurationDTO insuranceConfigurationDTO = insuranceConfigurationMapper.toDto(insuranceConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insuranceConfigurationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceConfiguration in the database
        List<InsuranceConfiguration> insuranceConfigurationList = insuranceConfigurationRepository.findAll();
        assertThat(insuranceConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsuranceConfiguration() throws Exception {
        // Initialize the database
        insuranceConfigurationRepository.saveAndFlush(insuranceConfiguration);

        int databaseSizeBeforeDelete = insuranceConfigurationRepository.findAll().size();

        // Delete the insuranceConfiguration
        restInsuranceConfigurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, insuranceConfiguration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InsuranceConfiguration> insuranceConfigurationList = insuranceConfigurationRepository.findAll();
        assertThat(insuranceConfigurationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
