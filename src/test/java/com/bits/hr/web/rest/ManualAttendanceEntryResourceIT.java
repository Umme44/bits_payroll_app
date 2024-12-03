package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.ManualAttendanceEntry;
import com.bits.hr.repository.ManualAttendanceEntryRepository;
import com.bits.hr.service.ManualAttendanceEntryService;
import com.bits.hr.service.dto.ManualAttendanceEntryDTO;
import com.bits.hr.service.mapper.ManualAttendanceEntryMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ManualAttendanceEntryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ManualAttendanceEntryResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_IN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_IN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_IN_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_IN_NOTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_OUT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OUT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OUT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_OUT_NOTE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_LINE_MANAGER_APPROVED = false;
    private static final Boolean UPDATED_IS_LINE_MANAGER_APPROVED = true;

    private static final Boolean DEFAULT_IS_HR_APPROVED = false;
    private static final Boolean UPDATED_IS_HR_APPROVED = true;

    private static final Boolean DEFAULT_IS_REJECTED = false;
    private static final Boolean UPDATED_IS_REJECTED = true;

    private static final String DEFAULT_REJECTION_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_REJECTION_COMMENT = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/manual-attendance-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ManualAttendanceEntryRepository manualAttendanceEntryRepository;

    @Mock
    private ManualAttendanceEntryRepository manualAttendanceEntryRepositoryMock;

    @Autowired
    private ManualAttendanceEntryMapper manualAttendanceEntryMapper;

    @Mock
    private ManualAttendanceEntryService manualAttendanceEntryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManualAttendanceEntryMockMvc;

    private ManualAttendanceEntry manualAttendanceEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManualAttendanceEntry createEntity(EntityManager em) {
        ManualAttendanceEntry manualAttendanceEntry = new ManualAttendanceEntry()
            .date(DEFAULT_DATE)
            .inTime(DEFAULT_IN_TIME)
            .inNote(DEFAULT_IN_NOTE)
            .outTime(DEFAULT_OUT_TIME)
            .outNote(DEFAULT_OUT_NOTE)
            .isLineManagerApproved(DEFAULT_IS_LINE_MANAGER_APPROVED)
            .isHRApproved(DEFAULT_IS_HR_APPROVED)
            .isRejected(DEFAULT_IS_REJECTED)
            .rejectionComment(DEFAULT_REJECTION_COMMENT)
            .note(DEFAULT_NOTE);
        return manualAttendanceEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManualAttendanceEntry createUpdatedEntity(EntityManager em) {
        ManualAttendanceEntry manualAttendanceEntry = new ManualAttendanceEntry()
            .date(UPDATED_DATE)
            .inTime(UPDATED_IN_TIME)
            .inNote(UPDATED_IN_NOTE)
            .outTime(UPDATED_OUT_TIME)
            .outNote(UPDATED_OUT_NOTE)
            .isLineManagerApproved(UPDATED_IS_LINE_MANAGER_APPROVED)
            .isHRApproved(UPDATED_IS_HR_APPROVED)
            .isRejected(UPDATED_IS_REJECTED)
            .rejectionComment(UPDATED_REJECTION_COMMENT)
            .note(UPDATED_NOTE);
        return manualAttendanceEntry;
    }

    @BeforeEach
    public void initTest() {
        manualAttendanceEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createManualAttendanceEntry() throws Exception {
        int databaseSizeBeforeCreate = manualAttendanceEntryRepository.findAll().size();
        // Create the ManualAttendanceEntry
        ManualAttendanceEntryDTO manualAttendanceEntryDTO = manualAttendanceEntryMapper.toDto(manualAttendanceEntry);
        restManualAttendanceEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualAttendanceEntryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ManualAttendanceEntry in the database
        List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAll();
        assertThat(manualAttendanceEntryList).hasSize(databaseSizeBeforeCreate + 1);
        ManualAttendanceEntry testManualAttendanceEntry = manualAttendanceEntryList.get(manualAttendanceEntryList.size() - 1);
        assertThat(testManualAttendanceEntry.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testManualAttendanceEntry.getInTime()).isEqualTo(DEFAULT_IN_TIME);
        assertThat(testManualAttendanceEntry.getInNote()).isEqualTo(DEFAULT_IN_NOTE);
        assertThat(testManualAttendanceEntry.getOutTime()).isEqualTo(DEFAULT_OUT_TIME);
        assertThat(testManualAttendanceEntry.getOutNote()).isEqualTo(DEFAULT_OUT_NOTE);
        assertThat(testManualAttendanceEntry.getIsLineManagerApproved()).isEqualTo(DEFAULT_IS_LINE_MANAGER_APPROVED);
        assertThat(testManualAttendanceEntry.getIsHRApproved()).isEqualTo(DEFAULT_IS_HR_APPROVED);
        assertThat(testManualAttendanceEntry.getIsRejected()).isEqualTo(DEFAULT_IS_REJECTED);
        assertThat(testManualAttendanceEntry.getRejectionComment()).isEqualTo(DEFAULT_REJECTION_COMMENT);
        assertThat(testManualAttendanceEntry.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createManualAttendanceEntryWithExistingId() throws Exception {
        // Create the ManualAttendanceEntry with an existing ID
        manualAttendanceEntry.setId(1L);
        ManualAttendanceEntryDTO manualAttendanceEntryDTO = manualAttendanceEntryMapper.toDto(manualAttendanceEntry);

        int databaseSizeBeforeCreate = manualAttendanceEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restManualAttendanceEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualAttendanceEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualAttendanceEntry in the database
        List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAll();
        assertThat(manualAttendanceEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllManualAttendanceEntries() throws Exception {
        // Initialize the database
        manualAttendanceEntryRepository.saveAndFlush(manualAttendanceEntry);

        // Get all the manualAttendanceEntryList
        restManualAttendanceEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manualAttendanceEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].inTime").value(hasItem(DEFAULT_IN_TIME.toString())))
            .andExpect(jsonPath("$.[*].inNote").value(hasItem(DEFAULT_IN_NOTE)))
            .andExpect(jsonPath("$.[*].outTime").value(hasItem(DEFAULT_OUT_TIME.toString())))
            .andExpect(jsonPath("$.[*].outNote").value(hasItem(DEFAULT_OUT_NOTE)))
            .andExpect(jsonPath("$.[*].isLineManagerApproved").value(hasItem(DEFAULT_IS_LINE_MANAGER_APPROVED.booleanValue())))
            .andExpect(jsonPath("$.[*].isHRApproved").value(hasItem(DEFAULT_IS_HR_APPROVED.booleanValue())))
            .andExpect(jsonPath("$.[*].isRejected").value(hasItem(DEFAULT_IS_REJECTED.booleanValue())))
            .andExpect(jsonPath("$.[*].rejectionComment").value(hasItem(DEFAULT_REJECTION_COMMENT)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllManualAttendanceEntriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(manualAttendanceEntryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restManualAttendanceEntryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(manualAttendanceEntryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllManualAttendanceEntriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(manualAttendanceEntryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restManualAttendanceEntryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(manualAttendanceEntryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getManualAttendanceEntry() throws Exception {
        // Initialize the database
        manualAttendanceEntryRepository.saveAndFlush(manualAttendanceEntry);

        // Get the manualAttendanceEntry
        restManualAttendanceEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, manualAttendanceEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manualAttendanceEntry.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.inTime").value(DEFAULT_IN_TIME.toString()))
            .andExpect(jsonPath("$.inNote").value(DEFAULT_IN_NOTE))
            .andExpect(jsonPath("$.outTime").value(DEFAULT_OUT_TIME.toString()))
            .andExpect(jsonPath("$.outNote").value(DEFAULT_OUT_NOTE))
            .andExpect(jsonPath("$.isLineManagerApproved").value(DEFAULT_IS_LINE_MANAGER_APPROVED.booleanValue()))
            .andExpect(jsonPath("$.isHRApproved").value(DEFAULT_IS_HR_APPROVED.booleanValue()))
            .andExpect(jsonPath("$.isRejected").value(DEFAULT_IS_REJECTED.booleanValue()))
            .andExpect(jsonPath("$.rejectionComment").value(DEFAULT_REJECTION_COMMENT))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingManualAttendanceEntry() throws Exception {
        // Get the manualAttendanceEntry
        restManualAttendanceEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingManualAttendanceEntry() throws Exception {
        // Initialize the database
        manualAttendanceEntryRepository.saveAndFlush(manualAttendanceEntry);

        int databaseSizeBeforeUpdate = manualAttendanceEntryRepository.findAll().size();

        // Update the manualAttendanceEntry
        ManualAttendanceEntry updatedManualAttendanceEntry = manualAttendanceEntryRepository.findById(manualAttendanceEntry.getId()).get();
        // Disconnect from session so that the updates on updatedManualAttendanceEntry are not directly saved in db
        em.detach(updatedManualAttendanceEntry);
        updatedManualAttendanceEntry
            .date(UPDATED_DATE)
            .inTime(UPDATED_IN_TIME)
            .inNote(UPDATED_IN_NOTE)
            .outTime(UPDATED_OUT_TIME)
            .outNote(UPDATED_OUT_NOTE)
            .isLineManagerApproved(UPDATED_IS_LINE_MANAGER_APPROVED)
            .isHRApproved(UPDATED_IS_HR_APPROVED)
            .isRejected(UPDATED_IS_REJECTED)
            .rejectionComment(UPDATED_REJECTION_COMMENT)
            .note(UPDATED_NOTE);
        ManualAttendanceEntryDTO manualAttendanceEntryDTO = manualAttendanceEntryMapper.toDto(updatedManualAttendanceEntry);

        restManualAttendanceEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manualAttendanceEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualAttendanceEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ManualAttendanceEntry in the database
        List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAll();
        assertThat(manualAttendanceEntryList).hasSize(databaseSizeBeforeUpdate);
        ManualAttendanceEntry testManualAttendanceEntry = manualAttendanceEntryList.get(manualAttendanceEntryList.size() - 1);
        assertThat(testManualAttendanceEntry.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testManualAttendanceEntry.getInTime()).isEqualTo(UPDATED_IN_TIME);
        assertThat(testManualAttendanceEntry.getInNote()).isEqualTo(UPDATED_IN_NOTE);
        assertThat(testManualAttendanceEntry.getOutTime()).isEqualTo(UPDATED_OUT_TIME);
        assertThat(testManualAttendanceEntry.getOutNote()).isEqualTo(UPDATED_OUT_NOTE);
        assertThat(testManualAttendanceEntry.getIsLineManagerApproved()).isEqualTo(UPDATED_IS_LINE_MANAGER_APPROVED);
        assertThat(testManualAttendanceEntry.getIsHRApproved()).isEqualTo(UPDATED_IS_HR_APPROVED);
        assertThat(testManualAttendanceEntry.getIsRejected()).isEqualTo(UPDATED_IS_REJECTED);
        assertThat(testManualAttendanceEntry.getRejectionComment()).isEqualTo(UPDATED_REJECTION_COMMENT);
        assertThat(testManualAttendanceEntry.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingManualAttendanceEntry() throws Exception {
        int databaseSizeBeforeUpdate = manualAttendanceEntryRepository.findAll().size();
        manualAttendanceEntry.setId(count.incrementAndGet());

        // Create the ManualAttendanceEntry
        ManualAttendanceEntryDTO manualAttendanceEntryDTO = manualAttendanceEntryMapper.toDto(manualAttendanceEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManualAttendanceEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manualAttendanceEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualAttendanceEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualAttendanceEntry in the database
        List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAll();
        assertThat(manualAttendanceEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchManualAttendanceEntry() throws Exception {
        int databaseSizeBeforeUpdate = manualAttendanceEntryRepository.findAll().size();
        manualAttendanceEntry.setId(count.incrementAndGet());

        // Create the ManualAttendanceEntry
        ManualAttendanceEntryDTO manualAttendanceEntryDTO = manualAttendanceEntryMapper.toDto(manualAttendanceEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualAttendanceEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualAttendanceEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualAttendanceEntry in the database
        List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAll();
        assertThat(manualAttendanceEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamManualAttendanceEntry() throws Exception {
        int databaseSizeBeforeUpdate = manualAttendanceEntryRepository.findAll().size();
        manualAttendanceEntry.setId(count.incrementAndGet());

        // Create the ManualAttendanceEntry
        ManualAttendanceEntryDTO manualAttendanceEntryDTO = manualAttendanceEntryMapper.toDto(manualAttendanceEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualAttendanceEntryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualAttendanceEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ManualAttendanceEntry in the database
        List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAll();
        assertThat(manualAttendanceEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateManualAttendanceEntryWithPatch() throws Exception {
        // Initialize the database
        manualAttendanceEntryRepository.saveAndFlush(manualAttendanceEntry);

        int databaseSizeBeforeUpdate = manualAttendanceEntryRepository.findAll().size();

        // Update the manualAttendanceEntry using partial update
        ManualAttendanceEntry partialUpdatedManualAttendanceEntry = new ManualAttendanceEntry();
        partialUpdatedManualAttendanceEntry.setId(manualAttendanceEntry.getId());

        partialUpdatedManualAttendanceEntry
            .inNote(UPDATED_IN_NOTE)
            .outTime(UPDATED_OUT_TIME)
            .isLineManagerApproved(UPDATED_IS_LINE_MANAGER_APPROVED)
            .isRejected(UPDATED_IS_REJECTED)
            .rejectionComment(UPDATED_REJECTION_COMMENT);

        restManualAttendanceEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManualAttendanceEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManualAttendanceEntry))
            )
            .andExpect(status().isOk());

        // Validate the ManualAttendanceEntry in the database
        List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAll();
        assertThat(manualAttendanceEntryList).hasSize(databaseSizeBeforeUpdate);
        ManualAttendanceEntry testManualAttendanceEntry = manualAttendanceEntryList.get(manualAttendanceEntryList.size() - 1);
        assertThat(testManualAttendanceEntry.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testManualAttendanceEntry.getInTime()).isEqualTo(DEFAULT_IN_TIME);
        assertThat(testManualAttendanceEntry.getInNote()).isEqualTo(UPDATED_IN_NOTE);
        assertThat(testManualAttendanceEntry.getOutTime()).isEqualTo(UPDATED_OUT_TIME);
        assertThat(testManualAttendanceEntry.getOutNote()).isEqualTo(DEFAULT_OUT_NOTE);
        assertThat(testManualAttendanceEntry.getIsLineManagerApproved()).isEqualTo(UPDATED_IS_LINE_MANAGER_APPROVED);
        assertThat(testManualAttendanceEntry.getIsHRApproved()).isEqualTo(DEFAULT_IS_HR_APPROVED);
        assertThat(testManualAttendanceEntry.getIsRejected()).isEqualTo(UPDATED_IS_REJECTED);
        assertThat(testManualAttendanceEntry.getRejectionComment()).isEqualTo(UPDATED_REJECTION_COMMENT);
        assertThat(testManualAttendanceEntry.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateManualAttendanceEntryWithPatch() throws Exception {
        // Initialize the database
        manualAttendanceEntryRepository.saveAndFlush(manualAttendanceEntry);

        int databaseSizeBeforeUpdate = manualAttendanceEntryRepository.findAll().size();

        // Update the manualAttendanceEntry using partial update
        ManualAttendanceEntry partialUpdatedManualAttendanceEntry = new ManualAttendanceEntry();
        partialUpdatedManualAttendanceEntry.setId(manualAttendanceEntry.getId());

        partialUpdatedManualAttendanceEntry
            .date(UPDATED_DATE)
            .inTime(UPDATED_IN_TIME)
            .inNote(UPDATED_IN_NOTE)
            .outTime(UPDATED_OUT_TIME)
            .outNote(UPDATED_OUT_NOTE)
            .isLineManagerApproved(UPDATED_IS_LINE_MANAGER_APPROVED)
            .isHRApproved(UPDATED_IS_HR_APPROVED)
            .isRejected(UPDATED_IS_REJECTED)
            .rejectionComment(UPDATED_REJECTION_COMMENT)
            .note(UPDATED_NOTE);

        restManualAttendanceEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManualAttendanceEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManualAttendanceEntry))
            )
            .andExpect(status().isOk());

        // Validate the ManualAttendanceEntry in the database
        List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAll();
        assertThat(manualAttendanceEntryList).hasSize(databaseSizeBeforeUpdate);
        ManualAttendanceEntry testManualAttendanceEntry = manualAttendanceEntryList.get(manualAttendanceEntryList.size() - 1);
        assertThat(testManualAttendanceEntry.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testManualAttendanceEntry.getInTime()).isEqualTo(UPDATED_IN_TIME);
        assertThat(testManualAttendanceEntry.getInNote()).isEqualTo(UPDATED_IN_NOTE);
        assertThat(testManualAttendanceEntry.getOutTime()).isEqualTo(UPDATED_OUT_TIME);
        assertThat(testManualAttendanceEntry.getOutNote()).isEqualTo(UPDATED_OUT_NOTE);
        assertThat(testManualAttendanceEntry.getIsLineManagerApproved()).isEqualTo(UPDATED_IS_LINE_MANAGER_APPROVED);
        assertThat(testManualAttendanceEntry.getIsHRApproved()).isEqualTo(UPDATED_IS_HR_APPROVED);
        assertThat(testManualAttendanceEntry.getIsRejected()).isEqualTo(UPDATED_IS_REJECTED);
        assertThat(testManualAttendanceEntry.getRejectionComment()).isEqualTo(UPDATED_REJECTION_COMMENT);
        assertThat(testManualAttendanceEntry.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingManualAttendanceEntry() throws Exception {
        int databaseSizeBeforeUpdate = manualAttendanceEntryRepository.findAll().size();
        manualAttendanceEntry.setId(count.incrementAndGet());

        // Create the ManualAttendanceEntry
        ManualAttendanceEntryDTO manualAttendanceEntryDTO = manualAttendanceEntryMapper.toDto(manualAttendanceEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManualAttendanceEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, manualAttendanceEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manualAttendanceEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualAttendanceEntry in the database
        List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAll();
        assertThat(manualAttendanceEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchManualAttendanceEntry() throws Exception {
        int databaseSizeBeforeUpdate = manualAttendanceEntryRepository.findAll().size();
        manualAttendanceEntry.setId(count.incrementAndGet());

        // Create the ManualAttendanceEntry
        ManualAttendanceEntryDTO manualAttendanceEntryDTO = manualAttendanceEntryMapper.toDto(manualAttendanceEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualAttendanceEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manualAttendanceEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualAttendanceEntry in the database
        List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAll();
        assertThat(manualAttendanceEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamManualAttendanceEntry() throws Exception {
        int databaseSizeBeforeUpdate = manualAttendanceEntryRepository.findAll().size();
        manualAttendanceEntry.setId(count.incrementAndGet());

        // Create the ManualAttendanceEntry
        ManualAttendanceEntryDTO manualAttendanceEntryDTO = manualAttendanceEntryMapper.toDto(manualAttendanceEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualAttendanceEntryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manualAttendanceEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ManualAttendanceEntry in the database
        List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAll();
        assertThat(manualAttendanceEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteManualAttendanceEntry() throws Exception {
        // Initialize the database
        manualAttendanceEntryRepository.saveAndFlush(manualAttendanceEntry);

        int databaseSizeBeforeDelete = manualAttendanceEntryRepository.findAll().size();

        // Delete the manualAttendanceEntry
        restManualAttendanceEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, manualAttendanceEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAll();
        assertThat(manualAttendanceEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
