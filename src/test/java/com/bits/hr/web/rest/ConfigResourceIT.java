package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Config;
import com.bits.hr.repository.ConfigRepository;
import com.bits.hr.service.dto.ConfigDTO;
import com.bits.hr.service.mapper.ConfigMapper;
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
 * Integration tests for the {@link ConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConfigResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfigMockMvc;

    private Config config;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Config createEntity(EntityManager em) {
        Config config = new Config().key(DEFAULT_KEY).value(DEFAULT_VALUE);
        return config;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Config createUpdatedEntity(EntityManager em) {
        Config config = new Config().key(UPDATED_KEY).value(UPDATED_VALUE);
        return config;
    }

    @BeforeEach
    public void initTest() {
        config = createEntity(em);
    }

    @Test
    @Transactional
    void createConfig() throws Exception {
        int databaseSizeBeforeCreate = configRepository.findAll().size();
        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);
        restConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configDTO)))
            .andExpect(status().isCreated());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeCreate + 1);
        Config testConfig = configList.get(configList.size() - 1);
        assertThat(testConfig.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testConfig.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createConfigWithExistingId() throws Exception {
        // Create the Config with an existing ID
        config.setId(1L);
        ConfigDTO configDTO = configMapper.toDto(config);

        int databaseSizeBeforeCreate = configRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = configRepository.findAll().size();
        // set the field null
        config.setKey(null);

        // Create the Config, which fails.
        ConfigDTO configDTO = configMapper.toDto(config);

        restConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configDTO)))
            .andExpect(status().isBadRequest());

        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConfigs() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList
        restConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(config.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getConfig() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get the config
        restConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, config.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(config.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingConfig() throws Exception {
        // Get the config
        restConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConfig() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        int databaseSizeBeforeUpdate = configRepository.findAll().size();

        // Update the config
        Config updatedConfig = configRepository.findById(config.getId()).get();
        // Disconnect from session so that the updates on updatedConfig are not directly saved in db
        em.detach(updatedConfig);
        updatedConfig.key(UPDATED_KEY).value(UPDATED_VALUE);
        ConfigDTO configDTO = configMapper.toDto(updatedConfig);

        restConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configDTO))
            )
            .andExpect(status().isOk());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
        Config testConfig = configList.get(configList.size() - 1);
        assertThat(testConfig.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testConfig.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingConfig() throws Exception {
        int databaseSizeBeforeUpdate = configRepository.findAll().size();
        config.setId(count.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConfig() throws Exception {
        int databaseSizeBeforeUpdate = configRepository.findAll().size();
        config.setId(count.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConfig() throws Exception {
        int databaseSizeBeforeUpdate = configRepository.findAll().size();
        config.setId(count.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConfigWithPatch() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        int databaseSizeBeforeUpdate = configRepository.findAll().size();

        // Update the config using partial update
        Config partialUpdatedConfig = new Config();
        partialUpdatedConfig.setId(config.getId());

        partialUpdatedConfig.key(UPDATED_KEY).value(UPDATED_VALUE);

        restConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfig))
            )
            .andExpect(status().isOk());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
        Config testConfig = configList.get(configList.size() - 1);
        assertThat(testConfig.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testConfig.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateConfigWithPatch() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        int databaseSizeBeforeUpdate = configRepository.findAll().size();

        // Update the config using partial update
        Config partialUpdatedConfig = new Config();
        partialUpdatedConfig.setId(config.getId());

        partialUpdatedConfig.key(UPDATED_KEY).value(UPDATED_VALUE);

        restConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfig))
            )
            .andExpect(status().isOk());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
        Config testConfig = configList.get(configList.size() - 1);
        assertThat(testConfig.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testConfig.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingConfig() throws Exception {
        int databaseSizeBeforeUpdate = configRepository.findAll().size();
        config.setId(count.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, configDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConfig() throws Exception {
        int databaseSizeBeforeUpdate = configRepository.findAll().size();
        config.setId(count.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConfig() throws Exception {
        int databaseSizeBeforeUpdate = configRepository.findAll().size();
        config.setId(count.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(configDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConfig() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        int databaseSizeBeforeDelete = configRepository.findAll().size();

        // Delete the config
        restConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, config.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
