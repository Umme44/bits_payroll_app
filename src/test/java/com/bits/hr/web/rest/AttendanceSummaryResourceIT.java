package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.AttendanceSummary;
import com.bits.hr.repository.AttendanceSummaryRepository;
import com.bits.hr.service.AttendanceSummaryService;
import com.bits.hr.service.dto.AttendanceSummaryDTO;
import com.bits.hr.service.mapper.AttendanceSummaryMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AttendanceSummaryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AttendanceSummaryResourceIT {

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_TOTAL_WORKING_DAYS = 1;
    private static final Integer UPDATED_TOTAL_WORKING_DAYS = 2;

    private static final Integer DEFAULT_TOTAL_LEAVE_DAYS = 1;
    private static final Integer UPDATED_TOTAL_LEAVE_DAYS = 2;

    private static final Integer DEFAULT_TOTAL_ABSENT_DAYS = 1;
    private static final Integer UPDATED_TOTAL_ABSENT_DAYS = 2;

    private static final Integer DEFAULT_TOTAL_FRACTION_DAYS = 1;
    private static final Integer UPDATED_TOTAL_FRACTION_DAYS = 2;

    private static final LocalDate DEFAULT_ATTENDANCE_REGULARISATION_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ATTENDANCE_REGULARISATION_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ATTENDANCE_REGULARISATION_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ATTENDANCE_REGULARISATION_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/attendance-summaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttendanceSummaryRepository attendanceSummaryRepository;

    @Mock
    private AttendanceSummaryRepository attendanceSummaryRepositoryMock;

    @Autowired
    private AttendanceSummaryMapper attendanceSummaryMapper;

    @Mock
    private AttendanceSummaryService attendanceSummaryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttendanceSummaryMockMvc;

    private AttendanceSummary attendanceSummary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttendanceSummary createEntity(EntityManager em) {
        AttendanceSummary attendanceSummary = new AttendanceSummary()
            .month(DEFAULT_MONTH)
            .year(DEFAULT_YEAR)
            .totalWorkingDays(DEFAULT_TOTAL_WORKING_DAYS)
            .totalLeaveDays(DEFAULT_TOTAL_LEAVE_DAYS)
            .totalAbsentDays(DEFAULT_TOTAL_ABSENT_DAYS)
            .totalFractionDays(DEFAULT_TOTAL_FRACTION_DAYS)
            .attendanceRegularisationStartDate(DEFAULT_ATTENDANCE_REGULARISATION_START_DATE)
            .attendanceRegularisationEndDate(DEFAULT_ATTENDANCE_REGULARISATION_END_DATE);
        return attendanceSummary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttendanceSummary createUpdatedEntity(EntityManager em) {
        AttendanceSummary attendanceSummary = new AttendanceSummary()
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .totalWorkingDays(UPDATED_TOTAL_WORKING_DAYS)
            .totalLeaveDays(UPDATED_TOTAL_LEAVE_DAYS)
            .totalAbsentDays(UPDATED_TOTAL_ABSENT_DAYS)
            .totalFractionDays(UPDATED_TOTAL_FRACTION_DAYS)
            .attendanceRegularisationStartDate(UPDATED_ATTENDANCE_REGULARISATION_START_DATE)
            .attendanceRegularisationEndDate(UPDATED_ATTENDANCE_REGULARISATION_END_DATE);
        return attendanceSummary;
    }

    @BeforeEach
    public void initTest() {
        attendanceSummary = createEntity(em);
    }

    @Test
    @Transactional
    void createAttendanceSummary() throws Exception {
        int databaseSizeBeforeCreate = attendanceSummaryRepository.findAll().size();
        // Create the AttendanceSummary
        AttendanceSummaryDTO attendanceSummaryDTO = attendanceSummaryMapper.toDto(attendanceSummary);
        restAttendanceSummaryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSummaryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AttendanceSummary in the database
        List<AttendanceSummary> attendanceSummaryList = attendanceSummaryRepository.findAll();
        assertThat(attendanceSummaryList).hasSize(databaseSizeBeforeCreate + 1);
        AttendanceSummary testAttendanceSummary = attendanceSummaryList.get(attendanceSummaryList.size() - 1);
        assertThat(testAttendanceSummary.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testAttendanceSummary.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testAttendanceSummary.getTotalWorkingDays()).isEqualTo(DEFAULT_TOTAL_WORKING_DAYS);
        assertThat(testAttendanceSummary.getTotalLeaveDays()).isEqualTo(DEFAULT_TOTAL_LEAVE_DAYS);
        assertThat(testAttendanceSummary.getTotalAbsentDays()).isEqualTo(DEFAULT_TOTAL_ABSENT_DAYS);
        assertThat(testAttendanceSummary.getTotalFractionDays()).isEqualTo(DEFAULT_TOTAL_FRACTION_DAYS);
        assertThat(testAttendanceSummary.getAttendanceRegularisationStartDate()).isEqualTo(DEFAULT_ATTENDANCE_REGULARISATION_START_DATE);
        assertThat(testAttendanceSummary.getAttendanceRegularisationEndDate()).isEqualTo(DEFAULT_ATTENDANCE_REGULARISATION_END_DATE);
    }

    @Test
    @Transactional
    void createAttendanceSummaryWithExistingId() throws Exception {
        // Create the AttendanceSummary with an existing ID
        attendanceSummary.setId(1L);
        AttendanceSummaryDTO attendanceSummaryDTO = attendanceSummaryMapper.toDto(attendanceSummary);

        int databaseSizeBeforeCreate = attendanceSummaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttendanceSummaryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSummaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttendanceSummary in the database
        List<AttendanceSummary> attendanceSummaryList = attendanceSummaryRepository.findAll();
        assertThat(attendanceSummaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAttendanceSummaries() throws Exception {
        // Initialize the database
        attendanceSummaryRepository.saveAndFlush(attendanceSummary);

        // Get all the attendanceSummaryList
        restAttendanceSummaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attendanceSummary.getId().intValue())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].totalWorkingDays").value(hasItem(DEFAULT_TOTAL_WORKING_DAYS)))
            .andExpect(jsonPath("$.[*].totalLeaveDays").value(hasItem(DEFAULT_TOTAL_LEAVE_DAYS)))
            .andExpect(jsonPath("$.[*].totalAbsentDays").value(hasItem(DEFAULT_TOTAL_ABSENT_DAYS)))
            .andExpect(jsonPath("$.[*].totalFractionDays").value(hasItem(DEFAULT_TOTAL_FRACTION_DAYS)))
            .andExpect(
                jsonPath("$.[*].attendanceRegularisationStartDate").value(hasItem(DEFAULT_ATTENDANCE_REGULARISATION_START_DATE.toString()))
            )
            .andExpect(
                jsonPath("$.[*].attendanceRegularisationEndDate").value(hasItem(DEFAULT_ATTENDANCE_REGULARISATION_END_DATE.toString()))
            );
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttendanceSummariesWithEagerRelationshipsIsEnabled() throws Exception {
        when(attendanceSummaryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAttendanceSummaryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(attendanceSummaryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttendanceSummariesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(attendanceSummaryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAttendanceSummaryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(attendanceSummaryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAttendanceSummary() throws Exception {
        // Initialize the database
        attendanceSummaryRepository.saveAndFlush(attendanceSummary);

        // Get the attendanceSummary
        restAttendanceSummaryMockMvc
            .perform(get(ENTITY_API_URL_ID, attendanceSummary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attendanceSummary.getId().intValue()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.totalWorkingDays").value(DEFAULT_TOTAL_WORKING_DAYS))
            .andExpect(jsonPath("$.totalLeaveDays").value(DEFAULT_TOTAL_LEAVE_DAYS))
            .andExpect(jsonPath("$.totalAbsentDays").value(DEFAULT_TOTAL_ABSENT_DAYS))
            .andExpect(jsonPath("$.totalFractionDays").value(DEFAULT_TOTAL_FRACTION_DAYS))
            .andExpect(jsonPath("$.attendanceRegularisationStartDate").value(DEFAULT_ATTENDANCE_REGULARISATION_START_DATE.toString()))
            .andExpect(jsonPath("$.attendanceRegularisationEndDate").value(DEFAULT_ATTENDANCE_REGULARISATION_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAttendanceSummary() throws Exception {
        // Get the attendanceSummary
        restAttendanceSummaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAttendanceSummary() throws Exception {
        // Initialize the database
        attendanceSummaryRepository.saveAndFlush(attendanceSummary);

        int databaseSizeBeforeUpdate = attendanceSummaryRepository.findAll().size();

        // Update the attendanceSummary
        AttendanceSummary updatedAttendanceSummary = attendanceSummaryRepository.findById(attendanceSummary.getId()).get();
        // Disconnect from session so that the updates on updatedAttendanceSummary are not directly saved in db
        em.detach(updatedAttendanceSummary);
        updatedAttendanceSummary
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .totalWorkingDays(UPDATED_TOTAL_WORKING_DAYS)
            .totalLeaveDays(UPDATED_TOTAL_LEAVE_DAYS)
            .totalAbsentDays(UPDATED_TOTAL_ABSENT_DAYS)
            .totalFractionDays(UPDATED_TOTAL_FRACTION_DAYS)
            .attendanceRegularisationStartDate(UPDATED_ATTENDANCE_REGULARISATION_START_DATE)
            .attendanceRegularisationEndDate(UPDATED_ATTENDANCE_REGULARISATION_END_DATE);
        AttendanceSummaryDTO attendanceSummaryDTO = attendanceSummaryMapper.toDto(updatedAttendanceSummary);

        restAttendanceSummaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attendanceSummaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSummaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the AttendanceSummary in the database
        List<AttendanceSummary> attendanceSummaryList = attendanceSummaryRepository.findAll();
        assertThat(attendanceSummaryList).hasSize(databaseSizeBeforeUpdate);
        AttendanceSummary testAttendanceSummary = attendanceSummaryList.get(attendanceSummaryList.size() - 1);
        assertThat(testAttendanceSummary.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testAttendanceSummary.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testAttendanceSummary.getTotalWorkingDays()).isEqualTo(UPDATED_TOTAL_WORKING_DAYS);
        assertThat(testAttendanceSummary.getTotalLeaveDays()).isEqualTo(UPDATED_TOTAL_LEAVE_DAYS);
        assertThat(testAttendanceSummary.getTotalAbsentDays()).isEqualTo(UPDATED_TOTAL_ABSENT_DAYS);
        assertThat(testAttendanceSummary.getTotalFractionDays()).isEqualTo(UPDATED_TOTAL_FRACTION_DAYS);
        assertThat(testAttendanceSummary.getAttendanceRegularisationStartDate()).isEqualTo(UPDATED_ATTENDANCE_REGULARISATION_START_DATE);
        assertThat(testAttendanceSummary.getAttendanceRegularisationEndDate()).isEqualTo(UPDATED_ATTENDANCE_REGULARISATION_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingAttendanceSummary() throws Exception {
        int databaseSizeBeforeUpdate = attendanceSummaryRepository.findAll().size();
        attendanceSummary.setId(count.incrementAndGet());

        // Create the AttendanceSummary
        AttendanceSummaryDTO attendanceSummaryDTO = attendanceSummaryMapper.toDto(attendanceSummary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttendanceSummaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attendanceSummaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSummaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttendanceSummary in the database
        List<AttendanceSummary> attendanceSummaryList = attendanceSummaryRepository.findAll();
        assertThat(attendanceSummaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttendanceSummary() throws Exception {
        int databaseSizeBeforeUpdate = attendanceSummaryRepository.findAll().size();
        attendanceSummary.setId(count.incrementAndGet());

        // Create the AttendanceSummary
        AttendanceSummaryDTO attendanceSummaryDTO = attendanceSummaryMapper.toDto(attendanceSummary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceSummaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSummaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttendanceSummary in the database
        List<AttendanceSummary> attendanceSummaryList = attendanceSummaryRepository.findAll();
        assertThat(attendanceSummaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttendanceSummary() throws Exception {
        int databaseSizeBeforeUpdate = attendanceSummaryRepository.findAll().size();
        attendanceSummary.setId(count.incrementAndGet());

        // Create the AttendanceSummary
        AttendanceSummaryDTO attendanceSummaryDTO = attendanceSummaryMapper.toDto(attendanceSummary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceSummaryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attendanceSummaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttendanceSummary in the database
        List<AttendanceSummary> attendanceSummaryList = attendanceSummaryRepository.findAll();
        assertThat(attendanceSummaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttendanceSummaryWithPatch() throws Exception {
        // Initialize the database
        attendanceSummaryRepository.saveAndFlush(attendanceSummary);

        int databaseSizeBeforeUpdate = attendanceSummaryRepository.findAll().size();

        // Update the attendanceSummary using partial update
        AttendanceSummary partialUpdatedAttendanceSummary = new AttendanceSummary();
        partialUpdatedAttendanceSummary.setId(attendanceSummary.getId());

        partialUpdatedAttendanceSummary
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .totalWorkingDays(UPDATED_TOTAL_WORKING_DAYS)
            .totalAbsentDays(UPDATED_TOTAL_ABSENT_DAYS)
            .totalFractionDays(UPDATED_TOTAL_FRACTION_DAYS)
            .attendanceRegularisationStartDate(UPDATED_ATTENDANCE_REGULARISATION_START_DATE)
            .attendanceRegularisationEndDate(UPDATED_ATTENDANCE_REGULARISATION_END_DATE);

        restAttendanceSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttendanceSummary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttendanceSummary))
            )
            .andExpect(status().isOk());

        // Validate the AttendanceSummary in the database
        List<AttendanceSummary> attendanceSummaryList = attendanceSummaryRepository.findAll();
        assertThat(attendanceSummaryList).hasSize(databaseSizeBeforeUpdate);
        AttendanceSummary testAttendanceSummary = attendanceSummaryList.get(attendanceSummaryList.size() - 1);
        assertThat(testAttendanceSummary.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testAttendanceSummary.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testAttendanceSummary.getTotalWorkingDays()).isEqualTo(UPDATED_TOTAL_WORKING_DAYS);
        assertThat(testAttendanceSummary.getTotalLeaveDays()).isEqualTo(DEFAULT_TOTAL_LEAVE_DAYS);
        assertThat(testAttendanceSummary.getTotalAbsentDays()).isEqualTo(UPDATED_TOTAL_ABSENT_DAYS);
        assertThat(testAttendanceSummary.getTotalFractionDays()).isEqualTo(UPDATED_TOTAL_FRACTION_DAYS);
        assertThat(testAttendanceSummary.getAttendanceRegularisationStartDate()).isEqualTo(UPDATED_ATTENDANCE_REGULARISATION_START_DATE);
        assertThat(testAttendanceSummary.getAttendanceRegularisationEndDate()).isEqualTo(UPDATED_ATTENDANCE_REGULARISATION_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAttendanceSummaryWithPatch() throws Exception {
        // Initialize the database
        attendanceSummaryRepository.saveAndFlush(attendanceSummary);

        int databaseSizeBeforeUpdate = attendanceSummaryRepository.findAll().size();

        // Update the attendanceSummary using partial update
        AttendanceSummary partialUpdatedAttendanceSummary = new AttendanceSummary();
        partialUpdatedAttendanceSummary.setId(attendanceSummary.getId());

        partialUpdatedAttendanceSummary
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .totalWorkingDays(UPDATED_TOTAL_WORKING_DAYS)
            .totalLeaveDays(UPDATED_TOTAL_LEAVE_DAYS)
            .totalAbsentDays(UPDATED_TOTAL_ABSENT_DAYS)
            .totalFractionDays(UPDATED_TOTAL_FRACTION_DAYS)
            .attendanceRegularisationStartDate(UPDATED_ATTENDANCE_REGULARISATION_START_DATE)
            .attendanceRegularisationEndDate(UPDATED_ATTENDANCE_REGULARISATION_END_DATE);

        restAttendanceSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttendanceSummary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttendanceSummary))
            )
            .andExpect(status().isOk());

        // Validate the AttendanceSummary in the database
        List<AttendanceSummary> attendanceSummaryList = attendanceSummaryRepository.findAll();
        assertThat(attendanceSummaryList).hasSize(databaseSizeBeforeUpdate);
        AttendanceSummary testAttendanceSummary = attendanceSummaryList.get(attendanceSummaryList.size() - 1);
        assertThat(testAttendanceSummary.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testAttendanceSummary.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testAttendanceSummary.getTotalWorkingDays()).isEqualTo(UPDATED_TOTAL_WORKING_DAYS);
        assertThat(testAttendanceSummary.getTotalLeaveDays()).isEqualTo(UPDATED_TOTAL_LEAVE_DAYS);
        assertThat(testAttendanceSummary.getTotalAbsentDays()).isEqualTo(UPDATED_TOTAL_ABSENT_DAYS);
        assertThat(testAttendanceSummary.getTotalFractionDays()).isEqualTo(UPDATED_TOTAL_FRACTION_DAYS);
        assertThat(testAttendanceSummary.getAttendanceRegularisationStartDate()).isEqualTo(UPDATED_ATTENDANCE_REGULARISATION_START_DATE);
        assertThat(testAttendanceSummary.getAttendanceRegularisationEndDate()).isEqualTo(UPDATED_ATTENDANCE_REGULARISATION_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAttendanceSummary() throws Exception {
        int databaseSizeBeforeUpdate = attendanceSummaryRepository.findAll().size();
        attendanceSummary.setId(count.incrementAndGet());

        // Create the AttendanceSummary
        AttendanceSummaryDTO attendanceSummaryDTO = attendanceSummaryMapper.toDto(attendanceSummary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttendanceSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attendanceSummaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSummaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttendanceSummary in the database
        List<AttendanceSummary> attendanceSummaryList = attendanceSummaryRepository.findAll();
        assertThat(attendanceSummaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttendanceSummary() throws Exception {
        int databaseSizeBeforeUpdate = attendanceSummaryRepository.findAll().size();
        attendanceSummary.setId(count.incrementAndGet());

        // Create the AttendanceSummary
        AttendanceSummaryDTO attendanceSummaryDTO = attendanceSummaryMapper.toDto(attendanceSummary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSummaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttendanceSummary in the database
        List<AttendanceSummary> attendanceSummaryList = attendanceSummaryRepository.findAll();
        assertThat(attendanceSummaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttendanceSummary() throws Exception {
        int databaseSizeBeforeUpdate = attendanceSummaryRepository.findAll().size();
        attendanceSummary.setId(count.incrementAndGet());

        // Create the AttendanceSummary
        AttendanceSummaryDTO attendanceSummaryDTO = attendanceSummaryMapper.toDto(attendanceSummary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSummaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttendanceSummary in the database
        List<AttendanceSummary> attendanceSummaryList = attendanceSummaryRepository.findAll();
        assertThat(attendanceSummaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttendanceSummary() throws Exception {
        // Initialize the database
        attendanceSummaryRepository.saveAndFlush(attendanceSummary);

        int databaseSizeBeforeDelete = attendanceSummaryRepository.findAll().size();

        // Delete the attendanceSummary
        restAttendanceSummaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, attendanceSummary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttendanceSummary> attendanceSummaryList = attendanceSummaryRepository.findAll();
        assertThat(attendanceSummaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
