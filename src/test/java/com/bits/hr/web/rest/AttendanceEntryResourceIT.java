package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.AttendanceEntry;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.AttendanceEntryRepository;
import com.bits.hr.service.dto.AttendanceEntryDTO;
import com.bits.hr.service.mapper.AttendanceEntryMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AttendanceEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttendanceEntryResourceIT {

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

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.APPROVED;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attendance-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttendanceEntryRepository attendanceEntryRepository;

    @Autowired
    private AttendanceEntryMapper attendanceEntryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttendanceEntryMockMvc;

    private AttendanceEntry attendanceEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttendanceEntry createEntity(EntityManager em) {
        AttendanceEntry attendanceEntry = new AttendanceEntry()
            .date(DEFAULT_DATE)
            .inTime(DEFAULT_IN_TIME)
            .inNote(DEFAULT_IN_NOTE)
            .outTime(DEFAULT_OUT_TIME)
            .outNote(DEFAULT_OUT_NOTE)
            .status(DEFAULT_STATUS)
            .note(DEFAULT_NOTE);
        return attendanceEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttendanceEntry createUpdatedEntity(EntityManager em) {
        AttendanceEntry attendanceEntry = new AttendanceEntry()
            .date(UPDATED_DATE)
            .inTime(UPDATED_IN_TIME)
            .inNote(UPDATED_IN_NOTE)
            .outTime(UPDATED_OUT_TIME)
            .outNote(UPDATED_OUT_NOTE)
            .status(UPDATED_STATUS)
            .note(UPDATED_NOTE);
        return attendanceEntry;
    }

    @BeforeEach
    public void initTest() {
        attendanceEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createAttendanceEntry() throws Exception {
        int databaseSizeBeforeCreate = attendanceEntryRepository.findAll().size();
        // Create the AttendanceEntry
        AttendanceEntryDTO attendanceEntryDTO = attendanceEntryMapper.toDto(attendanceEntry);
        restAttendanceEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attendanceEntryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AttendanceEntry in the database
        List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.findAll();
        assertThat(attendanceEntryList).hasSize(databaseSizeBeforeCreate + 1);
        AttendanceEntry testAttendanceEntry = attendanceEntryList.get(attendanceEntryList.size() - 1);
        assertThat(testAttendanceEntry.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAttendanceEntry.getInTime()).isEqualTo(DEFAULT_IN_TIME);
        assertThat(testAttendanceEntry.getInNote()).isEqualTo(DEFAULT_IN_NOTE);
        assertThat(testAttendanceEntry.getOutTime()).isEqualTo(DEFAULT_OUT_TIME);
        assertThat(testAttendanceEntry.getOutNote()).isEqualTo(DEFAULT_OUT_NOTE);
        assertThat(testAttendanceEntry.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAttendanceEntry.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createAttendanceEntryWithExistingId() throws Exception {
        // Create the AttendanceEntry with an existing ID
        attendanceEntry.setId(1L);
        AttendanceEntryDTO attendanceEntryDTO = attendanceEntryMapper.toDto(attendanceEntry);

        int databaseSizeBeforeCreate = attendanceEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttendanceEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attendanceEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttendanceEntry in the database
        List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.findAll();
        assertThat(attendanceEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAttendanceEntries() throws Exception {
        // Initialize the database
        attendanceEntryRepository.saveAndFlush(attendanceEntry);

        // Get all the attendanceEntryList
        restAttendanceEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attendanceEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].inTime").value(hasItem(DEFAULT_IN_TIME.toString())))
            .andExpect(jsonPath("$.[*].inNote").value(hasItem(DEFAULT_IN_NOTE)))
            .andExpect(jsonPath("$.[*].outTime").value(hasItem(DEFAULT_OUT_TIME.toString())))
            .andExpect(jsonPath("$.[*].outNote").value(hasItem(DEFAULT_OUT_NOTE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getAttendanceEntry() throws Exception {
        // Initialize the database
        attendanceEntryRepository.saveAndFlush(attendanceEntry);

        // Get the attendanceEntry
        restAttendanceEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, attendanceEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attendanceEntry.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.inTime").value(DEFAULT_IN_TIME.toString()))
            .andExpect(jsonPath("$.inNote").value(DEFAULT_IN_NOTE))
            .andExpect(jsonPath("$.outTime").value(DEFAULT_OUT_TIME.toString()))
            .andExpect(jsonPath("$.outNote").value(DEFAULT_OUT_NOTE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingAttendanceEntry() throws Exception {
        // Get the attendanceEntry
        restAttendanceEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAttendanceEntry() throws Exception {
        // Initialize the database
        attendanceEntryRepository.saveAndFlush(attendanceEntry);

        int databaseSizeBeforeUpdate = attendanceEntryRepository.findAll().size();

        // Update the attendanceEntry
        AttendanceEntry updatedAttendanceEntry = attendanceEntryRepository.findById(attendanceEntry.getId()).get();
        // Disconnect from session so that the updates on updatedAttendanceEntry are not directly saved in db
        em.detach(updatedAttendanceEntry);
        updatedAttendanceEntry
            .date(UPDATED_DATE)
            .inTime(UPDATED_IN_TIME)
            .inNote(UPDATED_IN_NOTE)
            .outTime(UPDATED_OUT_TIME)
            .outNote(UPDATED_OUT_NOTE)
            .status(UPDATED_STATUS)
            .note(UPDATED_NOTE);
        AttendanceEntryDTO attendanceEntryDTO = attendanceEntryMapper.toDto(updatedAttendanceEntry);

        restAttendanceEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attendanceEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attendanceEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the AttendanceEntry in the database
        List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.findAll();
        assertThat(attendanceEntryList).hasSize(databaseSizeBeforeUpdate);
        AttendanceEntry testAttendanceEntry = attendanceEntryList.get(attendanceEntryList.size() - 1);
        assertThat(testAttendanceEntry.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAttendanceEntry.getInTime()).isEqualTo(UPDATED_IN_TIME);
        assertThat(testAttendanceEntry.getInNote()).isEqualTo(UPDATED_IN_NOTE);
        assertThat(testAttendanceEntry.getOutTime()).isEqualTo(UPDATED_OUT_TIME);
        assertThat(testAttendanceEntry.getOutNote()).isEqualTo(UPDATED_OUT_NOTE);
        assertThat(testAttendanceEntry.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAttendanceEntry.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingAttendanceEntry() throws Exception {
        int databaseSizeBeforeUpdate = attendanceEntryRepository.findAll().size();
        attendanceEntry.setId(count.incrementAndGet());

        // Create the AttendanceEntry
        AttendanceEntryDTO attendanceEntryDTO = attendanceEntryMapper.toDto(attendanceEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttendanceEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attendanceEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attendanceEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttendanceEntry in the database
        List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.findAll();
        assertThat(attendanceEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttendanceEntry() throws Exception {
        int databaseSizeBeforeUpdate = attendanceEntryRepository.findAll().size();
        attendanceEntry.setId(count.incrementAndGet());

        // Create the AttendanceEntry
        AttendanceEntryDTO attendanceEntryDTO = attendanceEntryMapper.toDto(attendanceEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attendanceEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttendanceEntry in the database
        List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.findAll();
        assertThat(attendanceEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttendanceEntry() throws Exception {
        int databaseSizeBeforeUpdate = attendanceEntryRepository.findAll().size();
        attendanceEntry.setId(count.incrementAndGet());

        // Create the AttendanceEntry
        AttendanceEntryDTO attendanceEntryDTO = attendanceEntryMapper.toDto(attendanceEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceEntryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attendanceEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttendanceEntry in the database
        List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.findAll();
        assertThat(attendanceEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttendanceEntryWithPatch() throws Exception {
        // Initialize the database
        attendanceEntryRepository.saveAndFlush(attendanceEntry);

        int databaseSizeBeforeUpdate = attendanceEntryRepository.findAll().size();

        // Update the attendanceEntry using partial update
        AttendanceEntry partialUpdatedAttendanceEntry = new AttendanceEntry();
        partialUpdatedAttendanceEntry.setId(attendanceEntry.getId());

        partialUpdatedAttendanceEntry
            .date(UPDATED_DATE)
            .inTime(UPDATED_IN_TIME)
            .outTime(UPDATED_OUT_TIME)
            .outNote(UPDATED_OUT_NOTE)
            .status(UPDATED_STATUS);

        restAttendanceEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttendanceEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttendanceEntry))
            )
            .andExpect(status().isOk());

        // Validate the AttendanceEntry in the database
        List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.findAll();
        assertThat(attendanceEntryList).hasSize(databaseSizeBeforeUpdate);
        AttendanceEntry testAttendanceEntry = attendanceEntryList.get(attendanceEntryList.size() - 1);
        assertThat(testAttendanceEntry.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAttendanceEntry.getInTime()).isEqualTo(UPDATED_IN_TIME);
        assertThat(testAttendanceEntry.getInNote()).isEqualTo(DEFAULT_IN_NOTE);
        assertThat(testAttendanceEntry.getOutTime()).isEqualTo(UPDATED_OUT_TIME);
        assertThat(testAttendanceEntry.getOutNote()).isEqualTo(UPDATED_OUT_NOTE);
        assertThat(testAttendanceEntry.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAttendanceEntry.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateAttendanceEntryWithPatch() throws Exception {
        // Initialize the database
        attendanceEntryRepository.saveAndFlush(attendanceEntry);

        int databaseSizeBeforeUpdate = attendanceEntryRepository.findAll().size();

        // Update the attendanceEntry using partial update
        AttendanceEntry partialUpdatedAttendanceEntry = new AttendanceEntry();
        partialUpdatedAttendanceEntry.setId(attendanceEntry.getId());

        partialUpdatedAttendanceEntry
            .date(UPDATED_DATE)
            .inTime(UPDATED_IN_TIME)
            .inNote(UPDATED_IN_NOTE)
            .outTime(UPDATED_OUT_TIME)
            .outNote(UPDATED_OUT_NOTE)
            .status(UPDATED_STATUS)
            .note(UPDATED_NOTE);

        restAttendanceEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttendanceEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttendanceEntry))
            )
            .andExpect(status().isOk());

        // Validate the AttendanceEntry in the database
        List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.findAll();
        assertThat(attendanceEntryList).hasSize(databaseSizeBeforeUpdate);
        AttendanceEntry testAttendanceEntry = attendanceEntryList.get(attendanceEntryList.size() - 1);
        assertThat(testAttendanceEntry.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAttendanceEntry.getInTime()).isEqualTo(UPDATED_IN_TIME);
        assertThat(testAttendanceEntry.getInNote()).isEqualTo(UPDATED_IN_NOTE);
        assertThat(testAttendanceEntry.getOutTime()).isEqualTo(UPDATED_OUT_TIME);
        assertThat(testAttendanceEntry.getOutNote()).isEqualTo(UPDATED_OUT_NOTE);
        assertThat(testAttendanceEntry.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAttendanceEntry.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingAttendanceEntry() throws Exception {
        int databaseSizeBeforeUpdate = attendanceEntryRepository.findAll().size();
        attendanceEntry.setId(count.incrementAndGet());

        // Create the AttendanceEntry
        AttendanceEntryDTO attendanceEntryDTO = attendanceEntryMapper.toDto(attendanceEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttendanceEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attendanceEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attendanceEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttendanceEntry in the database
        List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.findAll();
        assertThat(attendanceEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttendanceEntry() throws Exception {
        int databaseSizeBeforeUpdate = attendanceEntryRepository.findAll().size();
        attendanceEntry.setId(count.incrementAndGet());

        // Create the AttendanceEntry
        AttendanceEntryDTO attendanceEntryDTO = attendanceEntryMapper.toDto(attendanceEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attendanceEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttendanceEntry in the database
        List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.findAll();
        assertThat(attendanceEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttendanceEntry() throws Exception {
        int databaseSizeBeforeUpdate = attendanceEntryRepository.findAll().size();
        attendanceEntry.setId(count.incrementAndGet());

        // Create the AttendanceEntry
        AttendanceEntryDTO attendanceEntryDTO = attendanceEntryMapper.toDto(attendanceEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceEntryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attendanceEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttendanceEntry in the database
        List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.findAll();
        assertThat(attendanceEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttendanceEntry() throws Exception {
        // Initialize the database
        attendanceEntryRepository.saveAndFlush(attendanceEntry);

        int databaseSizeBeforeDelete = attendanceEntryRepository.findAll().size();

        // Delete the attendanceEntry
        restAttendanceEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, attendanceEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.findAll();
        assertThat(attendanceEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
