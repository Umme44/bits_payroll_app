package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.AitConfig;
import com.bits.hr.repository.AitConfigRepository;
import com.bits.hr.service.dto.AitConfigDTO;
import com.bits.hr.service.mapper.AitConfigMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link AitConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AitConfigResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TAX_CONFIG = "AAAAAAAAAA";
    private static final String UPDATED_TAX_CONFIG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ait-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AitConfigRepository aitConfigRepository;

    @Autowired
    private AitConfigMapper aitConfigMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAitConfigMockMvc;

    private AitConfig aitConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AitConfig createEntity(EntityManager em) {
        AitConfig aitConfig = new AitConfig().startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE).taxConfig(DEFAULT_TAX_CONFIG);
        return aitConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AitConfig createUpdatedEntity(EntityManager em) {
        AitConfig aitConfig = new AitConfig().startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).taxConfig(UPDATED_TAX_CONFIG);
        return aitConfig;
    }

    @BeforeEach
    public void initTest() {
        aitConfig = createEntity(em);
    }

    @Test
    @Transactional
    void createAitConfig() throws Exception {
        int databaseSizeBeforeCreate = aitConfigRepository.findAll().size();
        // Create the AitConfig
        AitConfigDTO aitConfigDTO = aitConfigMapper.toDto(aitConfig);
        restAitConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aitConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the AitConfig in the database
        List<AitConfig> aitConfigList = aitConfigRepository.findAll();
        assertThat(aitConfigList).hasSize(databaseSizeBeforeCreate + 1);
        AitConfig testAitConfig = aitConfigList.get(aitConfigList.size() - 1);
        assertThat(testAitConfig.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAitConfig.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAitConfig.getTaxConfig()).isEqualTo(DEFAULT_TAX_CONFIG);
    }

    @Test
    @Transactional
    void createAitConfigWithExistingId() throws Exception {
        // Create the AitConfig with an existing ID
        aitConfig.setId(1L);
        AitConfigDTO aitConfigDTO = aitConfigMapper.toDto(aitConfig);

        int databaseSizeBeforeCreate = aitConfigRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAitConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aitConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AitConfig in the database
        List<AitConfig> aitConfigList = aitConfigRepository.findAll();
        assertThat(aitConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = aitConfigRepository.findAll().size();
        // set the field null
        aitConfig.setStartDate(null);

        // Create the AitConfig, which fails.
        AitConfigDTO aitConfigDTO = aitConfigMapper.toDto(aitConfig);

        restAitConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aitConfigDTO)))
            .andExpect(status().isBadRequest());

        List<AitConfig> aitConfigList = aitConfigRepository.findAll();
        assertThat(aitConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = aitConfigRepository.findAll().size();
        // set the field null
        aitConfig.setEndDate(null);

        // Create the AitConfig, which fails.
        AitConfigDTO aitConfigDTO = aitConfigMapper.toDto(aitConfig);

        restAitConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aitConfigDTO)))
            .andExpect(status().isBadRequest());

        List<AitConfig> aitConfigList = aitConfigRepository.findAll();
        assertThat(aitConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAitConfigs() throws Exception {
        // Initialize the database
        aitConfigRepository.saveAndFlush(aitConfig);

        // Get all the aitConfigList
        restAitConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aitConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].taxConfig").value(hasItem(DEFAULT_TAX_CONFIG.toString())));
    }

    @Test
    @Transactional
    void getAitConfig() throws Exception {
        // Initialize the database
        aitConfigRepository.saveAndFlush(aitConfig);

        // Get the aitConfig
        restAitConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, aitConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aitConfig.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.taxConfig").value(DEFAULT_TAX_CONFIG.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAitConfig() throws Exception {
        // Get the aitConfig
        restAitConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAitConfig() throws Exception {
        // Initialize the database
        aitConfigRepository.saveAndFlush(aitConfig);

        int databaseSizeBeforeUpdate = aitConfigRepository.findAll().size();

        // Update the aitConfig
        AitConfig updatedAitConfig = aitConfigRepository.findById(aitConfig.getId()).get();
        // Disconnect from session so that the updates on updatedAitConfig are not directly saved in db
        em.detach(updatedAitConfig);
        updatedAitConfig.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).taxConfig(UPDATED_TAX_CONFIG);
        AitConfigDTO aitConfigDTO = aitConfigMapper.toDto(updatedAitConfig);

        restAitConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aitConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aitConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the AitConfig in the database
        List<AitConfig> aitConfigList = aitConfigRepository.findAll();
        assertThat(aitConfigList).hasSize(databaseSizeBeforeUpdate);
        AitConfig testAitConfig = aitConfigList.get(aitConfigList.size() - 1);
        assertThat(testAitConfig.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAitConfig.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testAitConfig.getTaxConfig()).isEqualTo(UPDATED_TAX_CONFIG);
    }

    @Test
    @Transactional
    void putNonExistingAitConfig() throws Exception {
        int databaseSizeBeforeUpdate = aitConfigRepository.findAll().size();
        aitConfig.setId(count.incrementAndGet());

        // Create the AitConfig
        AitConfigDTO aitConfigDTO = aitConfigMapper.toDto(aitConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAitConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aitConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aitConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AitConfig in the database
        List<AitConfig> aitConfigList = aitConfigRepository.findAll();
        assertThat(aitConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAitConfig() throws Exception {
        int databaseSizeBeforeUpdate = aitConfigRepository.findAll().size();
        aitConfig.setId(count.incrementAndGet());

        // Create the AitConfig
        AitConfigDTO aitConfigDTO = aitConfigMapper.toDto(aitConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAitConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aitConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AitConfig in the database
        List<AitConfig> aitConfigList = aitConfigRepository.findAll();
        assertThat(aitConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAitConfig() throws Exception {
        int databaseSizeBeforeUpdate = aitConfigRepository.findAll().size();
        aitConfig.setId(count.incrementAndGet());

        // Create the AitConfig
        AitConfigDTO aitConfigDTO = aitConfigMapper.toDto(aitConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAitConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aitConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AitConfig in the database
        List<AitConfig> aitConfigList = aitConfigRepository.findAll();
        assertThat(aitConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAitConfigWithPatch() throws Exception {
        // Initialize the database
        aitConfigRepository.saveAndFlush(aitConfig);

        int databaseSizeBeforeUpdate = aitConfigRepository.findAll().size();

        // Update the aitConfig using partial update
        AitConfig partialUpdatedAitConfig = new AitConfig();
        partialUpdatedAitConfig.setId(aitConfig.getId());

        partialUpdatedAitConfig.taxConfig(UPDATED_TAX_CONFIG);

        restAitConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAitConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAitConfig))
            )
            .andExpect(status().isOk());

        // Validate the AitConfig in the database
        List<AitConfig> aitConfigList = aitConfigRepository.findAll();
        assertThat(aitConfigList).hasSize(databaseSizeBeforeUpdate);
        AitConfig testAitConfig = aitConfigList.get(aitConfigList.size() - 1);
        assertThat(testAitConfig.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAitConfig.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAitConfig.getTaxConfig()).isEqualTo(UPDATED_TAX_CONFIG);
    }

    @Test
    @Transactional
    void fullUpdateAitConfigWithPatch() throws Exception {
        // Initialize the database
        aitConfigRepository.saveAndFlush(aitConfig);

        int databaseSizeBeforeUpdate = aitConfigRepository.findAll().size();

        // Update the aitConfig using partial update
        AitConfig partialUpdatedAitConfig = new AitConfig();
        partialUpdatedAitConfig.setId(aitConfig.getId());

        partialUpdatedAitConfig.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).taxConfig(UPDATED_TAX_CONFIG);

        restAitConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAitConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAitConfig))
            )
            .andExpect(status().isOk());

        // Validate the AitConfig in the database
        List<AitConfig> aitConfigList = aitConfigRepository.findAll();
        assertThat(aitConfigList).hasSize(databaseSizeBeforeUpdate);
        AitConfig testAitConfig = aitConfigList.get(aitConfigList.size() - 1);
        assertThat(testAitConfig.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAitConfig.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testAitConfig.getTaxConfig()).isEqualTo(UPDATED_TAX_CONFIG);
    }

    @Test
    @Transactional
    void patchNonExistingAitConfig() throws Exception {
        int databaseSizeBeforeUpdate = aitConfigRepository.findAll().size();
        aitConfig.setId(count.incrementAndGet());

        // Create the AitConfig
        AitConfigDTO aitConfigDTO = aitConfigMapper.toDto(aitConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAitConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aitConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aitConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AitConfig in the database
        List<AitConfig> aitConfigList = aitConfigRepository.findAll();
        assertThat(aitConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAitConfig() throws Exception {
        int databaseSizeBeforeUpdate = aitConfigRepository.findAll().size();
        aitConfig.setId(count.incrementAndGet());

        // Create the AitConfig
        AitConfigDTO aitConfigDTO = aitConfigMapper.toDto(aitConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAitConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aitConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AitConfig in the database
        List<AitConfig> aitConfigList = aitConfigRepository.findAll();
        assertThat(aitConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAitConfig() throws Exception {
        int databaseSizeBeforeUpdate = aitConfigRepository.findAll().size();
        aitConfig.setId(count.incrementAndGet());

        // Create the AitConfig
        AitConfigDTO aitConfigDTO = aitConfigMapper.toDto(aitConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAitConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aitConfigDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AitConfig in the database
        List<AitConfig> aitConfigList = aitConfigRepository.findAll();
        assertThat(aitConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAitConfig() throws Exception {
        // Initialize the database
        aitConfigRepository.saveAndFlush(aitConfig);

        int databaseSizeBeforeDelete = aitConfigRepository.findAll().size();

        // Delete the aitConfig
        restAitConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, aitConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AitConfig> aitConfigList = aitConfigRepository.findAll();
        assertThat(aitConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
