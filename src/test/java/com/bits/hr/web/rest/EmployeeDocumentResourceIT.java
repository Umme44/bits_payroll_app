package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.EmployeeDocument;
import com.bits.hr.repository.EmployeeDocumentRepository;
import com.bits.hr.service.dto.EmployeeDocumentDTO;
import com.bits.hr.service.mapper.EmployeeDocumentMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link EmployeeDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeDocumentResourceIT {

    private static final String DEFAULT_PIN = "AAAAAAAAAA";
    private static final String UPDATED_PIN = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HAS_EMPLOYEE_VISIBILITY = false;
    private static final Boolean UPDATED_HAS_EMPLOYEE_VISIBILITY = true;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_FILE_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_FILE_EXTENSION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employee-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeDocumentRepository employeeDocumentRepository;

    @Autowired
    private EmployeeDocumentMapper employeeDocumentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeDocumentMockMvc;

    private EmployeeDocument employeeDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeDocument createEntity(EntityManager em) {
        EmployeeDocument employeeDocument = new EmployeeDocument()
            .pin(DEFAULT_PIN)
            .fileName(DEFAULT_FILE_NAME)
            .filePath(DEFAULT_FILE_PATH)
            .hasEmployeeVisibility(DEFAULT_HAS_EMPLOYEE_VISIBILITY)
            .remarks(DEFAULT_REMARKS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT)
            .fileExtension(DEFAULT_FILE_EXTENSION);
        return employeeDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeDocument createUpdatedEntity(EntityManager em) {
        EmployeeDocument employeeDocument = new EmployeeDocument()
            .pin(UPDATED_PIN)
            .fileName(UPDATED_FILE_NAME)
            .filePath(UPDATED_FILE_PATH)
            .hasEmployeeVisibility(UPDATED_HAS_EMPLOYEE_VISIBILITY)
            .remarks(UPDATED_REMARKS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .fileExtension(UPDATED_FILE_EXTENSION);
        return employeeDocument;
    }

    @BeforeEach
    public void initTest() {
        employeeDocument = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeeDocument() throws Exception {
        int databaseSizeBeforeCreate = employeeDocumentRepository.findAll().size();
        // Create the EmployeeDocument
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);
        restEmployeeDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeDocument testEmployeeDocument = employeeDocumentList.get(employeeDocumentList.size() - 1);
        assertThat(testEmployeeDocument.getPin()).isEqualTo(DEFAULT_PIN);
        assertThat(testEmployeeDocument.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testEmployeeDocument.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testEmployeeDocument.getHasEmployeeVisibility()).isEqualTo(DEFAULT_HAS_EMPLOYEE_VISIBILITY);
        assertThat(testEmployeeDocument.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testEmployeeDocument.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testEmployeeDocument.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEmployeeDocument.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testEmployeeDocument.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testEmployeeDocument.getFileExtension()).isEqualTo(DEFAULT_FILE_EXTENSION);
    }

    @Test
    @Transactional
    void createEmployeeDocumentWithExistingId() throws Exception {
        // Create the EmployeeDocument with an existing ID
        employeeDocument.setId(1L);
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        int databaseSizeBeforeCreate = employeeDocumentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPinIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeDocumentRepository.findAll().size();
        // set the field null
        employeeDocument.setPin(null);

        // Create the EmployeeDocument, which fails.
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        restEmployeeDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeDocumentRepository.findAll().size();
        // set the field null
        employeeDocument.setFileName(null);

        // Create the EmployeeDocument, which fails.
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        restEmployeeDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFilePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeDocumentRepository.findAll().size();
        // set the field null
        employeeDocument.setFilePath(null);

        // Create the EmployeeDocument, which fails.
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        restEmployeeDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeDocuments() throws Exception {
        // Initialize the database
        employeeDocumentRepository.saveAndFlush(employeeDocument);

        // Get all the employeeDocumentList
        restEmployeeDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].pin").value(hasItem(DEFAULT_PIN)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].hasEmployeeVisibility").value(hasItem(DEFAULT_HAS_EMPLOYEE_VISIBILITY.booleanValue())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].fileExtension").value(hasItem(DEFAULT_FILE_EXTENSION)));
    }

    @Test
    @Transactional
    void getEmployeeDocument() throws Exception {
        // Initialize the database
        employeeDocumentRepository.saveAndFlush(employeeDocument);

        // Get the employeeDocument
        restEmployeeDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeDocument.getId().intValue()))
            .andExpect(jsonPath("$.pin").value(DEFAULT_PIN))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.hasEmployeeVisibility").value(DEFAULT_HAS_EMPLOYEE_VISIBILITY.booleanValue()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.fileExtension").value(DEFAULT_FILE_EXTENSION));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeDocument() throws Exception {
        // Get the employeeDocument
        restEmployeeDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeDocument() throws Exception {
        // Initialize the database
        employeeDocumentRepository.saveAndFlush(employeeDocument);

        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().size();

        // Update the employeeDocument
        EmployeeDocument updatedEmployeeDocument = employeeDocumentRepository.findById(employeeDocument.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeeDocument are not directly saved in db
        em.detach(updatedEmployeeDocument);
        updatedEmployeeDocument
            .pin(UPDATED_PIN)
            .fileName(UPDATED_FILE_NAME)
            .filePath(UPDATED_FILE_PATH)
            .hasEmployeeVisibility(UPDATED_HAS_EMPLOYEE_VISIBILITY)
            .remarks(UPDATED_REMARKS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .fileExtension(UPDATED_FILE_EXTENSION);
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(updatedEmployeeDocument);

        restEmployeeDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
        EmployeeDocument testEmployeeDocument = employeeDocumentList.get(employeeDocumentList.size() - 1);
        assertThat(testEmployeeDocument.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testEmployeeDocument.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testEmployeeDocument.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testEmployeeDocument.getHasEmployeeVisibility()).isEqualTo(UPDATED_HAS_EMPLOYEE_VISIBILITY);
        assertThat(testEmployeeDocument.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testEmployeeDocument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmployeeDocument.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployeeDocument.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testEmployeeDocument.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmployeeDocument.getFileExtension()).isEqualTo(UPDATED_FILE_EXTENSION);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeDocument() throws Exception {
        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().size();
        employeeDocument.setId(count.incrementAndGet());

        // Create the EmployeeDocument
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeDocument() throws Exception {
        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().size();
        employeeDocument.setId(count.incrementAndGet());

        // Create the EmployeeDocument
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeDocument() throws Exception {
        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().size();
        employeeDocument.setId(count.incrementAndGet());

        // Create the EmployeeDocument
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeDocumentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeDocumentWithPatch() throws Exception {
        // Initialize the database
        employeeDocumentRepository.saveAndFlush(employeeDocument);

        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().size();

        // Update the employeeDocument using partial update
        EmployeeDocument partialUpdatedEmployeeDocument = new EmployeeDocument();
        partialUpdatedEmployeeDocument.setId(employeeDocument.getId());

        partialUpdatedEmployeeDocument
            .hasEmployeeVisibility(UPDATED_HAS_EMPLOYEE_VISIBILITY)
            .createdBy(UPDATED_CREATED_BY)
            .fileExtension(UPDATED_FILE_EXTENSION);

        restEmployeeDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeDocument))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
        EmployeeDocument testEmployeeDocument = employeeDocumentList.get(employeeDocumentList.size() - 1);
        assertThat(testEmployeeDocument.getPin()).isEqualTo(DEFAULT_PIN);
        assertThat(testEmployeeDocument.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testEmployeeDocument.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testEmployeeDocument.getHasEmployeeVisibility()).isEqualTo(UPDATED_HAS_EMPLOYEE_VISIBILITY);
        assertThat(testEmployeeDocument.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testEmployeeDocument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmployeeDocument.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEmployeeDocument.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testEmployeeDocument.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testEmployeeDocument.getFileExtension()).isEqualTo(UPDATED_FILE_EXTENSION);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeDocumentWithPatch() throws Exception {
        // Initialize the database
        employeeDocumentRepository.saveAndFlush(employeeDocument);

        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().size();

        // Update the employeeDocument using partial update
        EmployeeDocument partialUpdatedEmployeeDocument = new EmployeeDocument();
        partialUpdatedEmployeeDocument.setId(employeeDocument.getId());

        partialUpdatedEmployeeDocument
            .pin(UPDATED_PIN)
            .fileName(UPDATED_FILE_NAME)
            .filePath(UPDATED_FILE_PATH)
            .hasEmployeeVisibility(UPDATED_HAS_EMPLOYEE_VISIBILITY)
            .remarks(UPDATED_REMARKS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .fileExtension(UPDATED_FILE_EXTENSION);

        restEmployeeDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeDocument))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
        EmployeeDocument testEmployeeDocument = employeeDocumentList.get(employeeDocumentList.size() - 1);
        assertThat(testEmployeeDocument.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testEmployeeDocument.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testEmployeeDocument.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testEmployeeDocument.getHasEmployeeVisibility()).isEqualTo(UPDATED_HAS_EMPLOYEE_VISIBILITY);
        assertThat(testEmployeeDocument.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testEmployeeDocument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmployeeDocument.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployeeDocument.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testEmployeeDocument.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmployeeDocument.getFileExtension()).isEqualTo(UPDATED_FILE_EXTENSION);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeDocument() throws Exception {
        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().size();
        employeeDocument.setId(count.incrementAndGet());

        // Create the EmployeeDocument
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeDocument() throws Exception {
        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().size();
        employeeDocument.setId(count.incrementAndGet());

        // Create the EmployeeDocument
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeDocument() throws Exception {
        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().size();
        employeeDocument.setId(count.incrementAndGet());

        // Create the EmployeeDocument
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeDocument() throws Exception {
        // Initialize the database
        employeeDocumentRepository.saveAndFlush(employeeDocument);

        int databaseSizeBeforeDelete = employeeDocumentRepository.findAll().size();

        // Delete the employeeDocument
        restEmployeeDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
