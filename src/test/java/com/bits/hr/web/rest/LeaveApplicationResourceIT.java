package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.service.LeaveApplicationService;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.mapper.LeaveApplicationMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link LeaveApplicationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LeaveApplicationResourceIT {

    private static final LocalDate DEFAULT_APPLICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APPLICATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LeaveType DEFAULT_LEAVE_TYPE = LeaveType.Mentionable_Annual_Leave;
    private static final LeaveType UPDATED_LEAVE_TYPE = LeaveType.Mentionable_Casual_Leave;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_LINE_MANAGER_APPROVED = false;
    private static final Boolean UPDATED_IS_LINE_MANAGER_APPROVED = true;

    private static final Boolean DEFAULT_IS_HR_APPROVED = false;
    private static final Boolean UPDATED_IS_HR_APPROVED = true;

    private static final Boolean DEFAULT_IS_REJECTED = false;
    private static final Boolean UPDATED_IS_REJECTED = true;

    private static final String DEFAULT_REJECTION_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_REJECTION_COMMENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_HALF_DAY = false;
    private static final Boolean UPDATED_IS_HALF_DAY = true;

    private static final Integer DEFAULT_DURATION_IN_DAY = 0;
    private static final Integer UPDATED_DURATION_IN_DAY = 1;

    private static final String DEFAULT_PHONE_NUMBER_ON_LEAVE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER_ON_LEAVE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_ON_LEAVE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_ON_LEAVE = "BBBBBBBBBB";

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Instant DEFAULT_SANCTIONED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SANCTIONED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/leave-applications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Mock
    private LeaveApplicationRepository leaveApplicationRepositoryMock;

    @Autowired
    private LeaveApplicationMapper leaveApplicationMapper;

    @Mock
    private LeaveApplicationService leaveApplicationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaveApplicationMockMvc;

    private LeaveApplication leaveApplication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveApplication createEntity(EntityManager em) {
        LeaveApplication leaveApplication = new LeaveApplication()
            .applicationDate(DEFAULT_APPLICATION_DATE)
            .leaveType(DEFAULT_LEAVE_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .isLineManagerApproved(DEFAULT_IS_LINE_MANAGER_APPROVED)
            .isHRApproved(DEFAULT_IS_HR_APPROVED)
            .isRejected(DEFAULT_IS_REJECTED)
            .rejectionComment(DEFAULT_REJECTION_COMMENT)
            .isHalfDay(DEFAULT_IS_HALF_DAY)
            .durationInDay(DEFAULT_DURATION_IN_DAY)
            .phoneNumberOnLeave(DEFAULT_PHONE_NUMBER_ON_LEAVE)
            .addressOnLeave(DEFAULT_ADDRESS_ON_LEAVE)
            .reason(DEFAULT_REASON)
            .sanctionedAt(DEFAULT_SANCTIONED_AT);
        return leaveApplication;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveApplication createUpdatedEntity(EntityManager em) {
        LeaveApplication leaveApplication = new LeaveApplication()
            .applicationDate(UPDATED_APPLICATION_DATE)
            .leaveType(UPDATED_LEAVE_TYPE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isLineManagerApproved(UPDATED_IS_LINE_MANAGER_APPROVED)
            .isHRApproved(UPDATED_IS_HR_APPROVED)
            .isRejected(UPDATED_IS_REJECTED)
            .rejectionComment(UPDATED_REJECTION_COMMENT)
            .isHalfDay(UPDATED_IS_HALF_DAY)
            .durationInDay(UPDATED_DURATION_IN_DAY)
            .phoneNumberOnLeave(UPDATED_PHONE_NUMBER_ON_LEAVE)
            .addressOnLeave(UPDATED_ADDRESS_ON_LEAVE)
            .reason(UPDATED_REASON)
            .sanctionedAt(UPDATED_SANCTIONED_AT);
        return leaveApplication;
    }

    @BeforeEach
    public void initTest() {
        leaveApplication = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaveApplication() throws Exception {
        int databaseSizeBeforeCreate = leaveApplicationRepository.findAll().size();
        // Create the LeaveApplication
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);
        restLeaveApplicationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaveApplication in the database
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeCreate + 1);
        LeaveApplication testLeaveApplication = leaveApplicationList.get(leaveApplicationList.size() - 1);
        assertThat(testLeaveApplication.getApplicationDate()).isEqualTo(DEFAULT_APPLICATION_DATE);
        assertThat(testLeaveApplication.getLeaveType()).isEqualTo(DEFAULT_LEAVE_TYPE);
        assertThat(testLeaveApplication.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLeaveApplication.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testLeaveApplication.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testLeaveApplication.getIsLineManagerApproved()).isEqualTo(DEFAULT_IS_LINE_MANAGER_APPROVED);
        assertThat(testLeaveApplication.getIsHRApproved()).isEqualTo(DEFAULT_IS_HR_APPROVED);
        assertThat(testLeaveApplication.getIsRejected()).isEqualTo(DEFAULT_IS_REJECTED);
        assertThat(testLeaveApplication.getRejectionComment()).isEqualTo(DEFAULT_REJECTION_COMMENT);
        assertThat(testLeaveApplication.getIsHalfDay()).isEqualTo(DEFAULT_IS_HALF_DAY);
        assertThat(testLeaveApplication.getDurationInDay()).isEqualTo(DEFAULT_DURATION_IN_DAY);
        assertThat(testLeaveApplication.getPhoneNumberOnLeave()).isEqualTo(DEFAULT_PHONE_NUMBER_ON_LEAVE);
        assertThat(testLeaveApplication.getAddressOnLeave()).isEqualTo(DEFAULT_ADDRESS_ON_LEAVE);
        assertThat(testLeaveApplication.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testLeaveApplication.getSanctionedAt()).isEqualTo(DEFAULT_SANCTIONED_AT);
    }

    @Test
    @Transactional
    void createLeaveApplicationWithExistingId() throws Exception {
        // Create the LeaveApplication with an existing ID
        leaveApplication.setId(1L);
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);

        int databaseSizeBeforeCreate = leaveApplicationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaveApplicationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveApplication in the database
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLeaveApplications() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList
        restLeaveApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationDate").value(hasItem(DEFAULT_APPLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].leaveType").value(hasItem(DEFAULT_LEAVE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isLineManagerApproved").value(hasItem(DEFAULT_IS_LINE_MANAGER_APPROVED.booleanValue())))
            .andExpect(jsonPath("$.[*].isHRApproved").value(hasItem(DEFAULT_IS_HR_APPROVED.booleanValue())))
            .andExpect(jsonPath("$.[*].isRejected").value(hasItem(DEFAULT_IS_REJECTED.booleanValue())))
            .andExpect(jsonPath("$.[*].rejectionComment").value(hasItem(DEFAULT_REJECTION_COMMENT)))
            .andExpect(jsonPath("$.[*].isHalfDay").value(hasItem(DEFAULT_IS_HALF_DAY.booleanValue())))
            .andExpect(jsonPath("$.[*].durationInDay").value(hasItem(DEFAULT_DURATION_IN_DAY)))
            .andExpect(jsonPath("$.[*].phoneNumberOnLeave").value(hasItem(DEFAULT_PHONE_NUMBER_ON_LEAVE)))
            .andExpect(jsonPath("$.[*].addressOnLeave").value(hasItem(DEFAULT_ADDRESS_ON_LEAVE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())))
            .andExpect(jsonPath("$.[*].sanctionedAt").value(hasItem(DEFAULT_SANCTIONED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLeaveApplicationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(leaveApplicationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLeaveApplicationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(leaveApplicationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLeaveApplicationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(leaveApplicationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLeaveApplicationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(leaveApplicationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLeaveApplication() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get the leaveApplication
        restLeaveApplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, leaveApplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaveApplication.getId().intValue()))
            .andExpect(jsonPath("$.applicationDate").value(DEFAULT_APPLICATION_DATE.toString()))
            .andExpect(jsonPath("$.leaveType").value(DEFAULT_LEAVE_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.isLineManagerApproved").value(DEFAULT_IS_LINE_MANAGER_APPROVED.booleanValue()))
            .andExpect(jsonPath("$.isHRApproved").value(DEFAULT_IS_HR_APPROVED.booleanValue()))
            .andExpect(jsonPath("$.isRejected").value(DEFAULT_IS_REJECTED.booleanValue()))
            .andExpect(jsonPath("$.rejectionComment").value(DEFAULT_REJECTION_COMMENT))
            .andExpect(jsonPath("$.isHalfDay").value(DEFAULT_IS_HALF_DAY.booleanValue()))
            .andExpect(jsonPath("$.durationInDay").value(DEFAULT_DURATION_IN_DAY))
            .andExpect(jsonPath("$.phoneNumberOnLeave").value(DEFAULT_PHONE_NUMBER_ON_LEAVE))
            .andExpect(jsonPath("$.addressOnLeave").value(DEFAULT_ADDRESS_ON_LEAVE.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()))
            .andExpect(jsonPath("$.sanctionedAt").value(DEFAULT_SANCTIONED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLeaveApplication() throws Exception {
        // Get the leaveApplication
        restLeaveApplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLeaveApplication() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        int databaseSizeBeforeUpdate = leaveApplicationRepository.findAll().size();

        // Update the leaveApplication
        LeaveApplication updatedLeaveApplication = leaveApplicationRepository.findById(leaveApplication.getId()).get();
        // Disconnect from session so that the updates on updatedLeaveApplication are not directly saved in db
        em.detach(updatedLeaveApplication);
        updatedLeaveApplication
            .applicationDate(UPDATED_APPLICATION_DATE)
            .leaveType(UPDATED_LEAVE_TYPE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isLineManagerApproved(UPDATED_IS_LINE_MANAGER_APPROVED)
            .isHRApproved(UPDATED_IS_HR_APPROVED)
            .isRejected(UPDATED_IS_REJECTED)
            .rejectionComment(UPDATED_REJECTION_COMMENT)
            .isHalfDay(UPDATED_IS_HALF_DAY)
            .durationInDay(UPDATED_DURATION_IN_DAY)
            .phoneNumberOnLeave(UPDATED_PHONE_NUMBER_ON_LEAVE)
            .addressOnLeave(UPDATED_ADDRESS_ON_LEAVE)
            .reason(UPDATED_REASON)
            .sanctionedAt(UPDATED_SANCTIONED_AT);
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(updatedLeaveApplication);

        restLeaveApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaveApplicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaveApplication in the database
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeUpdate);
        LeaveApplication testLeaveApplication = leaveApplicationList.get(leaveApplicationList.size() - 1);
        assertThat(testLeaveApplication.getApplicationDate()).isEqualTo(UPDATED_APPLICATION_DATE);
        assertThat(testLeaveApplication.getLeaveType()).isEqualTo(UPDATED_LEAVE_TYPE);
        assertThat(testLeaveApplication.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLeaveApplication.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLeaveApplication.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testLeaveApplication.getIsLineManagerApproved()).isEqualTo(UPDATED_IS_LINE_MANAGER_APPROVED);
        assertThat(testLeaveApplication.getIsHRApproved()).isEqualTo(UPDATED_IS_HR_APPROVED);
        assertThat(testLeaveApplication.getIsRejected()).isEqualTo(UPDATED_IS_REJECTED);
        assertThat(testLeaveApplication.getRejectionComment()).isEqualTo(UPDATED_REJECTION_COMMENT);
        assertThat(testLeaveApplication.getIsHalfDay()).isEqualTo(UPDATED_IS_HALF_DAY);
        assertThat(testLeaveApplication.getDurationInDay()).isEqualTo(UPDATED_DURATION_IN_DAY);
        assertThat(testLeaveApplication.getPhoneNumberOnLeave()).isEqualTo(UPDATED_PHONE_NUMBER_ON_LEAVE);
        assertThat(testLeaveApplication.getAddressOnLeave()).isEqualTo(UPDATED_ADDRESS_ON_LEAVE);
        assertThat(testLeaveApplication.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testLeaveApplication.getSanctionedAt()).isEqualTo(UPDATED_SANCTIONED_AT);
    }

    @Test
    @Transactional
    void putNonExistingLeaveApplication() throws Exception {
        int databaseSizeBeforeUpdate = leaveApplicationRepository.findAll().size();
        leaveApplication.setId(count.incrementAndGet());

        // Create the LeaveApplication
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaveApplicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveApplication in the database
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaveApplication() throws Exception {
        int databaseSizeBeforeUpdate = leaveApplicationRepository.findAll().size();
        leaveApplication.setId(count.incrementAndGet());

        // Create the LeaveApplication
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveApplication in the database
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaveApplication() throws Exception {
        int databaseSizeBeforeUpdate = leaveApplicationRepository.findAll().size();
        leaveApplication.setId(count.incrementAndGet());

        // Create the LeaveApplication
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveApplicationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaveApplication in the database
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLeaveApplicationWithPatch() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        int databaseSizeBeforeUpdate = leaveApplicationRepository.findAll().size();

        // Update the leaveApplication using partial update
        LeaveApplication partialUpdatedLeaveApplication = new LeaveApplication();
        partialUpdatedLeaveApplication.setId(leaveApplication.getId());

        partialUpdatedLeaveApplication
            .applicationDate(UPDATED_APPLICATION_DATE)
            .description(UPDATED_DESCRIPTION)
            .endDate(UPDATED_END_DATE)
            .isHRApproved(UPDATED_IS_HR_APPROVED)
            .rejectionComment(UPDATED_REJECTION_COMMENT)
            .durationInDay(UPDATED_DURATION_IN_DAY)
            .phoneNumberOnLeave(UPDATED_PHONE_NUMBER_ON_LEAVE)
            .addressOnLeave(UPDATED_ADDRESS_ON_LEAVE)
            .reason(UPDATED_REASON);

        restLeaveApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaveApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaveApplication))
            )
            .andExpect(status().isOk());

        // Validate the LeaveApplication in the database
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeUpdate);
        LeaveApplication testLeaveApplication = leaveApplicationList.get(leaveApplicationList.size() - 1);
        assertThat(testLeaveApplication.getApplicationDate()).isEqualTo(UPDATED_APPLICATION_DATE);
        assertThat(testLeaveApplication.getLeaveType()).isEqualTo(DEFAULT_LEAVE_TYPE);
        assertThat(testLeaveApplication.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLeaveApplication.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testLeaveApplication.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testLeaveApplication.getIsLineManagerApproved()).isEqualTo(DEFAULT_IS_LINE_MANAGER_APPROVED);
        assertThat(testLeaveApplication.getIsHRApproved()).isEqualTo(UPDATED_IS_HR_APPROVED);
        assertThat(testLeaveApplication.getIsRejected()).isEqualTo(DEFAULT_IS_REJECTED);
        assertThat(testLeaveApplication.getRejectionComment()).isEqualTo(UPDATED_REJECTION_COMMENT);
        assertThat(testLeaveApplication.getIsHalfDay()).isEqualTo(DEFAULT_IS_HALF_DAY);
        assertThat(testLeaveApplication.getDurationInDay()).isEqualTo(UPDATED_DURATION_IN_DAY);
        assertThat(testLeaveApplication.getPhoneNumberOnLeave()).isEqualTo(UPDATED_PHONE_NUMBER_ON_LEAVE);
        assertThat(testLeaveApplication.getAddressOnLeave()).isEqualTo(UPDATED_ADDRESS_ON_LEAVE);
        assertThat(testLeaveApplication.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testLeaveApplication.getSanctionedAt()).isEqualTo(DEFAULT_SANCTIONED_AT);
    }

    @Test
    @Transactional
    void fullUpdateLeaveApplicationWithPatch() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        int databaseSizeBeforeUpdate = leaveApplicationRepository.findAll().size();

        // Update the leaveApplication using partial update
        LeaveApplication partialUpdatedLeaveApplication = new LeaveApplication();
        partialUpdatedLeaveApplication.setId(leaveApplication.getId());

        partialUpdatedLeaveApplication
            .applicationDate(UPDATED_APPLICATION_DATE)
            .leaveType(UPDATED_LEAVE_TYPE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isLineManagerApproved(UPDATED_IS_LINE_MANAGER_APPROVED)
            .isHRApproved(UPDATED_IS_HR_APPROVED)
            .isRejected(UPDATED_IS_REJECTED)
            .rejectionComment(UPDATED_REJECTION_COMMENT)
            .isHalfDay(UPDATED_IS_HALF_DAY)
            .durationInDay(UPDATED_DURATION_IN_DAY)
            .phoneNumberOnLeave(UPDATED_PHONE_NUMBER_ON_LEAVE)
            .addressOnLeave(UPDATED_ADDRESS_ON_LEAVE)
            .reason(UPDATED_REASON)
            .sanctionedAt(UPDATED_SANCTIONED_AT);

        restLeaveApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaveApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaveApplication))
            )
            .andExpect(status().isOk());

        // Validate the LeaveApplication in the database
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeUpdate);
        LeaveApplication testLeaveApplication = leaveApplicationList.get(leaveApplicationList.size() - 1);
        assertThat(testLeaveApplication.getApplicationDate()).isEqualTo(UPDATED_APPLICATION_DATE);
        assertThat(testLeaveApplication.getLeaveType()).isEqualTo(UPDATED_LEAVE_TYPE);
        assertThat(testLeaveApplication.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLeaveApplication.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLeaveApplication.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testLeaveApplication.getIsLineManagerApproved()).isEqualTo(UPDATED_IS_LINE_MANAGER_APPROVED);
        assertThat(testLeaveApplication.getIsHRApproved()).isEqualTo(UPDATED_IS_HR_APPROVED);
        assertThat(testLeaveApplication.getIsRejected()).isEqualTo(UPDATED_IS_REJECTED);
        assertThat(testLeaveApplication.getRejectionComment()).isEqualTo(UPDATED_REJECTION_COMMENT);
        assertThat(testLeaveApplication.getIsHalfDay()).isEqualTo(UPDATED_IS_HALF_DAY);
        assertThat(testLeaveApplication.getDurationInDay()).isEqualTo(UPDATED_DURATION_IN_DAY);
        assertThat(testLeaveApplication.getPhoneNumberOnLeave()).isEqualTo(UPDATED_PHONE_NUMBER_ON_LEAVE);
        assertThat(testLeaveApplication.getAddressOnLeave()).isEqualTo(UPDATED_ADDRESS_ON_LEAVE);
        assertThat(testLeaveApplication.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testLeaveApplication.getSanctionedAt()).isEqualTo(UPDATED_SANCTIONED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingLeaveApplication() throws Exception {
        int databaseSizeBeforeUpdate = leaveApplicationRepository.findAll().size();
        leaveApplication.setId(count.incrementAndGet());

        // Create the LeaveApplication
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaveApplicationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveApplication in the database
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaveApplication() throws Exception {
        int databaseSizeBeforeUpdate = leaveApplicationRepository.findAll().size();
        leaveApplication.setId(count.incrementAndGet());

        // Create the LeaveApplication
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveApplication in the database
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaveApplication() throws Exception {
        int databaseSizeBeforeUpdate = leaveApplicationRepository.findAll().size();
        leaveApplication.setId(count.incrementAndGet());

        // Create the LeaveApplication
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaveApplication in the database
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLeaveApplication() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        int databaseSizeBeforeDelete = leaveApplicationRepository.findAll().size();

        // Delete the leaveApplication
        restLeaveApplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaveApplication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
