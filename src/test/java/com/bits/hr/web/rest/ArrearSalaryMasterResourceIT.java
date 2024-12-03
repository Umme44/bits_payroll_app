package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.ArrearSalaryMaster;
import com.bits.hr.repository.ArrearSalaryMasterRepository;
import com.bits.hr.service.dto.ArrearSalaryMasterDTO;
import com.bits.hr.service.mapper.ArrearSalaryMasterMapper;
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
 * Integration tests for the {@link ArrearSalaryMasterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArrearSalaryMasterResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_LOCKED = false;
    private static final Boolean UPDATED_IS_LOCKED = true;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/arrear-salary-masters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArrearSalaryMasterRepository arrearSalaryMasterRepository;

    @Autowired
    private ArrearSalaryMasterMapper arrearSalaryMasterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArrearSalaryMasterMockMvc;

    private ArrearSalaryMaster arrearSalaryMaster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArrearSalaryMaster createEntity(EntityManager em) {
        ArrearSalaryMaster arrearSalaryMaster = new ArrearSalaryMaster()
            .title(DEFAULT_TITLE)
            .isLocked(DEFAULT_IS_LOCKED)
            .isDeleted(DEFAULT_IS_DELETED);
        return arrearSalaryMaster;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArrearSalaryMaster createUpdatedEntity(EntityManager em) {
        ArrearSalaryMaster arrearSalaryMaster = new ArrearSalaryMaster()
            .title(UPDATED_TITLE)
            .isLocked(UPDATED_IS_LOCKED)
            .isDeleted(UPDATED_IS_DELETED);
        return arrearSalaryMaster;
    }

    @BeforeEach
    public void initTest() {
        arrearSalaryMaster = createEntity(em);
    }

    @Test
    @Transactional
    void createArrearSalaryMaster() throws Exception {
        int databaseSizeBeforeCreate = arrearSalaryMasterRepository.findAll().size();
        // Create the ArrearSalaryMaster
        ArrearSalaryMasterDTO arrearSalaryMasterDTO = arrearSalaryMasterMapper.toDto(arrearSalaryMaster);
        restArrearSalaryMasterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryMasterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ArrearSalaryMaster in the database
        List<ArrearSalaryMaster> arrearSalaryMasterList = arrearSalaryMasterRepository.findAll();
        assertThat(arrearSalaryMasterList).hasSize(databaseSizeBeforeCreate + 1);
        ArrearSalaryMaster testArrearSalaryMaster = arrearSalaryMasterList.get(arrearSalaryMasterList.size() - 1);
        assertThat(testArrearSalaryMaster.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testArrearSalaryMaster.getIsLocked()).isEqualTo(DEFAULT_IS_LOCKED);
        assertThat(testArrearSalaryMaster.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createArrearSalaryMasterWithExistingId() throws Exception {
        // Create the ArrearSalaryMaster with an existing ID
        arrearSalaryMaster.setId(1L);
        ArrearSalaryMasterDTO arrearSalaryMasterDTO = arrearSalaryMasterMapper.toDto(arrearSalaryMaster);

        int databaseSizeBeforeCreate = arrearSalaryMasterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArrearSalaryMasterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearSalaryMaster in the database
        List<ArrearSalaryMaster> arrearSalaryMasterList = arrearSalaryMasterRepository.findAll();
        assertThat(arrearSalaryMasterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = arrearSalaryMasterRepository.findAll().size();
        // set the field null
        arrearSalaryMaster.setTitle(null);

        // Create the ArrearSalaryMaster, which fails.
        ArrearSalaryMasterDTO arrearSalaryMasterDTO = arrearSalaryMasterMapper.toDto(arrearSalaryMaster);

        restArrearSalaryMasterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryMasterDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArrearSalaryMaster> arrearSalaryMasterList = arrearSalaryMasterRepository.findAll();
        assertThat(arrearSalaryMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsLockedIsRequired() throws Exception {
        int databaseSizeBeforeTest = arrearSalaryMasterRepository.findAll().size();
        // set the field null
        arrearSalaryMaster.setIsLocked(null);

        // Create the ArrearSalaryMaster, which fails.
        ArrearSalaryMasterDTO arrearSalaryMasterDTO = arrearSalaryMasterMapper.toDto(arrearSalaryMaster);

        restArrearSalaryMasterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryMasterDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArrearSalaryMaster> arrearSalaryMasterList = arrearSalaryMasterRepository.findAll();
        assertThat(arrearSalaryMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = arrearSalaryMasterRepository.findAll().size();
        // set the field null
        arrearSalaryMaster.setIsDeleted(null);

        // Create the ArrearSalaryMaster, which fails.
        ArrearSalaryMasterDTO arrearSalaryMasterDTO = arrearSalaryMasterMapper.toDto(arrearSalaryMaster);

        restArrearSalaryMasterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryMasterDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArrearSalaryMaster> arrearSalaryMasterList = arrearSalaryMasterRepository.findAll();
        assertThat(arrearSalaryMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArrearSalaryMasters() throws Exception {
        // Initialize the database
        arrearSalaryMasterRepository.saveAndFlush(arrearSalaryMaster);

        // Get all the arrearSalaryMasterList
        restArrearSalaryMasterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arrearSalaryMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].isLocked").value(hasItem(DEFAULT_IS_LOCKED.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getArrearSalaryMaster() throws Exception {
        // Initialize the database
        arrearSalaryMasterRepository.saveAndFlush(arrearSalaryMaster);

        // Get the arrearSalaryMaster
        restArrearSalaryMasterMockMvc
            .perform(get(ENTITY_API_URL_ID, arrearSalaryMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(arrearSalaryMaster.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.isLocked").value(DEFAULT_IS_LOCKED.booleanValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingArrearSalaryMaster() throws Exception {
        // Get the arrearSalaryMaster
        restArrearSalaryMasterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArrearSalaryMaster() throws Exception {
        // Initialize the database
        arrearSalaryMasterRepository.saveAndFlush(arrearSalaryMaster);

        int databaseSizeBeforeUpdate = arrearSalaryMasterRepository.findAll().size();

        // Update the arrearSalaryMaster
        ArrearSalaryMaster updatedArrearSalaryMaster = arrearSalaryMasterRepository.findById(arrearSalaryMaster.getId()).get();
        // Disconnect from session so that the updates on updatedArrearSalaryMaster are not directly saved in db
        em.detach(updatedArrearSalaryMaster);
        updatedArrearSalaryMaster.title(UPDATED_TITLE).isLocked(UPDATED_IS_LOCKED).isDeleted(UPDATED_IS_DELETED);
        ArrearSalaryMasterDTO arrearSalaryMasterDTO = arrearSalaryMasterMapper.toDto(updatedArrearSalaryMaster);

        restArrearSalaryMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arrearSalaryMasterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryMasterDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArrearSalaryMaster in the database
        List<ArrearSalaryMaster> arrearSalaryMasterList = arrearSalaryMasterRepository.findAll();
        assertThat(arrearSalaryMasterList).hasSize(databaseSizeBeforeUpdate);
        ArrearSalaryMaster testArrearSalaryMaster = arrearSalaryMasterList.get(arrearSalaryMasterList.size() - 1);
        assertThat(testArrearSalaryMaster.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArrearSalaryMaster.getIsLocked()).isEqualTo(UPDATED_IS_LOCKED);
        assertThat(testArrearSalaryMaster.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingArrearSalaryMaster() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryMasterRepository.findAll().size();
        arrearSalaryMaster.setId(count.incrementAndGet());

        // Create the ArrearSalaryMaster
        ArrearSalaryMasterDTO arrearSalaryMasterDTO = arrearSalaryMasterMapper.toDto(arrearSalaryMaster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArrearSalaryMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arrearSalaryMasterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearSalaryMaster in the database
        List<ArrearSalaryMaster> arrearSalaryMasterList = arrearSalaryMasterRepository.findAll();
        assertThat(arrearSalaryMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArrearSalaryMaster() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryMasterRepository.findAll().size();
        arrearSalaryMaster.setId(count.incrementAndGet());

        // Create the ArrearSalaryMaster
        ArrearSalaryMasterDTO arrearSalaryMasterDTO = arrearSalaryMasterMapper.toDto(arrearSalaryMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearSalaryMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearSalaryMaster in the database
        List<ArrearSalaryMaster> arrearSalaryMasterList = arrearSalaryMasterRepository.findAll();
        assertThat(arrearSalaryMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArrearSalaryMaster() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryMasterRepository.findAll().size();
        arrearSalaryMaster.setId(count.incrementAndGet());

        // Create the ArrearSalaryMaster
        ArrearSalaryMasterDTO arrearSalaryMasterDTO = arrearSalaryMasterMapper.toDto(arrearSalaryMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearSalaryMasterMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryMasterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArrearSalaryMaster in the database
        List<ArrearSalaryMaster> arrearSalaryMasterList = arrearSalaryMasterRepository.findAll();
        assertThat(arrearSalaryMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArrearSalaryMasterWithPatch() throws Exception {
        // Initialize the database
        arrearSalaryMasterRepository.saveAndFlush(arrearSalaryMaster);

        int databaseSizeBeforeUpdate = arrearSalaryMasterRepository.findAll().size();

        // Update the arrearSalaryMaster using partial update
        ArrearSalaryMaster partialUpdatedArrearSalaryMaster = new ArrearSalaryMaster();
        partialUpdatedArrearSalaryMaster.setId(arrearSalaryMaster.getId());

        partialUpdatedArrearSalaryMaster.title(UPDATED_TITLE).isLocked(UPDATED_IS_LOCKED);

        restArrearSalaryMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArrearSalaryMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArrearSalaryMaster))
            )
            .andExpect(status().isOk());

        // Validate the ArrearSalaryMaster in the database
        List<ArrearSalaryMaster> arrearSalaryMasterList = arrearSalaryMasterRepository.findAll();
        assertThat(arrearSalaryMasterList).hasSize(databaseSizeBeforeUpdate);
        ArrearSalaryMaster testArrearSalaryMaster = arrearSalaryMasterList.get(arrearSalaryMasterList.size() - 1);
        assertThat(testArrearSalaryMaster.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArrearSalaryMaster.getIsLocked()).isEqualTo(UPDATED_IS_LOCKED);
        assertThat(testArrearSalaryMaster.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateArrearSalaryMasterWithPatch() throws Exception {
        // Initialize the database
        arrearSalaryMasterRepository.saveAndFlush(arrearSalaryMaster);

        int databaseSizeBeforeUpdate = arrearSalaryMasterRepository.findAll().size();

        // Update the arrearSalaryMaster using partial update
        ArrearSalaryMaster partialUpdatedArrearSalaryMaster = new ArrearSalaryMaster();
        partialUpdatedArrearSalaryMaster.setId(arrearSalaryMaster.getId());

        partialUpdatedArrearSalaryMaster.title(UPDATED_TITLE).isLocked(UPDATED_IS_LOCKED).isDeleted(UPDATED_IS_DELETED);

        restArrearSalaryMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArrearSalaryMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArrearSalaryMaster))
            )
            .andExpect(status().isOk());

        // Validate the ArrearSalaryMaster in the database
        List<ArrearSalaryMaster> arrearSalaryMasterList = arrearSalaryMasterRepository.findAll();
        assertThat(arrearSalaryMasterList).hasSize(databaseSizeBeforeUpdate);
        ArrearSalaryMaster testArrearSalaryMaster = arrearSalaryMasterList.get(arrearSalaryMasterList.size() - 1);
        assertThat(testArrearSalaryMaster.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArrearSalaryMaster.getIsLocked()).isEqualTo(UPDATED_IS_LOCKED);
        assertThat(testArrearSalaryMaster.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingArrearSalaryMaster() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryMasterRepository.findAll().size();
        arrearSalaryMaster.setId(count.incrementAndGet());

        // Create the ArrearSalaryMaster
        ArrearSalaryMasterDTO arrearSalaryMasterDTO = arrearSalaryMasterMapper.toDto(arrearSalaryMaster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArrearSalaryMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, arrearSalaryMasterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearSalaryMaster in the database
        List<ArrearSalaryMaster> arrearSalaryMasterList = arrearSalaryMasterRepository.findAll();
        assertThat(arrearSalaryMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArrearSalaryMaster() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryMasterRepository.findAll().size();
        arrearSalaryMaster.setId(count.incrementAndGet());

        // Create the ArrearSalaryMaster
        ArrearSalaryMasterDTO arrearSalaryMasterDTO = arrearSalaryMasterMapper.toDto(arrearSalaryMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearSalaryMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearSalaryMaster in the database
        List<ArrearSalaryMaster> arrearSalaryMasterList = arrearSalaryMasterRepository.findAll();
        assertThat(arrearSalaryMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArrearSalaryMaster() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryMasterRepository.findAll().size();
        arrearSalaryMaster.setId(count.incrementAndGet());

        // Create the ArrearSalaryMaster
        ArrearSalaryMasterDTO arrearSalaryMasterDTO = arrearSalaryMasterMapper.toDto(arrearSalaryMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearSalaryMasterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryMasterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArrearSalaryMaster in the database
        List<ArrearSalaryMaster> arrearSalaryMasterList = arrearSalaryMasterRepository.findAll();
        assertThat(arrearSalaryMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArrearSalaryMaster() throws Exception {
        // Initialize the database
        arrearSalaryMasterRepository.saveAndFlush(arrearSalaryMaster);

        int databaseSizeBeforeDelete = arrearSalaryMasterRepository.findAll().size();

        // Delete the arrearSalaryMaster
        restArrearSalaryMasterMockMvc
            .perform(delete(ENTITY_API_URL_ID, arrearSalaryMaster.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArrearSalaryMaster> arrearSalaryMasterList = arrearSalaryMasterRepository.findAll();
        assertThat(arrearSalaryMasterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
