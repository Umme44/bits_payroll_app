package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.FileTemplates;
import com.bits.hr.domain.enumeration.FileAccessPrevilage;
import com.bits.hr.domain.enumeration.FileTemplatesType;
import com.bits.hr.repository.FileTemplatesRepository;
import com.bits.hr.service.dto.FileTemplatesDTO;
import com.bits.hr.service.mapper.FileTemplatesMapper;
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
 * Integration tests for the {@link FileTemplatesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileTemplatesResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final FileTemplatesType DEFAULT_TYPE = FileTemplatesType.POLICIES;
    private static final FileTemplatesType UPDATED_TYPE = FileTemplatesType.TEMPLATES;

    private static final FileAccessPrevilage DEFAULT_ACCESS_PRIVILEGE = FileAccessPrevilage.GENERAL;
    private static final FileAccessPrevilage UPDATED_ACCESS_PRIVILEGE = FileAccessPrevilage.MANAGEMENT;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/file-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FileTemplatesRepository fileTemplatesRepository;

    @Autowired
    private FileTemplatesMapper fileTemplatesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileTemplatesMockMvc;

    private FileTemplates fileTemplates;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileTemplates createEntity(EntityManager em) {
        FileTemplates fileTemplates = new FileTemplates()
            .title(DEFAULT_TITLE)
            .filePath(DEFAULT_FILE_PATH)
            .type(DEFAULT_TYPE)
            .accessPrivilege(DEFAULT_ACCESS_PRIVILEGE)
            .isActive(DEFAULT_IS_ACTIVE);
        return fileTemplates;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileTemplates createUpdatedEntity(EntityManager em) {
        FileTemplates fileTemplates = new FileTemplates()
            .title(UPDATED_TITLE)
            .filePath(UPDATED_FILE_PATH)
            .type(UPDATED_TYPE)
            .accessPrivilege(UPDATED_ACCESS_PRIVILEGE)
            .isActive(UPDATED_IS_ACTIVE);
        return fileTemplates;
    }

    @BeforeEach
    public void initTest() {
        fileTemplates = createEntity(em);
    }

    @Test
    @Transactional
    void createFileTemplates() throws Exception {
        int databaseSizeBeforeCreate = fileTemplatesRepository.findAll().size();
        // Create the FileTemplates
        FileTemplatesDTO fileTemplatesDTO = fileTemplatesMapper.toDto(fileTemplates);
        restFileTemplatesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileTemplatesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FileTemplates in the database
        List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
        assertThat(fileTemplatesList).hasSize(databaseSizeBeforeCreate + 1);
        FileTemplates testFileTemplates = fileTemplatesList.get(fileTemplatesList.size() - 1);
        assertThat(testFileTemplates.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFileTemplates.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testFileTemplates.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFileTemplates.getAccessPrivilege()).isEqualTo(DEFAULT_ACCESS_PRIVILEGE);
        assertThat(testFileTemplates.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createFileTemplatesWithExistingId() throws Exception {
        // Create the FileTemplates with an existing ID
        fileTemplates.setId(1L);
        FileTemplatesDTO fileTemplatesDTO = fileTemplatesMapper.toDto(fileTemplates);

        int databaseSizeBeforeCreate = fileTemplatesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileTemplatesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileTemplatesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileTemplates in the database
        List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
        assertThat(fileTemplatesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileTemplatesRepository.findAll().size();
        // set the field null
        fileTemplates.setTitle(null);

        // Create the FileTemplates, which fails.
        FileTemplatesDTO fileTemplatesDTO = fileTemplatesMapper.toDto(fileTemplates);

        restFileTemplatesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileTemplatesDTO))
            )
            .andExpect(status().isBadRequest());

        List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
        assertThat(fileTemplatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileTemplatesRepository.findAll().size();
        // set the field null
        fileTemplates.setIsActive(null);

        // Create the FileTemplates, which fails.
        FileTemplatesDTO fileTemplatesDTO = fileTemplatesMapper.toDto(fileTemplates);

        restFileTemplatesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileTemplatesDTO))
            )
            .andExpect(status().isBadRequest());

        List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
        assertThat(fileTemplatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFileTemplates() throws Exception {
        // Initialize the database
        fileTemplatesRepository.saveAndFlush(fileTemplates);

        // Get all the fileTemplatesList
        restFileTemplatesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileTemplates.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accessPrivilege").value(hasItem(DEFAULT_ACCESS_PRIVILEGE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getFileTemplates() throws Exception {
        // Initialize the database
        fileTemplatesRepository.saveAndFlush(fileTemplates);

        // Get the fileTemplates
        restFileTemplatesMockMvc
            .perform(get(ENTITY_API_URL_ID, fileTemplates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileTemplates.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.accessPrivilege").value(DEFAULT_ACCESS_PRIVILEGE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFileTemplates() throws Exception {
        // Get the fileTemplates
        restFileTemplatesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFileTemplates() throws Exception {
        // Initialize the database
        fileTemplatesRepository.saveAndFlush(fileTemplates);

        int databaseSizeBeforeUpdate = fileTemplatesRepository.findAll().size();

        // Update the fileTemplates
        FileTemplates updatedFileTemplates = fileTemplatesRepository.findById(fileTemplates.getId()).get();
        // Disconnect from session so that the updates on updatedFileTemplates are not directly saved in db
        em.detach(updatedFileTemplates);
        updatedFileTemplates
            .title(UPDATED_TITLE)
            .filePath(UPDATED_FILE_PATH)
            .type(UPDATED_TYPE)
            .accessPrivilege(UPDATED_ACCESS_PRIVILEGE)
            .isActive(UPDATED_IS_ACTIVE);
        FileTemplatesDTO fileTemplatesDTO = fileTemplatesMapper.toDto(updatedFileTemplates);

        restFileTemplatesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileTemplatesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileTemplatesDTO))
            )
            .andExpect(status().isOk());

        // Validate the FileTemplates in the database
        List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
        assertThat(fileTemplatesList).hasSize(databaseSizeBeforeUpdate);
        FileTemplates testFileTemplates = fileTemplatesList.get(fileTemplatesList.size() - 1);
        assertThat(testFileTemplates.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFileTemplates.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testFileTemplates.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFileTemplates.getAccessPrivilege()).isEqualTo(UPDATED_ACCESS_PRIVILEGE);
        assertThat(testFileTemplates.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingFileTemplates() throws Exception {
        int databaseSizeBeforeUpdate = fileTemplatesRepository.findAll().size();
        fileTemplates.setId(count.incrementAndGet());

        // Create the FileTemplates
        FileTemplatesDTO fileTemplatesDTO = fileTemplatesMapper.toDto(fileTemplates);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileTemplatesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileTemplatesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileTemplatesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileTemplates in the database
        List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
        assertThat(fileTemplatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFileTemplates() throws Exception {
        int databaseSizeBeforeUpdate = fileTemplatesRepository.findAll().size();
        fileTemplates.setId(count.incrementAndGet());

        // Create the FileTemplates
        FileTemplatesDTO fileTemplatesDTO = fileTemplatesMapper.toDto(fileTemplates);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileTemplatesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileTemplatesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileTemplates in the database
        List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
        assertThat(fileTemplatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFileTemplates() throws Exception {
        int databaseSizeBeforeUpdate = fileTemplatesRepository.findAll().size();
        fileTemplates.setId(count.incrementAndGet());

        // Create the FileTemplates
        FileTemplatesDTO fileTemplatesDTO = fileTemplatesMapper.toDto(fileTemplates);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileTemplatesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileTemplatesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileTemplates in the database
        List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
        assertThat(fileTemplatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileTemplatesWithPatch() throws Exception {
        // Initialize the database
        fileTemplatesRepository.saveAndFlush(fileTemplates);

        int databaseSizeBeforeUpdate = fileTemplatesRepository.findAll().size();

        // Update the fileTemplates using partial update
        FileTemplates partialUpdatedFileTemplates = new FileTemplates();
        partialUpdatedFileTemplates.setId(fileTemplates.getId());

        partialUpdatedFileTemplates.title(UPDATED_TITLE).type(UPDATED_TYPE).accessPrivilege(UPDATED_ACCESS_PRIVILEGE);

        restFileTemplatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileTemplates.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileTemplates))
            )
            .andExpect(status().isOk());

        // Validate the FileTemplates in the database
        List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
        assertThat(fileTemplatesList).hasSize(databaseSizeBeforeUpdate);
        FileTemplates testFileTemplates = fileTemplatesList.get(fileTemplatesList.size() - 1);
        assertThat(testFileTemplates.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFileTemplates.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testFileTemplates.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFileTemplates.getAccessPrivilege()).isEqualTo(UPDATED_ACCESS_PRIVILEGE);
        assertThat(testFileTemplates.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateFileTemplatesWithPatch() throws Exception {
        // Initialize the database
        fileTemplatesRepository.saveAndFlush(fileTemplates);

        int databaseSizeBeforeUpdate = fileTemplatesRepository.findAll().size();

        // Update the fileTemplates using partial update
        FileTemplates partialUpdatedFileTemplates = new FileTemplates();
        partialUpdatedFileTemplates.setId(fileTemplates.getId());

        partialUpdatedFileTemplates
            .title(UPDATED_TITLE)
            .filePath(UPDATED_FILE_PATH)
            .type(UPDATED_TYPE)
            .accessPrivilege(UPDATED_ACCESS_PRIVILEGE)
            .isActive(UPDATED_IS_ACTIVE);

        restFileTemplatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileTemplates.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileTemplates))
            )
            .andExpect(status().isOk());

        // Validate the FileTemplates in the database
        List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
        assertThat(fileTemplatesList).hasSize(databaseSizeBeforeUpdate);
        FileTemplates testFileTemplates = fileTemplatesList.get(fileTemplatesList.size() - 1);
        assertThat(testFileTemplates.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFileTemplates.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testFileTemplates.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFileTemplates.getAccessPrivilege()).isEqualTo(UPDATED_ACCESS_PRIVILEGE);
        assertThat(testFileTemplates.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingFileTemplates() throws Exception {
        int databaseSizeBeforeUpdate = fileTemplatesRepository.findAll().size();
        fileTemplates.setId(count.incrementAndGet());

        // Create the FileTemplates
        FileTemplatesDTO fileTemplatesDTO = fileTemplatesMapper.toDto(fileTemplates);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileTemplatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileTemplatesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileTemplatesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileTemplates in the database
        List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
        assertThat(fileTemplatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFileTemplates() throws Exception {
        int databaseSizeBeforeUpdate = fileTemplatesRepository.findAll().size();
        fileTemplates.setId(count.incrementAndGet());

        // Create the FileTemplates
        FileTemplatesDTO fileTemplatesDTO = fileTemplatesMapper.toDto(fileTemplates);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileTemplatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileTemplatesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileTemplates in the database
        List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
        assertThat(fileTemplatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFileTemplates() throws Exception {
        int databaseSizeBeforeUpdate = fileTemplatesRepository.findAll().size();
        fileTemplates.setId(count.incrementAndGet());

        // Create the FileTemplates
        FileTemplatesDTO fileTemplatesDTO = fileTemplatesMapper.toDto(fileTemplates);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileTemplatesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileTemplatesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileTemplates in the database
        List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
        assertThat(fileTemplatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFileTemplates() throws Exception {
        // Initialize the database
        fileTemplatesRepository.saveAndFlush(fileTemplates);

        int databaseSizeBeforeDelete = fileTemplatesRepository.findAll().size();

        // Delete the fileTemplates
        restFileTemplatesMockMvc
            .perform(delete(ENTITY_API_URL_ID, fileTemplates.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
        assertThat(fileTemplatesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
