package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.EmployeeStaticFile;
import com.bits.hr.repository.EmployeeStaticFileRepository;
import com.bits.hr.service.dto.EmployeeStaticFileDTO;
import com.bits.hr.service.mapper.EmployeeStaticFileMapper;
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
 * Integration tests for the {@link EmployeeStaticFileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeStaticFileResourceIT {

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employee-static-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeStaticFileRepository employeeStaticFileRepository;

    @Autowired
    private EmployeeStaticFileMapper employeeStaticFileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeStaticFileMockMvc;

    private EmployeeStaticFile employeeStaticFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeStaticFile createEntity(EntityManager em) {
        EmployeeStaticFile employeeStaticFile = new EmployeeStaticFile().filePath(DEFAULT_FILE_PATH);
        return employeeStaticFile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeStaticFile createUpdatedEntity(EntityManager em) {
        EmployeeStaticFile employeeStaticFile = new EmployeeStaticFile().filePath(UPDATED_FILE_PATH);
        return employeeStaticFile;
    }

    @BeforeEach
    public void initTest() {
        employeeStaticFile = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeeStaticFile() throws Exception {
        int databaseSizeBeforeCreate = employeeStaticFileRepository.findAll().size();
        // Create the EmployeeStaticFile
        EmployeeStaticFileDTO employeeStaticFileDTO = employeeStaticFileMapper.toDto(employeeStaticFile);
        restEmployeeStaticFileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeStaticFileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EmployeeStaticFile in the database
        List<EmployeeStaticFile> employeeStaticFileList = employeeStaticFileRepository.findAll();
        assertThat(employeeStaticFileList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeStaticFile testEmployeeStaticFile = employeeStaticFileList.get(employeeStaticFileList.size() - 1);
        assertThat(testEmployeeStaticFile.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
    }

    @Test
    @Transactional
    void createEmployeeStaticFileWithExistingId() throws Exception {
        // Create the EmployeeStaticFile with an existing ID
        employeeStaticFile.setId(1L);
        EmployeeStaticFileDTO employeeStaticFileDTO = employeeStaticFileMapper.toDto(employeeStaticFile);

        int databaseSizeBeforeCreate = employeeStaticFileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeStaticFileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeStaticFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeStaticFile in the database
        List<EmployeeStaticFile> employeeStaticFileList = employeeStaticFileRepository.findAll();
        assertThat(employeeStaticFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployeeStaticFiles() throws Exception {
        // Initialize the database
        employeeStaticFileRepository.saveAndFlush(employeeStaticFile);

        // Get all the employeeStaticFileList
        restEmployeeStaticFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeStaticFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)));
    }

    @Test
    @Transactional
    void getEmployeeStaticFile() throws Exception {
        // Initialize the database
        employeeStaticFileRepository.saveAndFlush(employeeStaticFile);

        // Get the employeeStaticFile
        restEmployeeStaticFileMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeStaticFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeStaticFile.getId().intValue()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeStaticFile() throws Exception {
        // Get the employeeStaticFile
        restEmployeeStaticFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeStaticFile() throws Exception {
        // Initialize the database
        employeeStaticFileRepository.saveAndFlush(employeeStaticFile);

        int databaseSizeBeforeUpdate = employeeStaticFileRepository.findAll().size();

        // Update the employeeStaticFile
        EmployeeStaticFile updatedEmployeeStaticFile = employeeStaticFileRepository.findById(employeeStaticFile.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeeStaticFile are not directly saved in db
        em.detach(updatedEmployeeStaticFile);
        updatedEmployeeStaticFile.filePath(UPDATED_FILE_PATH);
        EmployeeStaticFileDTO employeeStaticFileDTO = employeeStaticFileMapper.toDto(updatedEmployeeStaticFile);

        restEmployeeStaticFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeStaticFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeStaticFileDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeStaticFile in the database
        List<EmployeeStaticFile> employeeStaticFileList = employeeStaticFileRepository.findAll();
        assertThat(employeeStaticFileList).hasSize(databaseSizeBeforeUpdate);
        EmployeeStaticFile testEmployeeStaticFile = employeeStaticFileList.get(employeeStaticFileList.size() - 1);
        assertThat(testEmployeeStaticFile.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeStaticFile() throws Exception {
        int databaseSizeBeforeUpdate = employeeStaticFileRepository.findAll().size();
        employeeStaticFile.setId(count.incrementAndGet());

        // Create the EmployeeStaticFile
        EmployeeStaticFileDTO employeeStaticFileDTO = employeeStaticFileMapper.toDto(employeeStaticFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeStaticFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeStaticFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeStaticFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeStaticFile in the database
        List<EmployeeStaticFile> employeeStaticFileList = employeeStaticFileRepository.findAll();
        assertThat(employeeStaticFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeStaticFile() throws Exception {
        int databaseSizeBeforeUpdate = employeeStaticFileRepository.findAll().size();
        employeeStaticFile.setId(count.incrementAndGet());

        // Create the EmployeeStaticFile
        EmployeeStaticFileDTO employeeStaticFileDTO = employeeStaticFileMapper.toDto(employeeStaticFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeStaticFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeStaticFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeStaticFile in the database
        List<EmployeeStaticFile> employeeStaticFileList = employeeStaticFileRepository.findAll();
        assertThat(employeeStaticFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeStaticFile() throws Exception {
        int databaseSizeBeforeUpdate = employeeStaticFileRepository.findAll().size();
        employeeStaticFile.setId(count.incrementAndGet());

        // Create the EmployeeStaticFile
        EmployeeStaticFileDTO employeeStaticFileDTO = employeeStaticFileMapper.toDto(employeeStaticFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeStaticFileMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeStaticFileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeStaticFile in the database
        List<EmployeeStaticFile> employeeStaticFileList = employeeStaticFileRepository.findAll();
        assertThat(employeeStaticFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeStaticFileWithPatch() throws Exception {
        // Initialize the database
        employeeStaticFileRepository.saveAndFlush(employeeStaticFile);

        int databaseSizeBeforeUpdate = employeeStaticFileRepository.findAll().size();

        // Update the employeeStaticFile using partial update
        EmployeeStaticFile partialUpdatedEmployeeStaticFile = new EmployeeStaticFile();
        partialUpdatedEmployeeStaticFile.setId(employeeStaticFile.getId());

        restEmployeeStaticFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeStaticFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeStaticFile))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeStaticFile in the database
        List<EmployeeStaticFile> employeeStaticFileList = employeeStaticFileRepository.findAll();
        assertThat(employeeStaticFileList).hasSize(databaseSizeBeforeUpdate);
        EmployeeStaticFile testEmployeeStaticFile = employeeStaticFileList.get(employeeStaticFileList.size() - 1);
        assertThat(testEmployeeStaticFile.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeStaticFileWithPatch() throws Exception {
        // Initialize the database
        employeeStaticFileRepository.saveAndFlush(employeeStaticFile);

        int databaseSizeBeforeUpdate = employeeStaticFileRepository.findAll().size();

        // Update the employeeStaticFile using partial update
        EmployeeStaticFile partialUpdatedEmployeeStaticFile = new EmployeeStaticFile();
        partialUpdatedEmployeeStaticFile.setId(employeeStaticFile.getId());

        partialUpdatedEmployeeStaticFile.filePath(UPDATED_FILE_PATH);

        restEmployeeStaticFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeStaticFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeStaticFile))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeStaticFile in the database
        List<EmployeeStaticFile> employeeStaticFileList = employeeStaticFileRepository.findAll();
        assertThat(employeeStaticFileList).hasSize(databaseSizeBeforeUpdate);
        EmployeeStaticFile testEmployeeStaticFile = employeeStaticFileList.get(employeeStaticFileList.size() - 1);
        assertThat(testEmployeeStaticFile.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeStaticFile() throws Exception {
        int databaseSizeBeforeUpdate = employeeStaticFileRepository.findAll().size();
        employeeStaticFile.setId(count.incrementAndGet());

        // Create the EmployeeStaticFile
        EmployeeStaticFileDTO employeeStaticFileDTO = employeeStaticFileMapper.toDto(employeeStaticFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeStaticFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeStaticFileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeStaticFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeStaticFile in the database
        List<EmployeeStaticFile> employeeStaticFileList = employeeStaticFileRepository.findAll();
        assertThat(employeeStaticFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeStaticFile() throws Exception {
        int databaseSizeBeforeUpdate = employeeStaticFileRepository.findAll().size();
        employeeStaticFile.setId(count.incrementAndGet());

        // Create the EmployeeStaticFile
        EmployeeStaticFileDTO employeeStaticFileDTO = employeeStaticFileMapper.toDto(employeeStaticFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeStaticFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeStaticFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeStaticFile in the database
        List<EmployeeStaticFile> employeeStaticFileList = employeeStaticFileRepository.findAll();
        assertThat(employeeStaticFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeStaticFile() throws Exception {
        int databaseSizeBeforeUpdate = employeeStaticFileRepository.findAll().size();
        employeeStaticFile.setId(count.incrementAndGet());

        // Create the EmployeeStaticFile
        EmployeeStaticFileDTO employeeStaticFileDTO = employeeStaticFileMapper.toDto(employeeStaticFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeStaticFileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeStaticFileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeStaticFile in the database
        List<EmployeeStaticFile> employeeStaticFileList = employeeStaticFileRepository.findAll();
        assertThat(employeeStaticFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeStaticFile() throws Exception {
        // Initialize the database
        employeeStaticFileRepository.saveAndFlush(employeeStaticFile);

        int databaseSizeBeforeDelete = employeeStaticFileRepository.findAll().size();

        // Delete the employeeStaticFile
        restEmployeeStaticFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeStaticFile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeeStaticFile> employeeStaticFileList = employeeStaticFileRepository.findAll();
        assertThat(employeeStaticFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
