package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.MovementEntry;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.MovementType;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.MovementEntryRepository;
import com.bits.hr.service.MovementEntryService;
import com.bits.hr.service.dto.MovementEntryDTO;
import com.bits.hr.service.mapper.MovementEntryMapper;
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
 * Integration tests for the {@link MovementEntryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MovementEntryResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_START_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_START_NOTE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_END_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_END_NOTE = "BBBBBBBBBB";

    private static final MovementType DEFAULT_TYPE = MovementType.MOVEMENT;
    private static final MovementType UPDATED_TYPE = MovementType.MOVEMENT;

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.APPROVED;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_SANCTION_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SANCTION_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/movement-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MovementEntryRepository movementEntryRepository;

    @Mock
    private MovementEntryRepository movementEntryRepositoryMock;

    @Autowired
    private MovementEntryMapper movementEntryMapper;

    @Mock
    private MovementEntryService movementEntryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMovementEntryMockMvc;

    private MovementEntry movementEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MovementEntry createEntity(EntityManager em) {
        MovementEntry movementEntry = new MovementEntry()
            .startDate(DEFAULT_START_DATE)
            .startTime(DEFAULT_START_TIME)
            .startNote(DEFAULT_START_NOTE)
            .endDate(DEFAULT_END_DATE)
            .endTime(DEFAULT_END_TIME)
            .endNote(DEFAULT_END_NOTE)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .sanctionAt(DEFAULT_SANCTION_AT)
            .note(DEFAULT_NOTE);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        movementEntry.setEmployee(employee);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        movementEntry.setCreatedBy(user);
        return movementEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MovementEntry createUpdatedEntity(EntityManager em) {
        MovementEntry movementEntry = new MovementEntry()
            .startDate(UPDATED_START_DATE)
            .startTime(UPDATED_START_TIME)
            .startNote(UPDATED_START_NOTE)
            .endDate(UPDATED_END_DATE)
            .endTime(UPDATED_END_TIME)
            .endNote(UPDATED_END_NOTE)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .sanctionAt(UPDATED_SANCTION_AT)
            .note(UPDATED_NOTE);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        movementEntry.setEmployee(employee);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        movementEntry.setCreatedBy(user);
        return movementEntry;
    }

    @BeforeEach
    public void initTest() {
        movementEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createMovementEntry() throws Exception {
        int databaseSizeBeforeCreate = movementEntryRepository.findAll().size();
        // Create the MovementEntry
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);
        restMovementEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MovementEntry in the database
        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeCreate + 1);
        MovementEntry testMovementEntry = movementEntryList.get(movementEntryList.size() - 1);
        assertThat(testMovementEntry.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testMovementEntry.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testMovementEntry.getStartNote()).isEqualTo(DEFAULT_START_NOTE);
        assertThat(testMovementEntry.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testMovementEntry.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testMovementEntry.getEndNote()).isEqualTo(DEFAULT_END_NOTE);
        assertThat(testMovementEntry.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMovementEntry.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMovementEntry.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testMovementEntry.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testMovementEntry.getSanctionAt()).isEqualTo(DEFAULT_SANCTION_AT);
        assertThat(testMovementEntry.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createMovementEntryWithExistingId() throws Exception {
        // Create the MovementEntry with an existing ID
        movementEntry.setId(1L);
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        int databaseSizeBeforeCreate = movementEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovementEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovementEntry in the database
        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementEntryRepository.findAll().size();
        // set the field null
        movementEntry.setStartDate(null);

        // Create the MovementEntry, which fails.
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        restMovementEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementEntryRepository.findAll().size();
        // set the field null
        movementEntry.setStartTime(null);

        // Create the MovementEntry, which fails.
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        restMovementEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartNoteIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementEntryRepository.findAll().size();
        // set the field null
        movementEntry.setStartNote(null);

        // Create the MovementEntry, which fails.
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        restMovementEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementEntryRepository.findAll().size();
        // set the field null
        movementEntry.setEndDate(null);

        // Create the MovementEntry, which fails.
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        restMovementEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementEntryRepository.findAll().size();
        // set the field null
        movementEntry.setEndTime(null);

        // Create the MovementEntry, which fails.
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        restMovementEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndNoteIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementEntryRepository.findAll().size();
        // set the field null
        movementEntry.setEndNote(null);

        // Create the MovementEntry, which fails.
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        restMovementEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementEntryRepository.findAll().size();
        // set the field null
        movementEntry.setType(null);

        // Create the MovementEntry, which fails.
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        restMovementEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementEntryRepository.findAll().size();
        // set the field null
        movementEntry.setStatus(null);

        // Create the MovementEntry, which fails.
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        restMovementEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementEntryRepository.findAll().size();
        // set the field null
        movementEntry.setCreatedAt(null);

        // Create the MovementEntry, which fails.
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        restMovementEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMovementEntries() throws Exception {
        // Initialize the database
        movementEntryRepository.saveAndFlush(movementEntry);

        // Get all the movementEntryList
        restMovementEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movementEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].startNote").value(hasItem(DEFAULT_START_NOTE)))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].endNote").value(hasItem(DEFAULT_END_NOTE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].sanctionAt").value(hasItem(DEFAULT_SANCTION_AT.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMovementEntriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(movementEntryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMovementEntryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(movementEntryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMovementEntriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(movementEntryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMovementEntryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(movementEntryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMovementEntry() throws Exception {
        // Initialize the database
        movementEntryRepository.saveAndFlush(movementEntry);

        // Get the movementEntry
        restMovementEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, movementEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(movementEntry.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.startNote").value(DEFAULT_START_NOTE))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.endNote").value(DEFAULT_END_NOTE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.sanctionAt").value(DEFAULT_SANCTION_AT.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingMovementEntry() throws Exception {
        // Get the movementEntry
        restMovementEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMovementEntry() throws Exception {
        // Initialize the database
        movementEntryRepository.saveAndFlush(movementEntry);

        int databaseSizeBeforeUpdate = movementEntryRepository.findAll().size();

        // Update the movementEntry
        MovementEntry updatedMovementEntry = movementEntryRepository.findById(movementEntry.getId()).get();
        // Disconnect from session so that the updates on updatedMovementEntry are not directly saved in db
        em.detach(updatedMovementEntry);
        updatedMovementEntry
            .startDate(UPDATED_START_DATE)
            .startTime(UPDATED_START_TIME)
            .startNote(UPDATED_START_NOTE)
            .endDate(UPDATED_END_DATE)
            .endTime(UPDATED_END_TIME)
            .endNote(UPDATED_END_NOTE)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .sanctionAt(UPDATED_SANCTION_AT)
            .note(UPDATED_NOTE);
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(updatedMovementEntry);

        restMovementEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movementEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the MovementEntry in the database
        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeUpdate);
        MovementEntry testMovementEntry = movementEntryList.get(movementEntryList.size() - 1);
        assertThat(testMovementEntry.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMovementEntry.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testMovementEntry.getStartNote()).isEqualTo(UPDATED_START_NOTE);
        assertThat(testMovementEntry.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testMovementEntry.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testMovementEntry.getEndNote()).isEqualTo(UPDATED_END_NOTE);
        assertThat(testMovementEntry.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMovementEntry.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMovementEntry.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testMovementEntry.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testMovementEntry.getSanctionAt()).isEqualTo(UPDATED_SANCTION_AT);
        assertThat(testMovementEntry.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingMovementEntry() throws Exception {
        int databaseSizeBeforeUpdate = movementEntryRepository.findAll().size();
        movementEntry.setId(count.incrementAndGet());

        // Create the MovementEntry
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovementEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movementEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovementEntry in the database
        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMovementEntry() throws Exception {
        int databaseSizeBeforeUpdate = movementEntryRepository.findAll().size();
        movementEntry.setId(count.incrementAndGet());

        // Create the MovementEntry
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovementEntry in the database
        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMovementEntry() throws Exception {
        int databaseSizeBeforeUpdate = movementEntryRepository.findAll().size();
        movementEntry.setId(count.incrementAndGet());

        // Create the MovementEntry
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementEntryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MovementEntry in the database
        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMovementEntryWithPatch() throws Exception {
        // Initialize the database
        movementEntryRepository.saveAndFlush(movementEntry);

        int databaseSizeBeforeUpdate = movementEntryRepository.findAll().size();

        // Update the movementEntry using partial update
        MovementEntry partialUpdatedMovementEntry = new MovementEntry();
        partialUpdatedMovementEntry.setId(movementEntry.getId());

        partialUpdatedMovementEntry
            .startDate(UPDATED_START_DATE)
            .startTime(UPDATED_START_TIME)
            .startNote(UPDATED_START_NOTE)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .sanctionAt(UPDATED_SANCTION_AT);

        restMovementEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovementEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovementEntry))
            )
            .andExpect(status().isOk());

        // Validate the MovementEntry in the database
        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeUpdate);
        MovementEntry testMovementEntry = movementEntryList.get(movementEntryList.size() - 1);
        assertThat(testMovementEntry.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMovementEntry.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testMovementEntry.getStartNote()).isEqualTo(UPDATED_START_NOTE);
        assertThat(testMovementEntry.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testMovementEntry.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testMovementEntry.getEndNote()).isEqualTo(DEFAULT_END_NOTE);
        assertThat(testMovementEntry.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMovementEntry.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMovementEntry.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testMovementEntry.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testMovementEntry.getSanctionAt()).isEqualTo(UPDATED_SANCTION_AT);
        assertThat(testMovementEntry.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateMovementEntryWithPatch() throws Exception {
        // Initialize the database
        movementEntryRepository.saveAndFlush(movementEntry);

        int databaseSizeBeforeUpdate = movementEntryRepository.findAll().size();

        // Update the movementEntry using partial update
        MovementEntry partialUpdatedMovementEntry = new MovementEntry();
        partialUpdatedMovementEntry.setId(movementEntry.getId());

        partialUpdatedMovementEntry
            .startDate(UPDATED_START_DATE)
            .startTime(UPDATED_START_TIME)
            .startNote(UPDATED_START_NOTE)
            .endDate(UPDATED_END_DATE)
            .endTime(UPDATED_END_TIME)
            .endNote(UPDATED_END_NOTE)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .sanctionAt(UPDATED_SANCTION_AT)
            .note(UPDATED_NOTE);

        restMovementEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovementEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovementEntry))
            )
            .andExpect(status().isOk());

        // Validate the MovementEntry in the database
        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeUpdate);
        MovementEntry testMovementEntry = movementEntryList.get(movementEntryList.size() - 1);
        assertThat(testMovementEntry.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMovementEntry.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testMovementEntry.getStartNote()).isEqualTo(UPDATED_START_NOTE);
        assertThat(testMovementEntry.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testMovementEntry.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testMovementEntry.getEndNote()).isEqualTo(UPDATED_END_NOTE);
        assertThat(testMovementEntry.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMovementEntry.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMovementEntry.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testMovementEntry.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testMovementEntry.getSanctionAt()).isEqualTo(UPDATED_SANCTION_AT);
        assertThat(testMovementEntry.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingMovementEntry() throws Exception {
        int databaseSizeBeforeUpdate = movementEntryRepository.findAll().size();
        movementEntry.setId(count.incrementAndGet());

        // Create the MovementEntry
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovementEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, movementEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovementEntry in the database
        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMovementEntry() throws Exception {
        int databaseSizeBeforeUpdate = movementEntryRepository.findAll().size();
        movementEntry.setId(count.incrementAndGet());

        // Create the MovementEntry
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovementEntry in the database
        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMovementEntry() throws Exception {
        int databaseSizeBeforeUpdate = movementEntryRepository.findAll().size();
        movementEntry.setId(count.incrementAndGet());

        // Create the MovementEntry
        MovementEntryDTO movementEntryDTO = movementEntryMapper.toDto(movementEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementEntryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movementEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MovementEntry in the database
        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMovementEntry() throws Exception {
        // Initialize the database
        movementEntryRepository.saveAndFlush(movementEntry);

        int databaseSizeBeforeDelete = movementEntryRepository.findAll().size();

        // Delete the movementEntry
        restMovementEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, movementEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MovementEntry> movementEntryList = movementEntryRepository.findAll();
        assertThat(movementEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
