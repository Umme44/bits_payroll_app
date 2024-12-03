package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.RoomRequisition;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.RoomRequisitionRepository;
import com.bits.hr.service.RoomRequisitionService;
import com.bits.hr.service.dto.RoomRequisitionDTO;
import com.bits.hr.service.mapper.RoomRequisitionMapper;
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
 * Integration tests for the {@link RoomRequisitionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RoomRequisitionResourceIT {

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.APPROVED;

    private static final String DEFAULT_BOOKING_TRN = "AAAAAAAAAA";
    private static final String UPDATED_BOOKING_TRN = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SANCTIONED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SANCTIONED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PARTICIPANT_LIST = "AAAAAAAAAA";
    private static final String UPDATED_PARTICIPANT_LIST = "BBBBBBBBBB";

    private static final String DEFAULT_REJECTED_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REJECTED_REASON = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BOOKING_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BOOKING_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_BOOKING_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BOOKING_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_START_TIME = 0D;
    private static final Double UPDATED_START_TIME = 1D;

    private static final Double DEFAULT_END_TIME = 0D;
    private static final Double UPDATED_END_TIME = 1D;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_AGENDA = "AAAAAAAAAA";
    private static final String UPDATED_AGENDA = "BBBBBBBBBB";

    private static final String DEFAULT_OPTIONAL_PARTICIPANT_LIST = "AAAAAAAAAA";
    private static final String UPDATED_OPTIONAL_PARTICIPANT_LIST = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_FULL_DAY = false;
    private static final Boolean UPDATED_IS_FULL_DAY = true;

    private static final String ENTITY_API_URL = "/api/room-requisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoomRequisitionRepository roomRequisitionRepository;

    @Mock
    private RoomRequisitionRepository roomRequisitionRepositoryMock;

    @Autowired
    private RoomRequisitionMapper roomRequisitionMapper;

    @Mock
    private RoomRequisitionService roomRequisitionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomRequisitionMockMvc;

    private RoomRequisition roomRequisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomRequisition createEntity(EntityManager em) {
        RoomRequisition roomRequisition = new RoomRequisition()
            .status(DEFAULT_STATUS)
            .bookingTrn(DEFAULT_BOOKING_TRN)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .sanctionedAt(DEFAULT_SANCTIONED_AT)
            .participantList(DEFAULT_PARTICIPANT_LIST)
            .rejectedReason(DEFAULT_REJECTED_REASON)
            .bookingStartDate(DEFAULT_BOOKING_START_DATE)
            .bookingEndDate(DEFAULT_BOOKING_END_DATE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .title(DEFAULT_TITLE)
            .agenda(DEFAULT_AGENDA)
            .optionalParticipantList(DEFAULT_OPTIONAL_PARTICIPANT_LIST)
            .isFullDay(DEFAULT_IS_FULL_DAY);
        return roomRequisition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomRequisition createUpdatedEntity(EntityManager em) {
        RoomRequisition roomRequisition = new RoomRequisition()
            .status(UPDATED_STATUS)
            .bookingTrn(UPDATED_BOOKING_TRN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .sanctionedAt(UPDATED_SANCTIONED_AT)
            .participantList(UPDATED_PARTICIPANT_LIST)
            .rejectedReason(UPDATED_REJECTED_REASON)
            .bookingStartDate(UPDATED_BOOKING_START_DATE)
            .bookingEndDate(UPDATED_BOOKING_END_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .title(UPDATED_TITLE)
            .agenda(UPDATED_AGENDA)
            .optionalParticipantList(UPDATED_OPTIONAL_PARTICIPANT_LIST)
            .isFullDay(UPDATED_IS_FULL_DAY);
        return roomRequisition;
    }

    @BeforeEach
    public void initTest() {
        roomRequisition = createEntity(em);
    }

    @Test
    @Transactional
    void createRoomRequisition() throws Exception {
        int databaseSizeBeforeCreate = roomRequisitionRepository.findAll().size();
        // Create the RoomRequisition
        RoomRequisitionDTO roomRequisitionDTO = roomRequisitionMapper.toDto(roomRequisition);
        restRoomRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomRequisitionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RoomRequisition in the database
        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeCreate + 1);
        RoomRequisition testRoomRequisition = roomRequisitionList.get(roomRequisitionList.size() - 1);
        assertThat(testRoomRequisition.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRoomRequisition.getBookingTrn()).isEqualTo(DEFAULT_BOOKING_TRN);
        assertThat(testRoomRequisition.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testRoomRequisition.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testRoomRequisition.getSanctionedAt()).isEqualTo(DEFAULT_SANCTIONED_AT);
        assertThat(testRoomRequisition.getParticipantList()).isEqualTo(DEFAULT_PARTICIPANT_LIST);
        assertThat(testRoomRequisition.getRejectedReason()).isEqualTo(DEFAULT_REJECTED_REASON);
        assertThat(testRoomRequisition.getBookingStartDate()).isEqualTo(DEFAULT_BOOKING_START_DATE);
        assertThat(testRoomRequisition.getBookingEndDate()).isEqualTo(DEFAULT_BOOKING_END_DATE);
        assertThat(testRoomRequisition.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testRoomRequisition.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testRoomRequisition.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRoomRequisition.getAgenda()).isEqualTo(DEFAULT_AGENDA);
        assertThat(testRoomRequisition.getOptionalParticipantList()).isEqualTo(DEFAULT_OPTIONAL_PARTICIPANT_LIST);
        assertThat(testRoomRequisition.getIsFullDay()).isEqualTo(DEFAULT_IS_FULL_DAY);
    }

    @Test
    @Transactional
    void createRoomRequisitionWithExistingId() throws Exception {
        // Create the RoomRequisition with an existing ID
        roomRequisition.setId(1L);
        RoomRequisitionDTO roomRequisitionDTO = roomRequisitionMapper.toDto(roomRequisition);

        int databaseSizeBeforeCreate = roomRequisitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomRequisition in the database
        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomRequisitionRepository.findAll().size();
        // set the field null
        roomRequisition.setStatus(null);

        // Create the RoomRequisition, which fails.
        RoomRequisitionDTO roomRequisitionDTO = roomRequisitionMapper.toDto(roomRequisition);

        restRoomRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBookingStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomRequisitionRepository.findAll().size();
        // set the field null
        roomRequisition.setBookingStartDate(null);

        // Create the RoomRequisition, which fails.
        RoomRequisitionDTO roomRequisitionDTO = roomRequisitionMapper.toDto(roomRequisition);

        restRoomRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBookingEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomRequisitionRepository.findAll().size();
        // set the field null
        roomRequisition.setBookingEndDate(null);

        // Create the RoomRequisition, which fails.
        RoomRequisitionDTO roomRequisitionDTO = roomRequisitionMapper.toDto(roomRequisition);

        restRoomRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomRequisitionRepository.findAll().size();
        // set the field null
        roomRequisition.setStartTime(null);

        // Create the RoomRequisition, which fails.
        RoomRequisitionDTO roomRequisitionDTO = roomRequisitionMapper.toDto(roomRequisition);

        restRoomRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomRequisitionRepository.findAll().size();
        // set the field null
        roomRequisition.setEndTime(null);

        // Create the RoomRequisition, which fails.
        RoomRequisitionDTO roomRequisitionDTO = roomRequisitionMapper.toDto(roomRequisition);

        restRoomRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomRequisitionRepository.findAll().size();
        // set the field null
        roomRequisition.setTitle(null);

        // Create the RoomRequisition, which fails.
        RoomRequisitionDTO roomRequisitionDTO = roomRequisitionMapper.toDto(roomRequisition);

        restRoomRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRoomRequisitions() throws Exception {
        // Initialize the database
        roomRequisitionRepository.saveAndFlush(roomRequisition);

        // Get all the roomRequisitionList
        restRoomRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].bookingTrn").value(hasItem(DEFAULT_BOOKING_TRN)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].sanctionedAt").value(hasItem(DEFAULT_SANCTIONED_AT.toString())))
            .andExpect(jsonPath("$.[*].participantList").value(hasItem(DEFAULT_PARTICIPANT_LIST.toString())))
            .andExpect(jsonPath("$.[*].rejectedReason").value(hasItem(DEFAULT_REJECTED_REASON)))
            .andExpect(jsonPath("$.[*].bookingStartDate").value(hasItem(DEFAULT_BOOKING_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].bookingEndDate").value(hasItem(DEFAULT_BOOKING_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].agenda").value(hasItem(DEFAULT_AGENDA)))
            .andExpect(jsonPath("$.[*].optionalParticipantList").value(hasItem(DEFAULT_OPTIONAL_PARTICIPANT_LIST.toString())))
            .andExpect(jsonPath("$.[*].isFullDay").value(hasItem(DEFAULT_IS_FULL_DAY.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRoomRequisitionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(roomRequisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRoomRequisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(roomRequisitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRoomRequisitionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(roomRequisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRoomRequisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(roomRequisitionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRoomRequisition() throws Exception {
        // Initialize the database
        roomRequisitionRepository.saveAndFlush(roomRequisition);

        // Get the roomRequisition
        restRoomRequisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, roomRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roomRequisition.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.bookingTrn").value(DEFAULT_BOOKING_TRN))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.sanctionedAt").value(DEFAULT_SANCTIONED_AT.toString()))
            .andExpect(jsonPath("$.participantList").value(DEFAULT_PARTICIPANT_LIST.toString()))
            .andExpect(jsonPath("$.rejectedReason").value(DEFAULT_REJECTED_REASON))
            .andExpect(jsonPath("$.bookingStartDate").value(DEFAULT_BOOKING_START_DATE.toString()))
            .andExpect(jsonPath("$.bookingEndDate").value(DEFAULT_BOOKING_END_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.doubleValue()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.doubleValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.agenda").value(DEFAULT_AGENDA))
            .andExpect(jsonPath("$.optionalParticipantList").value(DEFAULT_OPTIONAL_PARTICIPANT_LIST.toString()))
            .andExpect(jsonPath("$.isFullDay").value(DEFAULT_IS_FULL_DAY.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingRoomRequisition() throws Exception {
        // Get the roomRequisition
        restRoomRequisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoomRequisition() throws Exception {
        // Initialize the database
        roomRequisitionRepository.saveAndFlush(roomRequisition);

        int databaseSizeBeforeUpdate = roomRequisitionRepository.findAll().size();

        // Update the roomRequisition
        RoomRequisition updatedRoomRequisition = roomRequisitionRepository.findById(roomRequisition.getId()).get();
        // Disconnect from session so that the updates on updatedRoomRequisition are not directly saved in db
        em.detach(updatedRoomRequisition);
        updatedRoomRequisition
            .status(UPDATED_STATUS)
            .bookingTrn(UPDATED_BOOKING_TRN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .sanctionedAt(UPDATED_SANCTIONED_AT)
            .participantList(UPDATED_PARTICIPANT_LIST)
            .rejectedReason(UPDATED_REJECTED_REASON)
            .bookingStartDate(UPDATED_BOOKING_START_DATE)
            .bookingEndDate(UPDATED_BOOKING_END_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .title(UPDATED_TITLE)
            .agenda(UPDATED_AGENDA)
            .optionalParticipantList(UPDATED_OPTIONAL_PARTICIPANT_LIST)
            .isFullDay(UPDATED_IS_FULL_DAY);
        RoomRequisitionDTO roomRequisitionDTO = roomRequisitionMapper.toDto(updatedRoomRequisition);

        restRoomRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomRequisitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the RoomRequisition in the database
        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeUpdate);
        RoomRequisition testRoomRequisition = roomRequisitionList.get(roomRequisitionList.size() - 1);
        assertThat(testRoomRequisition.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRoomRequisition.getBookingTrn()).isEqualTo(UPDATED_BOOKING_TRN);
        assertThat(testRoomRequisition.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRoomRequisition.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testRoomRequisition.getSanctionedAt()).isEqualTo(UPDATED_SANCTIONED_AT);
        assertThat(testRoomRequisition.getParticipantList()).isEqualTo(UPDATED_PARTICIPANT_LIST);
        assertThat(testRoomRequisition.getRejectedReason()).isEqualTo(UPDATED_REJECTED_REASON);
        assertThat(testRoomRequisition.getBookingStartDate()).isEqualTo(UPDATED_BOOKING_START_DATE);
        assertThat(testRoomRequisition.getBookingEndDate()).isEqualTo(UPDATED_BOOKING_END_DATE);
        assertThat(testRoomRequisition.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testRoomRequisition.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testRoomRequisition.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRoomRequisition.getAgenda()).isEqualTo(UPDATED_AGENDA);
        assertThat(testRoomRequisition.getOptionalParticipantList()).isEqualTo(UPDATED_OPTIONAL_PARTICIPANT_LIST);
        assertThat(testRoomRequisition.getIsFullDay()).isEqualTo(UPDATED_IS_FULL_DAY);
    }

    @Test
    @Transactional
    void putNonExistingRoomRequisition() throws Exception {
        int databaseSizeBeforeUpdate = roomRequisitionRepository.findAll().size();
        roomRequisition.setId(count.incrementAndGet());

        // Create the RoomRequisition
        RoomRequisitionDTO roomRequisitionDTO = roomRequisitionMapper.toDto(roomRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomRequisition in the database
        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoomRequisition() throws Exception {
        int databaseSizeBeforeUpdate = roomRequisitionRepository.findAll().size();
        roomRequisition.setId(count.incrementAndGet());

        // Create the RoomRequisition
        RoomRequisitionDTO roomRequisitionDTO = roomRequisitionMapper.toDto(roomRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomRequisition in the database
        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoomRequisition() throws Exception {
        int databaseSizeBeforeUpdate = roomRequisitionRepository.findAll().size();
        roomRequisition.setId(count.incrementAndGet());

        // Create the RoomRequisition
        RoomRequisitionDTO roomRequisitionDTO = roomRequisitionMapper.toDto(roomRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomRequisition in the database
        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomRequisitionWithPatch() throws Exception {
        // Initialize the database
        roomRequisitionRepository.saveAndFlush(roomRequisition);

        int databaseSizeBeforeUpdate = roomRequisitionRepository.findAll().size();

        // Update the roomRequisition using partial update
        RoomRequisition partialUpdatedRoomRequisition = new RoomRequisition();
        partialUpdatedRoomRequisition.setId(roomRequisition.getId());

        partialUpdatedRoomRequisition
            .status(UPDATED_STATUS)
            .bookingTrn(UPDATED_BOOKING_TRN)
            .createdAt(UPDATED_CREATED_AT)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .agenda(UPDATED_AGENDA)
            .optionalParticipantList(UPDATED_OPTIONAL_PARTICIPANT_LIST);

        restRoomRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomRequisition))
            )
            .andExpect(status().isOk());

        // Validate the RoomRequisition in the database
        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeUpdate);
        RoomRequisition testRoomRequisition = roomRequisitionList.get(roomRequisitionList.size() - 1);
        assertThat(testRoomRequisition.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRoomRequisition.getBookingTrn()).isEqualTo(UPDATED_BOOKING_TRN);
        assertThat(testRoomRequisition.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRoomRequisition.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testRoomRequisition.getSanctionedAt()).isEqualTo(DEFAULT_SANCTIONED_AT);
        assertThat(testRoomRequisition.getParticipantList()).isEqualTo(DEFAULT_PARTICIPANT_LIST);
        assertThat(testRoomRequisition.getRejectedReason()).isEqualTo(DEFAULT_REJECTED_REASON);
        assertThat(testRoomRequisition.getBookingStartDate()).isEqualTo(DEFAULT_BOOKING_START_DATE);
        assertThat(testRoomRequisition.getBookingEndDate()).isEqualTo(DEFAULT_BOOKING_END_DATE);
        assertThat(testRoomRequisition.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testRoomRequisition.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testRoomRequisition.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRoomRequisition.getAgenda()).isEqualTo(UPDATED_AGENDA);
        assertThat(testRoomRequisition.getOptionalParticipantList()).isEqualTo(UPDATED_OPTIONAL_PARTICIPANT_LIST);
        assertThat(testRoomRequisition.getIsFullDay()).isEqualTo(DEFAULT_IS_FULL_DAY);
    }

    @Test
    @Transactional
    void fullUpdateRoomRequisitionWithPatch() throws Exception {
        // Initialize the database
        roomRequisitionRepository.saveAndFlush(roomRequisition);

        int databaseSizeBeforeUpdate = roomRequisitionRepository.findAll().size();

        // Update the roomRequisition using partial update
        RoomRequisition partialUpdatedRoomRequisition = new RoomRequisition();
        partialUpdatedRoomRequisition.setId(roomRequisition.getId());

        partialUpdatedRoomRequisition
            .status(UPDATED_STATUS)
            .bookingTrn(UPDATED_BOOKING_TRN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .sanctionedAt(UPDATED_SANCTIONED_AT)
            .participantList(UPDATED_PARTICIPANT_LIST)
            .rejectedReason(UPDATED_REJECTED_REASON)
            .bookingStartDate(UPDATED_BOOKING_START_DATE)
            .bookingEndDate(UPDATED_BOOKING_END_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .title(UPDATED_TITLE)
            .agenda(UPDATED_AGENDA)
            .optionalParticipantList(UPDATED_OPTIONAL_PARTICIPANT_LIST)
            .isFullDay(UPDATED_IS_FULL_DAY);

        restRoomRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomRequisition))
            )
            .andExpect(status().isOk());

        // Validate the RoomRequisition in the database
        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeUpdate);
        RoomRequisition testRoomRequisition = roomRequisitionList.get(roomRequisitionList.size() - 1);
        assertThat(testRoomRequisition.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRoomRequisition.getBookingTrn()).isEqualTo(UPDATED_BOOKING_TRN);
        assertThat(testRoomRequisition.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRoomRequisition.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testRoomRequisition.getSanctionedAt()).isEqualTo(UPDATED_SANCTIONED_AT);
        assertThat(testRoomRequisition.getParticipantList()).isEqualTo(UPDATED_PARTICIPANT_LIST);
        assertThat(testRoomRequisition.getRejectedReason()).isEqualTo(UPDATED_REJECTED_REASON);
        assertThat(testRoomRequisition.getBookingStartDate()).isEqualTo(UPDATED_BOOKING_START_DATE);
        assertThat(testRoomRequisition.getBookingEndDate()).isEqualTo(UPDATED_BOOKING_END_DATE);
        assertThat(testRoomRequisition.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testRoomRequisition.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testRoomRequisition.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRoomRequisition.getAgenda()).isEqualTo(UPDATED_AGENDA);
        assertThat(testRoomRequisition.getOptionalParticipantList()).isEqualTo(UPDATED_OPTIONAL_PARTICIPANT_LIST);
        assertThat(testRoomRequisition.getIsFullDay()).isEqualTo(UPDATED_IS_FULL_DAY);
    }

    @Test
    @Transactional
    void patchNonExistingRoomRequisition() throws Exception {
        int databaseSizeBeforeUpdate = roomRequisitionRepository.findAll().size();
        roomRequisition.setId(count.incrementAndGet());

        // Create the RoomRequisition
        RoomRequisitionDTO roomRequisitionDTO = roomRequisitionMapper.toDto(roomRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomRequisitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomRequisition in the database
        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoomRequisition() throws Exception {
        int databaseSizeBeforeUpdate = roomRequisitionRepository.findAll().size();
        roomRequisition.setId(count.incrementAndGet());

        // Create the RoomRequisition
        RoomRequisitionDTO roomRequisitionDTO = roomRequisitionMapper.toDto(roomRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomRequisition in the database
        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoomRequisition() throws Exception {
        int databaseSizeBeforeUpdate = roomRequisitionRepository.findAll().size();
        roomRequisition.setId(count.incrementAndGet());

        // Create the RoomRequisition
        RoomRequisitionDTO roomRequisitionDTO = roomRequisitionMapper.toDto(roomRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomRequisition in the database
        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoomRequisition() throws Exception {
        // Initialize the database
        roomRequisitionRepository.saveAndFlush(roomRequisition);

        int databaseSizeBeforeDelete = roomRequisitionRepository.findAll().size();

        // Delete the roomRequisition
        restRoomRequisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, roomRequisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.findAll();
        assertThat(roomRequisitionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
