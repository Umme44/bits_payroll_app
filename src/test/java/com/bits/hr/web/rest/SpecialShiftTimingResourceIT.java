package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.SpecialShiftTiming;
import com.bits.hr.domain.TimeSlot;
import com.bits.hr.repository.SpecialShiftTimingRepository;
import com.bits.hr.service.SpecialShiftTimingService;
import com.bits.hr.service.dto.SpecialShiftTimingDTO;
import com.bits.hr.service.mapper.SpecialShiftTimingMapper;
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
 * Integration tests for the {@link SpecialShiftTimingResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SpecialShiftTimingResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_OVERRIDE_ROASTER = false;
    private static final Boolean UPDATED_OVERRIDE_ROASTER = true;

    private static final Boolean DEFAULT_OVERRIDE_FLEX_SCHEDULE = false;
    private static final Boolean UPDATED_OVERRIDE_FLEX_SCHEDULE = true;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/special-shift-timings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecialShiftTimingRepository specialShiftTimingRepository;

    @Mock
    private SpecialShiftTimingRepository specialShiftTimingRepositoryMock;

    @Autowired
    private SpecialShiftTimingMapper specialShiftTimingMapper;

    @Mock
    private SpecialShiftTimingService specialShiftTimingServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialShiftTimingMockMvc;

    private SpecialShiftTiming specialShiftTiming;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialShiftTiming createEntity(EntityManager em) {
        SpecialShiftTiming specialShiftTiming = new SpecialShiftTiming()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .overrideRoaster(DEFAULT_OVERRIDE_ROASTER)
            .overrideFlexSchedule(DEFAULT_OVERRIDE_FLEX_SCHEDULE)
            .remarks(DEFAULT_REMARKS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .reason(DEFAULT_REASON);
        // Add required entity
        TimeSlot timeSlot;
        if (TestUtil.findAll(em, TimeSlot.class).isEmpty()) {
            timeSlot = TimeSlotResourceIT.createEntity(em);
            em.persist(timeSlot);
            em.flush();
        } else {
            timeSlot = TestUtil.findAll(em, TimeSlot.class).get(0);
        }
        specialShiftTiming.setTimeSlot(timeSlot);
        return specialShiftTiming;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialShiftTiming createUpdatedEntity(EntityManager em) {
        SpecialShiftTiming specialShiftTiming = new SpecialShiftTiming()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .overrideRoaster(UPDATED_OVERRIDE_ROASTER)
            .overrideFlexSchedule(UPDATED_OVERRIDE_FLEX_SCHEDULE)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .reason(UPDATED_REASON);
        // Add required entity
        TimeSlot timeSlot;
        if (TestUtil.findAll(em, TimeSlot.class).isEmpty()) {
            timeSlot = TimeSlotResourceIT.createUpdatedEntity(em);
            em.persist(timeSlot);
            em.flush();
        } else {
            timeSlot = TestUtil.findAll(em, TimeSlot.class).get(0);
        }
        specialShiftTiming.setTimeSlot(timeSlot);
        return specialShiftTiming;
    }

    @BeforeEach
    public void initTest() {
        specialShiftTiming = createEntity(em);
    }

    @Test
    @Transactional
    void createSpecialShiftTiming() throws Exception {
        int databaseSizeBeforeCreate = specialShiftTimingRepository.findAll().size();
        // Create the SpecialShiftTiming
        SpecialShiftTimingDTO specialShiftTimingDTO = specialShiftTimingMapper.toDto(specialShiftTiming);
        restSpecialShiftTimingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialShiftTimingDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SpecialShiftTiming in the database
        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeCreate + 1);
        SpecialShiftTiming testSpecialShiftTiming = specialShiftTimingList.get(specialShiftTimingList.size() - 1);
        assertThat(testSpecialShiftTiming.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSpecialShiftTiming.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSpecialShiftTiming.getOverrideRoaster()).isEqualTo(DEFAULT_OVERRIDE_ROASTER);
        assertThat(testSpecialShiftTiming.getOverrideFlexSchedule()).isEqualTo(DEFAULT_OVERRIDE_FLEX_SCHEDULE);
        assertThat(testSpecialShiftTiming.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testSpecialShiftTiming.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSpecialShiftTiming.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSpecialShiftTiming.getReason()).isEqualTo(DEFAULT_REASON);
    }

    @Test
    @Transactional
    void createSpecialShiftTimingWithExistingId() throws Exception {
        // Create the SpecialShiftTiming with an existing ID
        specialShiftTiming.setId(1L);
        SpecialShiftTimingDTO specialShiftTimingDTO = specialShiftTimingMapper.toDto(specialShiftTiming);

        int databaseSizeBeforeCreate = specialShiftTimingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialShiftTimingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialShiftTimingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialShiftTiming in the database
        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialShiftTimingRepository.findAll().size();
        // set the field null
        specialShiftTiming.setStartDate(null);

        // Create the SpecialShiftTiming, which fails.
        SpecialShiftTimingDTO specialShiftTimingDTO = specialShiftTimingMapper.toDto(specialShiftTiming);

        restSpecialShiftTimingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialShiftTimingDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialShiftTimingRepository.findAll().size();
        // set the field null
        specialShiftTiming.setEndDate(null);

        // Create the SpecialShiftTiming, which fails.
        SpecialShiftTimingDTO specialShiftTimingDTO = specialShiftTimingMapper.toDto(specialShiftTiming);

        restSpecialShiftTimingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialShiftTimingDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOverrideRoasterIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialShiftTimingRepository.findAll().size();
        // set the field null
        specialShiftTiming.setOverrideRoaster(null);

        // Create the SpecialShiftTiming, which fails.
        SpecialShiftTimingDTO specialShiftTimingDTO = specialShiftTimingMapper.toDto(specialShiftTiming);

        restSpecialShiftTimingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialShiftTimingDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOverrideFlexScheduleIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialShiftTimingRepository.findAll().size();
        // set the field null
        specialShiftTiming.setOverrideFlexSchedule(null);

        // Create the SpecialShiftTiming, which fails.
        SpecialShiftTimingDTO specialShiftTimingDTO = specialShiftTimingMapper.toDto(specialShiftTiming);

        restSpecialShiftTimingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialShiftTimingDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecialShiftTimings() throws Exception {
        // Initialize the database
        specialShiftTimingRepository.saveAndFlush(specialShiftTiming);

        // Get all the specialShiftTimingList
        restSpecialShiftTimingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialShiftTiming.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].overrideRoaster").value(hasItem(DEFAULT_OVERRIDE_ROASTER.booleanValue())))
            .andExpect(jsonPath("$.[*].overrideFlexSchedule").value(hasItem(DEFAULT_OVERRIDE_FLEX_SCHEDULE.booleanValue())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSpecialShiftTimingsWithEagerRelationshipsIsEnabled() throws Exception {
        when(specialShiftTimingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpecialShiftTimingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(specialShiftTimingServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSpecialShiftTimingsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(specialShiftTimingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpecialShiftTimingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(specialShiftTimingRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSpecialShiftTiming() throws Exception {
        // Initialize the database
        specialShiftTimingRepository.saveAndFlush(specialShiftTiming);

        // Get the specialShiftTiming
        restSpecialShiftTimingMockMvc
            .perform(get(ENTITY_API_URL_ID, specialShiftTiming.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialShiftTiming.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.overrideRoaster").value(DEFAULT_OVERRIDE_ROASTER.booleanValue()))
            .andExpect(jsonPath("$.overrideFlexSchedule").value(DEFAULT_OVERRIDE_FLEX_SCHEDULE.booleanValue()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON));
    }

    @Test
    @Transactional
    void getNonExistingSpecialShiftTiming() throws Exception {
        // Get the specialShiftTiming
        restSpecialShiftTimingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialShiftTiming() throws Exception {
        // Initialize the database
        specialShiftTimingRepository.saveAndFlush(specialShiftTiming);

        int databaseSizeBeforeUpdate = specialShiftTimingRepository.findAll().size();

        // Update the specialShiftTiming
        SpecialShiftTiming updatedSpecialShiftTiming = specialShiftTimingRepository.findById(specialShiftTiming.getId()).get();
        // Disconnect from session so that the updates on updatedSpecialShiftTiming are not directly saved in db
        em.detach(updatedSpecialShiftTiming);
        updatedSpecialShiftTiming
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .overrideRoaster(UPDATED_OVERRIDE_ROASTER)
            .overrideFlexSchedule(UPDATED_OVERRIDE_FLEX_SCHEDULE)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .reason(UPDATED_REASON);
        SpecialShiftTimingDTO specialShiftTimingDTO = specialShiftTimingMapper.toDto(updatedSpecialShiftTiming);

        restSpecialShiftTimingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialShiftTimingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialShiftTimingDTO))
            )
            .andExpect(status().isOk());

        // Validate the SpecialShiftTiming in the database
        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeUpdate);
        SpecialShiftTiming testSpecialShiftTiming = specialShiftTimingList.get(specialShiftTimingList.size() - 1);
        assertThat(testSpecialShiftTiming.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSpecialShiftTiming.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSpecialShiftTiming.getOverrideRoaster()).isEqualTo(UPDATED_OVERRIDE_ROASTER);
        assertThat(testSpecialShiftTiming.getOverrideFlexSchedule()).isEqualTo(UPDATED_OVERRIDE_FLEX_SCHEDULE);
        assertThat(testSpecialShiftTiming.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testSpecialShiftTiming.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSpecialShiftTiming.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSpecialShiftTiming.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void putNonExistingSpecialShiftTiming() throws Exception {
        int databaseSizeBeforeUpdate = specialShiftTimingRepository.findAll().size();
        specialShiftTiming.setId(count.incrementAndGet());

        // Create the SpecialShiftTiming
        SpecialShiftTimingDTO specialShiftTimingDTO = specialShiftTimingMapper.toDto(specialShiftTiming);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialShiftTimingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialShiftTimingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialShiftTimingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialShiftTiming in the database
        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialShiftTiming() throws Exception {
        int databaseSizeBeforeUpdate = specialShiftTimingRepository.findAll().size();
        specialShiftTiming.setId(count.incrementAndGet());

        // Create the SpecialShiftTiming
        SpecialShiftTimingDTO specialShiftTimingDTO = specialShiftTimingMapper.toDto(specialShiftTiming);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialShiftTimingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialShiftTimingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialShiftTiming in the database
        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialShiftTiming() throws Exception {
        int databaseSizeBeforeUpdate = specialShiftTimingRepository.findAll().size();
        specialShiftTiming.setId(count.incrementAndGet());

        // Create the SpecialShiftTiming
        SpecialShiftTimingDTO specialShiftTimingDTO = specialShiftTimingMapper.toDto(specialShiftTiming);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialShiftTimingMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialShiftTimingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialShiftTiming in the database
        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialShiftTimingWithPatch() throws Exception {
        // Initialize the database
        specialShiftTimingRepository.saveAndFlush(specialShiftTiming);

        int databaseSizeBeforeUpdate = specialShiftTimingRepository.findAll().size();

        // Update the specialShiftTiming using partial update
        SpecialShiftTiming partialUpdatedSpecialShiftTiming = new SpecialShiftTiming();
        partialUpdatedSpecialShiftTiming.setId(specialShiftTiming.getId());

        partialUpdatedSpecialShiftTiming
            .startDate(UPDATED_START_DATE)
            .overrideRoaster(UPDATED_OVERRIDE_ROASTER)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT);

        restSpecialShiftTimingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialShiftTiming.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialShiftTiming))
            )
            .andExpect(status().isOk());

        // Validate the SpecialShiftTiming in the database
        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeUpdate);
        SpecialShiftTiming testSpecialShiftTiming = specialShiftTimingList.get(specialShiftTimingList.size() - 1);
        assertThat(testSpecialShiftTiming.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSpecialShiftTiming.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSpecialShiftTiming.getOverrideRoaster()).isEqualTo(UPDATED_OVERRIDE_ROASTER);
        assertThat(testSpecialShiftTiming.getOverrideFlexSchedule()).isEqualTo(DEFAULT_OVERRIDE_FLEX_SCHEDULE);
        assertThat(testSpecialShiftTiming.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testSpecialShiftTiming.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSpecialShiftTiming.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSpecialShiftTiming.getReason()).isEqualTo(DEFAULT_REASON);
    }

    @Test
    @Transactional
    void fullUpdateSpecialShiftTimingWithPatch() throws Exception {
        // Initialize the database
        specialShiftTimingRepository.saveAndFlush(specialShiftTiming);

        int databaseSizeBeforeUpdate = specialShiftTimingRepository.findAll().size();

        // Update the specialShiftTiming using partial update
        SpecialShiftTiming partialUpdatedSpecialShiftTiming = new SpecialShiftTiming();
        partialUpdatedSpecialShiftTiming.setId(specialShiftTiming.getId());

        partialUpdatedSpecialShiftTiming
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .overrideRoaster(UPDATED_OVERRIDE_ROASTER)
            .overrideFlexSchedule(UPDATED_OVERRIDE_FLEX_SCHEDULE)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .reason(UPDATED_REASON);

        restSpecialShiftTimingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialShiftTiming.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialShiftTiming))
            )
            .andExpect(status().isOk());

        // Validate the SpecialShiftTiming in the database
        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeUpdate);
        SpecialShiftTiming testSpecialShiftTiming = specialShiftTimingList.get(specialShiftTimingList.size() - 1);
        assertThat(testSpecialShiftTiming.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSpecialShiftTiming.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSpecialShiftTiming.getOverrideRoaster()).isEqualTo(UPDATED_OVERRIDE_ROASTER);
        assertThat(testSpecialShiftTiming.getOverrideFlexSchedule()).isEqualTo(UPDATED_OVERRIDE_FLEX_SCHEDULE);
        assertThat(testSpecialShiftTiming.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testSpecialShiftTiming.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSpecialShiftTiming.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSpecialShiftTiming.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void patchNonExistingSpecialShiftTiming() throws Exception {
        int databaseSizeBeforeUpdate = specialShiftTimingRepository.findAll().size();
        specialShiftTiming.setId(count.incrementAndGet());

        // Create the SpecialShiftTiming
        SpecialShiftTimingDTO specialShiftTimingDTO = specialShiftTimingMapper.toDto(specialShiftTiming);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialShiftTimingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialShiftTimingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialShiftTimingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialShiftTiming in the database
        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialShiftTiming() throws Exception {
        int databaseSizeBeforeUpdate = specialShiftTimingRepository.findAll().size();
        specialShiftTiming.setId(count.incrementAndGet());

        // Create the SpecialShiftTiming
        SpecialShiftTimingDTO specialShiftTimingDTO = specialShiftTimingMapper.toDto(specialShiftTiming);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialShiftTimingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialShiftTimingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialShiftTiming in the database
        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialShiftTiming() throws Exception {
        int databaseSizeBeforeUpdate = specialShiftTimingRepository.findAll().size();
        specialShiftTiming.setId(count.incrementAndGet());

        // Create the SpecialShiftTiming
        SpecialShiftTimingDTO specialShiftTimingDTO = specialShiftTimingMapper.toDto(specialShiftTiming);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialShiftTimingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialShiftTimingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialShiftTiming in the database
        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialShiftTiming() throws Exception {
        // Initialize the database
        specialShiftTimingRepository.saveAndFlush(specialShiftTiming);

        int databaseSizeBeforeDelete = specialShiftTimingRepository.findAll().size();

        // Delete the specialShiftTiming
        restSpecialShiftTimingMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialShiftTiming.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findAll();
        assertThat(specialShiftTimingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
