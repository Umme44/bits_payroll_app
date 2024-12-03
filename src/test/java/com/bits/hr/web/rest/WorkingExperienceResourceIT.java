package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.WorkingExperience;
import com.bits.hr.repository.WorkingExperienceRepository;
import com.bits.hr.service.dto.WorkingExperienceDTO;
import com.bits.hr.service.mapper.WorkingExperienceMapper;
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
 * Integration tests for the {@link WorkingExperienceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkingExperienceResourceIT {

    private static final String DEFAULT_LAST_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_LAST_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANIZATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZATION_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOJ_OF_LAST_ORGANIZATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOJ_OF_LAST_ORGANIZATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DOR_OF_LAST_ORGANIZATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOR_OF_LAST_ORGANIZATION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/working-experiences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkingExperienceRepository workingExperienceRepository;

    @Autowired
    private WorkingExperienceMapper workingExperienceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkingExperienceMockMvc;

    private WorkingExperience workingExperience;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkingExperience createEntity(EntityManager em) {
        WorkingExperience workingExperience = new WorkingExperience()
            .lastDesignation(DEFAULT_LAST_DESIGNATION)
            .organizationName(DEFAULT_ORGANIZATION_NAME)
            .dojOfLastOrganization(DEFAULT_DOJ_OF_LAST_ORGANIZATION)
            .dorOfLastOrganization(DEFAULT_DOR_OF_LAST_ORGANIZATION);
        return workingExperience;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkingExperience createUpdatedEntity(EntityManager em) {
        WorkingExperience workingExperience = new WorkingExperience()
            .lastDesignation(UPDATED_LAST_DESIGNATION)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .dojOfLastOrganization(UPDATED_DOJ_OF_LAST_ORGANIZATION)
            .dorOfLastOrganization(UPDATED_DOR_OF_LAST_ORGANIZATION);
        return workingExperience;
    }

    @BeforeEach
    public void initTest() {
        workingExperience = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkingExperience() throws Exception {
        int databaseSizeBeforeCreate = workingExperienceRepository.findAll().size();
        // Create the WorkingExperience
        WorkingExperienceDTO workingExperienceDTO = workingExperienceMapper.toDto(workingExperience);
        restWorkingExperienceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workingExperienceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkingExperience in the database
        List<WorkingExperience> workingExperienceList = workingExperienceRepository.findAll();
        assertThat(workingExperienceList).hasSize(databaseSizeBeforeCreate + 1);
        WorkingExperience testWorkingExperience = workingExperienceList.get(workingExperienceList.size() - 1);
        assertThat(testWorkingExperience.getLastDesignation()).isEqualTo(DEFAULT_LAST_DESIGNATION);
        assertThat(testWorkingExperience.getOrganizationName()).isEqualTo(DEFAULT_ORGANIZATION_NAME);
        assertThat(testWorkingExperience.getDojOfLastOrganization()).isEqualTo(DEFAULT_DOJ_OF_LAST_ORGANIZATION);
        assertThat(testWorkingExperience.getDorOfLastOrganization()).isEqualTo(DEFAULT_DOR_OF_LAST_ORGANIZATION);
    }

    @Test
    @Transactional
    void createWorkingExperienceWithExistingId() throws Exception {
        // Create the WorkingExperience with an existing ID
        workingExperience.setId(1L);
        WorkingExperienceDTO workingExperienceDTO = workingExperienceMapper.toDto(workingExperience);

        int databaseSizeBeforeCreate = workingExperienceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkingExperienceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workingExperienceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingExperience in the database
        List<WorkingExperience> workingExperienceList = workingExperienceRepository.findAll();
        assertThat(workingExperienceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWorkingExperiences() throws Exception {
        // Initialize the database
        workingExperienceRepository.saveAndFlush(workingExperience);

        // Get all the workingExperienceList
        restWorkingExperienceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workingExperience.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastDesignation").value(hasItem(DEFAULT_LAST_DESIGNATION)))
            .andExpect(jsonPath("$.[*].organizationName").value(hasItem(DEFAULT_ORGANIZATION_NAME)))
            .andExpect(jsonPath("$.[*].dojOfLastOrganization").value(hasItem(DEFAULT_DOJ_OF_LAST_ORGANIZATION.toString())))
            .andExpect(jsonPath("$.[*].dorOfLastOrganization").value(hasItem(DEFAULT_DOR_OF_LAST_ORGANIZATION.toString())));
    }

    @Test
    @Transactional
    void getWorkingExperience() throws Exception {
        // Initialize the database
        workingExperienceRepository.saveAndFlush(workingExperience);

        // Get the workingExperience
        restWorkingExperienceMockMvc
            .perform(get(ENTITY_API_URL_ID, workingExperience.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workingExperience.getId().intValue()))
            .andExpect(jsonPath("$.lastDesignation").value(DEFAULT_LAST_DESIGNATION))
            .andExpect(jsonPath("$.organizationName").value(DEFAULT_ORGANIZATION_NAME))
            .andExpect(jsonPath("$.dojOfLastOrganization").value(DEFAULT_DOJ_OF_LAST_ORGANIZATION.toString()))
            .andExpect(jsonPath("$.dorOfLastOrganization").value(DEFAULT_DOR_OF_LAST_ORGANIZATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWorkingExperience() throws Exception {
        // Get the workingExperience
        restWorkingExperienceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkingExperience() throws Exception {
        // Initialize the database
        workingExperienceRepository.saveAndFlush(workingExperience);

        int databaseSizeBeforeUpdate = workingExperienceRepository.findAll().size();

        // Update the workingExperience
        WorkingExperience updatedWorkingExperience = workingExperienceRepository.findById(workingExperience.getId()).get();
        // Disconnect from session so that the updates on updatedWorkingExperience are not directly saved in db
        em.detach(updatedWorkingExperience);
        updatedWorkingExperience
            .lastDesignation(UPDATED_LAST_DESIGNATION)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .dojOfLastOrganization(UPDATED_DOJ_OF_LAST_ORGANIZATION)
            .dorOfLastOrganization(UPDATED_DOR_OF_LAST_ORGANIZATION);
        WorkingExperienceDTO workingExperienceDTO = workingExperienceMapper.toDto(updatedWorkingExperience);

        restWorkingExperienceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workingExperienceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workingExperienceDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkingExperience in the database
        List<WorkingExperience> workingExperienceList = workingExperienceRepository.findAll();
        assertThat(workingExperienceList).hasSize(databaseSizeBeforeUpdate);
        WorkingExperience testWorkingExperience = workingExperienceList.get(workingExperienceList.size() - 1);
        assertThat(testWorkingExperience.getLastDesignation()).isEqualTo(UPDATED_LAST_DESIGNATION);
        assertThat(testWorkingExperience.getOrganizationName()).isEqualTo(UPDATED_ORGANIZATION_NAME);
        assertThat(testWorkingExperience.getDojOfLastOrganization()).isEqualTo(UPDATED_DOJ_OF_LAST_ORGANIZATION);
        assertThat(testWorkingExperience.getDorOfLastOrganization()).isEqualTo(UPDATED_DOR_OF_LAST_ORGANIZATION);
    }

    @Test
    @Transactional
    void putNonExistingWorkingExperience() throws Exception {
        int databaseSizeBeforeUpdate = workingExperienceRepository.findAll().size();
        workingExperience.setId(count.incrementAndGet());

        // Create the WorkingExperience
        WorkingExperienceDTO workingExperienceDTO = workingExperienceMapper.toDto(workingExperience);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkingExperienceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workingExperienceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workingExperienceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingExperience in the database
        List<WorkingExperience> workingExperienceList = workingExperienceRepository.findAll();
        assertThat(workingExperienceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkingExperience() throws Exception {
        int databaseSizeBeforeUpdate = workingExperienceRepository.findAll().size();
        workingExperience.setId(count.incrementAndGet());

        // Create the WorkingExperience
        WorkingExperienceDTO workingExperienceDTO = workingExperienceMapper.toDto(workingExperience);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingExperienceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workingExperienceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingExperience in the database
        List<WorkingExperience> workingExperienceList = workingExperienceRepository.findAll();
        assertThat(workingExperienceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkingExperience() throws Exception {
        int databaseSizeBeforeUpdate = workingExperienceRepository.findAll().size();
        workingExperience.setId(count.incrementAndGet());

        // Create the WorkingExperience
        WorkingExperienceDTO workingExperienceDTO = workingExperienceMapper.toDto(workingExperience);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingExperienceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workingExperienceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkingExperience in the database
        List<WorkingExperience> workingExperienceList = workingExperienceRepository.findAll();
        assertThat(workingExperienceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkingExperienceWithPatch() throws Exception {
        // Initialize the database
        workingExperienceRepository.saveAndFlush(workingExperience);

        int databaseSizeBeforeUpdate = workingExperienceRepository.findAll().size();

        // Update the workingExperience using partial update
        WorkingExperience partialUpdatedWorkingExperience = new WorkingExperience();
        partialUpdatedWorkingExperience.setId(workingExperience.getId());

        partialUpdatedWorkingExperience
            .dojOfLastOrganization(UPDATED_DOJ_OF_LAST_ORGANIZATION)
            .dorOfLastOrganization(UPDATED_DOR_OF_LAST_ORGANIZATION);

        restWorkingExperienceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkingExperience.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkingExperience))
            )
            .andExpect(status().isOk());

        // Validate the WorkingExperience in the database
        List<WorkingExperience> workingExperienceList = workingExperienceRepository.findAll();
        assertThat(workingExperienceList).hasSize(databaseSizeBeforeUpdate);
        WorkingExperience testWorkingExperience = workingExperienceList.get(workingExperienceList.size() - 1);
        assertThat(testWorkingExperience.getLastDesignation()).isEqualTo(DEFAULT_LAST_DESIGNATION);
        assertThat(testWorkingExperience.getOrganizationName()).isEqualTo(DEFAULT_ORGANIZATION_NAME);
        assertThat(testWorkingExperience.getDojOfLastOrganization()).isEqualTo(UPDATED_DOJ_OF_LAST_ORGANIZATION);
        assertThat(testWorkingExperience.getDorOfLastOrganization()).isEqualTo(UPDATED_DOR_OF_LAST_ORGANIZATION);
    }

    @Test
    @Transactional
    void fullUpdateWorkingExperienceWithPatch() throws Exception {
        // Initialize the database
        workingExperienceRepository.saveAndFlush(workingExperience);

        int databaseSizeBeforeUpdate = workingExperienceRepository.findAll().size();

        // Update the workingExperience using partial update
        WorkingExperience partialUpdatedWorkingExperience = new WorkingExperience();
        partialUpdatedWorkingExperience.setId(workingExperience.getId());

        partialUpdatedWorkingExperience
            .lastDesignation(UPDATED_LAST_DESIGNATION)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .dojOfLastOrganization(UPDATED_DOJ_OF_LAST_ORGANIZATION)
            .dorOfLastOrganization(UPDATED_DOR_OF_LAST_ORGANIZATION);

        restWorkingExperienceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkingExperience.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkingExperience))
            )
            .andExpect(status().isOk());

        // Validate the WorkingExperience in the database
        List<WorkingExperience> workingExperienceList = workingExperienceRepository.findAll();
        assertThat(workingExperienceList).hasSize(databaseSizeBeforeUpdate);
        WorkingExperience testWorkingExperience = workingExperienceList.get(workingExperienceList.size() - 1);
        assertThat(testWorkingExperience.getLastDesignation()).isEqualTo(UPDATED_LAST_DESIGNATION);
        assertThat(testWorkingExperience.getOrganizationName()).isEqualTo(UPDATED_ORGANIZATION_NAME);
        assertThat(testWorkingExperience.getDojOfLastOrganization()).isEqualTo(UPDATED_DOJ_OF_LAST_ORGANIZATION);
        assertThat(testWorkingExperience.getDorOfLastOrganization()).isEqualTo(UPDATED_DOR_OF_LAST_ORGANIZATION);
    }

    @Test
    @Transactional
    void patchNonExistingWorkingExperience() throws Exception {
        int databaseSizeBeforeUpdate = workingExperienceRepository.findAll().size();
        workingExperience.setId(count.incrementAndGet());

        // Create the WorkingExperience
        WorkingExperienceDTO workingExperienceDTO = workingExperienceMapper.toDto(workingExperience);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkingExperienceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workingExperienceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workingExperienceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingExperience in the database
        List<WorkingExperience> workingExperienceList = workingExperienceRepository.findAll();
        assertThat(workingExperienceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkingExperience() throws Exception {
        int databaseSizeBeforeUpdate = workingExperienceRepository.findAll().size();
        workingExperience.setId(count.incrementAndGet());

        // Create the WorkingExperience
        WorkingExperienceDTO workingExperienceDTO = workingExperienceMapper.toDto(workingExperience);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingExperienceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workingExperienceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingExperience in the database
        List<WorkingExperience> workingExperienceList = workingExperienceRepository.findAll();
        assertThat(workingExperienceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkingExperience() throws Exception {
        int databaseSizeBeforeUpdate = workingExperienceRepository.findAll().size();
        workingExperience.setId(count.incrementAndGet());

        // Create the WorkingExperience
        WorkingExperienceDTO workingExperienceDTO = workingExperienceMapper.toDto(workingExperience);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingExperienceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workingExperienceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkingExperience in the database
        List<WorkingExperience> workingExperienceList = workingExperienceRepository.findAll();
        assertThat(workingExperienceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkingExperience() throws Exception {
        // Initialize the database
        workingExperienceRepository.saveAndFlush(workingExperience);

        int databaseSizeBeforeDelete = workingExperienceRepository.findAll().size();

        // Delete the workingExperience
        restWorkingExperienceMockMvc
            .perform(delete(ENTITY_API_URL_ID, workingExperience.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkingExperience> workingExperienceList = workingExperienceRepository.findAll();
        assertThat(workingExperienceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
