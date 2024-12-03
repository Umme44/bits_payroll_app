package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.DeductionType;
import com.bits.hr.repository.DeductionTypeRepository;
import com.bits.hr.service.dto.DeductionTypeDTO;
import com.bits.hr.service.mapper.DeductionTypeMapper;
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
 * Integration tests for the {@link DeductionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeductionTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/deduction-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeductionTypeRepository deductionTypeRepository;

    @Autowired
    private DeductionTypeMapper deductionTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeductionTypeMockMvc;

    private DeductionType deductionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeductionType createEntity(EntityManager em) {
        DeductionType deductionType = new DeductionType().name(DEFAULT_NAME);
        return deductionType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeductionType createUpdatedEntity(EntityManager em) {
        DeductionType deductionType = new DeductionType().name(UPDATED_NAME);
        return deductionType;
    }

    @BeforeEach
    public void initTest() {
        deductionType = createEntity(em);
    }

    @Test
    @Transactional
    void createDeductionType() throws Exception {
        int databaseSizeBeforeCreate = deductionTypeRepository.findAll().size();
        // Create the DeductionType
        DeductionTypeDTO deductionTypeDTO = deductionTypeMapper.toDto(deductionType);
        restDeductionTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deductionTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DeductionType in the database
        List<DeductionType> deductionTypeList = deductionTypeRepository.findAll();
        assertThat(deductionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DeductionType testDeductionType = deductionTypeList.get(deductionTypeList.size() - 1);
        assertThat(testDeductionType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createDeductionTypeWithExistingId() throws Exception {
        // Create the DeductionType with an existing ID
        deductionType.setId(1L);
        DeductionTypeDTO deductionTypeDTO = deductionTypeMapper.toDto(deductionType);

        int databaseSizeBeforeCreate = deductionTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeductionTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deductionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeductionType in the database
        List<DeductionType> deductionTypeList = deductionTypeRepository.findAll();
        assertThat(deductionTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = deductionTypeRepository.findAll().size();
        // set the field null
        deductionType.setName(null);

        // Create the DeductionType, which fails.
        DeductionTypeDTO deductionTypeDTO = deductionTypeMapper.toDto(deductionType);

        restDeductionTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deductionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<DeductionType> deductionTypeList = deductionTypeRepository.findAll();
        assertThat(deductionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeductionTypes() throws Exception {
        // Initialize the database
        deductionTypeRepository.saveAndFlush(deductionType);

        // Get all the deductionTypeList
        restDeductionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deductionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getDeductionType() throws Exception {
        // Initialize the database
        deductionTypeRepository.saveAndFlush(deductionType);

        // Get the deductionType
        restDeductionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, deductionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deductionType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingDeductionType() throws Exception {
        // Get the deductionType
        restDeductionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeductionType() throws Exception {
        // Initialize the database
        deductionTypeRepository.saveAndFlush(deductionType);

        int databaseSizeBeforeUpdate = deductionTypeRepository.findAll().size();

        // Update the deductionType
        DeductionType updatedDeductionType = deductionTypeRepository.findById(deductionType.getId()).get();
        // Disconnect from session so that the updates on updatedDeductionType are not directly saved in db
        em.detach(updatedDeductionType);
        updatedDeductionType.name(UPDATED_NAME);
        DeductionTypeDTO deductionTypeDTO = deductionTypeMapper.toDto(updatedDeductionType);

        restDeductionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deductionTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deductionTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeductionType in the database
        List<DeductionType> deductionTypeList = deductionTypeRepository.findAll();
        assertThat(deductionTypeList).hasSize(databaseSizeBeforeUpdate);
        DeductionType testDeductionType = deductionTypeList.get(deductionTypeList.size() - 1);
        assertThat(testDeductionType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDeductionType() throws Exception {
        int databaseSizeBeforeUpdate = deductionTypeRepository.findAll().size();
        deductionType.setId(count.incrementAndGet());

        // Create the DeductionType
        DeductionTypeDTO deductionTypeDTO = deductionTypeMapper.toDto(deductionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeductionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deductionTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deductionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeductionType in the database
        List<DeductionType> deductionTypeList = deductionTypeRepository.findAll();
        assertThat(deductionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeductionType() throws Exception {
        int databaseSizeBeforeUpdate = deductionTypeRepository.findAll().size();
        deductionType.setId(count.incrementAndGet());

        // Create the DeductionType
        DeductionTypeDTO deductionTypeDTO = deductionTypeMapper.toDto(deductionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deductionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeductionType in the database
        List<DeductionType> deductionTypeList = deductionTypeRepository.findAll();
        assertThat(deductionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeductionType() throws Exception {
        int databaseSizeBeforeUpdate = deductionTypeRepository.findAll().size();
        deductionType.setId(count.incrementAndGet());

        // Create the DeductionType
        DeductionTypeDTO deductionTypeDTO = deductionTypeMapper.toDto(deductionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductionTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deductionTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeductionType in the database
        List<DeductionType> deductionTypeList = deductionTypeRepository.findAll();
        assertThat(deductionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeductionTypeWithPatch() throws Exception {
        // Initialize the database
        deductionTypeRepository.saveAndFlush(deductionType);

        int databaseSizeBeforeUpdate = deductionTypeRepository.findAll().size();

        // Update the deductionType using partial update
        DeductionType partialUpdatedDeductionType = new DeductionType();
        partialUpdatedDeductionType.setId(deductionType.getId());

        partialUpdatedDeductionType.name(UPDATED_NAME);

        restDeductionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeductionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeductionType))
            )
            .andExpect(status().isOk());

        // Validate the DeductionType in the database
        List<DeductionType> deductionTypeList = deductionTypeRepository.findAll();
        assertThat(deductionTypeList).hasSize(databaseSizeBeforeUpdate);
        DeductionType testDeductionType = deductionTypeList.get(deductionTypeList.size() - 1);
        assertThat(testDeductionType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateDeductionTypeWithPatch() throws Exception {
        // Initialize the database
        deductionTypeRepository.saveAndFlush(deductionType);

        int databaseSizeBeforeUpdate = deductionTypeRepository.findAll().size();

        // Update the deductionType using partial update
        DeductionType partialUpdatedDeductionType = new DeductionType();
        partialUpdatedDeductionType.setId(deductionType.getId());

        partialUpdatedDeductionType.name(UPDATED_NAME);

        restDeductionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeductionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeductionType))
            )
            .andExpect(status().isOk());

        // Validate the DeductionType in the database
        List<DeductionType> deductionTypeList = deductionTypeRepository.findAll();
        assertThat(deductionTypeList).hasSize(databaseSizeBeforeUpdate);
        DeductionType testDeductionType = deductionTypeList.get(deductionTypeList.size() - 1);
        assertThat(testDeductionType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingDeductionType() throws Exception {
        int databaseSizeBeforeUpdate = deductionTypeRepository.findAll().size();
        deductionType.setId(count.incrementAndGet());

        // Create the DeductionType
        DeductionTypeDTO deductionTypeDTO = deductionTypeMapper.toDto(deductionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeductionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deductionTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deductionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeductionType in the database
        List<DeductionType> deductionTypeList = deductionTypeRepository.findAll();
        assertThat(deductionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeductionType() throws Exception {
        int databaseSizeBeforeUpdate = deductionTypeRepository.findAll().size();
        deductionType.setId(count.incrementAndGet());

        // Create the DeductionType
        DeductionTypeDTO deductionTypeDTO = deductionTypeMapper.toDto(deductionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deductionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeductionType in the database
        List<DeductionType> deductionTypeList = deductionTypeRepository.findAll();
        assertThat(deductionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeductionType() throws Exception {
        int databaseSizeBeforeUpdate = deductionTypeRepository.findAll().size();
        deductionType.setId(count.incrementAndGet());

        // Create the DeductionType
        DeductionTypeDTO deductionTypeDTO = deductionTypeMapper.toDto(deductionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deductionTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeductionType in the database
        List<DeductionType> deductionTypeList = deductionTypeRepository.findAll();
        assertThat(deductionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeductionType() throws Exception {
        // Initialize the database
        deductionTypeRepository.saveAndFlush(deductionType);

        int databaseSizeBeforeDelete = deductionTypeRepository.findAll().size();

        // Delete the deductionType
        restDeductionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, deductionType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeductionType> deductionTypeList = deductionTypeRepository.findAll();
        assertThat(deductionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
