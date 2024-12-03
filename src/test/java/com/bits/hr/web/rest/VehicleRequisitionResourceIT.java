package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.VehicleRequisition;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.VehicleRequisitionRepository;
import com.bits.hr.service.VehicleRequisitionService;
import com.bits.hr.service.dto.VehicleRequisitionDTO;
import com.bits.hr.service.mapper.VehicleRequisitionMapper;
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
 * Integration tests for the {@link VehicleRequisitionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VehicleRequisitionResourceIT {

    private static final String DEFAULT_PURPOSE = "AAAAAAAAAA";
    private static final String UPDATED_PURPOSE = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_PASSENGERS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_PASSENGERS_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_TOTAL_NUMBER_OF_PASSENGERS = 0L;
    private static final Long UPDATED_TOTAL_NUMBER_OF_PASSENGERS = 1L;

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.APPROVED;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SANCTION_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SANCTION_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TRANSACTION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_START_TIME = 0D;
    private static final Double UPDATED_START_TIME = 1D;

    private static final Double DEFAULT_END_TIME = 0D;
    private static final Double UPDATED_END_TIME = 1D;

    private static final String DEFAULT_AREA = "AAAAAAAAAA";
    private static final String UPDATED_AREA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vehicle-requisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VehicleRequisitionRepository vehicleRequisitionRepository;

    @Mock
    private VehicleRequisitionRepository vehicleRequisitionRepositoryMock;

    @Autowired
    private VehicleRequisitionMapper vehicleRequisitionMapper;

    @Mock
    private VehicleRequisitionService vehicleRequisitionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleRequisitionMockMvc;

    private VehicleRequisition vehicleRequisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleRequisition createEntity(EntityManager em) {
        VehicleRequisition vehicleRequisition = new VehicleRequisition()
            .purpose(DEFAULT_PURPOSE)
            .otherPassengersName(DEFAULT_OTHER_PASSENGERS_NAME)
            .totalNumberOfPassengers(DEFAULT_TOTAL_NUMBER_OF_PASSENGERS)
            .status(DEFAULT_STATUS)
            .remarks(DEFAULT_REMARKS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .sanctionAt(DEFAULT_SANCTION_AT)
            .transactionNumber(DEFAULT_TRANSACTION_NUMBER)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .area(DEFAULT_AREA);
        return vehicleRequisition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleRequisition createUpdatedEntity(EntityManager em) {
        VehicleRequisition vehicleRequisition = new VehicleRequisition()
            .purpose(UPDATED_PURPOSE)
            .otherPassengersName(UPDATED_OTHER_PASSENGERS_NAME)
            .totalNumberOfPassengers(UPDATED_TOTAL_NUMBER_OF_PASSENGERS)
            .status(UPDATED_STATUS)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .sanctionAt(UPDATED_SANCTION_AT)
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .area(UPDATED_AREA);
        return vehicleRequisition;
    }

    @BeforeEach
    public void initTest() {
        vehicleRequisition = createEntity(em);
    }

    @Test
    @Transactional
    void createVehicleRequisition() throws Exception {
        int databaseSizeBeforeCreate = vehicleRequisitionRepository.findAll().size();
        // Create the VehicleRequisition
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);
        restVehicleRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the VehicleRequisition in the database
        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeCreate + 1);
        VehicleRequisition testVehicleRequisition = vehicleRequisitionList.get(vehicleRequisitionList.size() - 1);
        assertThat(testVehicleRequisition.getPurpose()).isEqualTo(DEFAULT_PURPOSE);
        assertThat(testVehicleRequisition.getOtherPassengersName()).isEqualTo(DEFAULT_OTHER_PASSENGERS_NAME);
        assertThat(testVehicleRequisition.getTotalNumberOfPassengers()).isEqualTo(DEFAULT_TOTAL_NUMBER_OF_PASSENGERS);
        assertThat(testVehicleRequisition.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testVehicleRequisition.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testVehicleRequisition.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testVehicleRequisition.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testVehicleRequisition.getSanctionAt()).isEqualTo(DEFAULT_SANCTION_AT);
        assertThat(testVehicleRequisition.getTransactionNumber()).isEqualTo(DEFAULT_TRANSACTION_NUMBER);
        assertThat(testVehicleRequisition.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testVehicleRequisition.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testVehicleRequisition.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testVehicleRequisition.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testVehicleRequisition.getArea()).isEqualTo(DEFAULT_AREA);
    }

    @Test
    @Transactional
    void createVehicleRequisitionWithExistingId() throws Exception {
        // Create the VehicleRequisition with an existing ID
        vehicleRequisition.setId(1L);
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);

        int databaseSizeBeforeCreate = vehicleRequisitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleRequisition in the database
        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPurposeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleRequisitionRepository.findAll().size();
        // set the field null
        vehicleRequisition.setPurpose(null);

        // Create the VehicleRequisition, which fails.
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);

        restVehicleRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalNumberOfPassengersIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleRequisitionRepository.findAll().size();
        // set the field null
        vehicleRequisition.setTotalNumberOfPassengers(null);

        // Create the VehicleRequisition, which fails.
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);

        restVehicleRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleRequisitionRepository.findAll().size();
        // set the field null
        vehicleRequisition.setStatus(null);

        // Create the VehicleRequisition, which fails.
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);

        restVehicleRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleRequisitionRepository.findAll().size();
        // set the field null
        vehicleRequisition.setStartDate(null);

        // Create the VehicleRequisition, which fails.
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);

        restVehicleRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleRequisitionRepository.findAll().size();
        // set the field null
        vehicleRequisition.setEndDate(null);

        // Create the VehicleRequisition, which fails.
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);

        restVehicleRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleRequisitionRepository.findAll().size();
        // set the field null
        vehicleRequisition.setStartTime(null);

        // Create the VehicleRequisition, which fails.
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);

        restVehicleRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleRequisitionRepository.findAll().size();
        // set the field null
        vehicleRequisition.setEndTime(null);

        // Create the VehicleRequisition, which fails.
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);

        restVehicleRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAreaIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleRequisitionRepository.findAll().size();
        // set the field null
        vehicleRequisition.setArea(null);

        // Create the VehicleRequisition, which fails.
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);

        restVehicleRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicleRequisitions() throws Exception {
        // Initialize the database
        vehicleRequisitionRepository.saveAndFlush(vehicleRequisition);

        // Get all the vehicleRequisitionList
        restVehicleRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE)))
            .andExpect(jsonPath("$.[*].otherPassengersName").value(hasItem(DEFAULT_OTHER_PASSENGERS_NAME)))
            .andExpect(jsonPath("$.[*].totalNumberOfPassengers").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_PASSENGERS.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].sanctionAt").value(hasItem(DEFAULT_SANCTION_AT.toString())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVehicleRequisitionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(vehicleRequisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVehicleRequisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vehicleRequisitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVehicleRequisitionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vehicleRequisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVehicleRequisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vehicleRequisitionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVehicleRequisition() throws Exception {
        // Initialize the database
        vehicleRequisitionRepository.saveAndFlush(vehicleRequisition);

        // Get the vehicleRequisition
        restVehicleRequisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleRequisition.getId().intValue()))
            .andExpect(jsonPath("$.purpose").value(DEFAULT_PURPOSE))
            .andExpect(jsonPath("$.otherPassengersName").value(DEFAULT_OTHER_PASSENGERS_NAME))
            .andExpect(jsonPath("$.totalNumberOfPassengers").value(DEFAULT_TOTAL_NUMBER_OF_PASSENGERS.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.sanctionAt").value(DEFAULT_SANCTION_AT.toString()))
            .andExpect(jsonPath("$.transactionNumber").value(DEFAULT_TRANSACTION_NUMBER))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.doubleValue()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.doubleValue()))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA));
    }

    @Test
    @Transactional
    void getNonExistingVehicleRequisition() throws Exception {
        // Get the vehicleRequisition
        restVehicleRequisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleRequisition() throws Exception {
        // Initialize the database
        vehicleRequisitionRepository.saveAndFlush(vehicleRequisition);

        int databaseSizeBeforeUpdate = vehicleRequisitionRepository.findAll().size();

        // Update the vehicleRequisition
        VehicleRequisition updatedVehicleRequisition = vehicleRequisitionRepository.findById(vehicleRequisition.getId()).get();
        // Disconnect from session so that the updates on updatedVehicleRequisition are not directly saved in db
        em.detach(updatedVehicleRequisition);
        updatedVehicleRequisition
            .purpose(UPDATED_PURPOSE)
            .otherPassengersName(UPDATED_OTHER_PASSENGERS_NAME)
            .totalNumberOfPassengers(UPDATED_TOTAL_NUMBER_OF_PASSENGERS)
            .status(UPDATED_STATUS)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .sanctionAt(UPDATED_SANCTION_AT)
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .area(UPDATED_AREA);
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(updatedVehicleRequisition);

        restVehicleRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the VehicleRequisition in the database
        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeUpdate);
        VehicleRequisition testVehicleRequisition = vehicleRequisitionList.get(vehicleRequisitionList.size() - 1);
        assertThat(testVehicleRequisition.getPurpose()).isEqualTo(UPDATED_PURPOSE);
        assertThat(testVehicleRequisition.getOtherPassengersName()).isEqualTo(UPDATED_OTHER_PASSENGERS_NAME);
        assertThat(testVehicleRequisition.getTotalNumberOfPassengers()).isEqualTo(UPDATED_TOTAL_NUMBER_OF_PASSENGERS);
        assertThat(testVehicleRequisition.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVehicleRequisition.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testVehicleRequisition.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testVehicleRequisition.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testVehicleRequisition.getSanctionAt()).isEqualTo(UPDATED_SANCTION_AT);
        assertThat(testVehicleRequisition.getTransactionNumber()).isEqualTo(UPDATED_TRANSACTION_NUMBER);
        assertThat(testVehicleRequisition.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testVehicleRequisition.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testVehicleRequisition.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testVehicleRequisition.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testVehicleRequisition.getArea()).isEqualTo(UPDATED_AREA);
    }

    @Test
    @Transactional
    void putNonExistingVehicleRequisition() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRequisitionRepository.findAll().size();
        vehicleRequisition.setId(count.incrementAndGet());

        // Create the VehicleRequisition
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleRequisition in the database
        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleRequisition() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRequisitionRepository.findAll().size();
        vehicleRequisition.setId(count.incrementAndGet());

        // Create the VehicleRequisition
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleRequisition in the database
        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleRequisition() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRequisitionRepository.findAll().size();
        vehicleRequisition.setId(count.incrementAndGet());

        // Create the VehicleRequisition
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleRequisition in the database
        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleRequisitionWithPatch() throws Exception {
        // Initialize the database
        vehicleRequisitionRepository.saveAndFlush(vehicleRequisition);

        int databaseSizeBeforeUpdate = vehicleRequisitionRepository.findAll().size();

        // Update the vehicleRequisition using partial update
        VehicleRequisition partialUpdatedVehicleRequisition = new VehicleRequisition();
        partialUpdatedVehicleRequisition.setId(vehicleRequisition.getId());

        partialUpdatedVehicleRequisition
            .otherPassengersName(UPDATED_OTHER_PASSENGERS_NAME)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .startDate(UPDATED_START_DATE)
            .startTime(UPDATED_START_TIME)
            .area(UPDATED_AREA);

        restVehicleRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicleRequisition))
            )
            .andExpect(status().isOk());

        // Validate the VehicleRequisition in the database
        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeUpdate);
        VehicleRequisition testVehicleRequisition = vehicleRequisitionList.get(vehicleRequisitionList.size() - 1);
        assertThat(testVehicleRequisition.getPurpose()).isEqualTo(DEFAULT_PURPOSE);
        assertThat(testVehicleRequisition.getOtherPassengersName()).isEqualTo(UPDATED_OTHER_PASSENGERS_NAME);
        assertThat(testVehicleRequisition.getTotalNumberOfPassengers()).isEqualTo(DEFAULT_TOTAL_NUMBER_OF_PASSENGERS);
        assertThat(testVehicleRequisition.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVehicleRequisition.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testVehicleRequisition.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testVehicleRequisition.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testVehicleRequisition.getSanctionAt()).isEqualTo(DEFAULT_SANCTION_AT);
        assertThat(testVehicleRequisition.getTransactionNumber()).isEqualTo(DEFAULT_TRANSACTION_NUMBER);
        assertThat(testVehicleRequisition.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testVehicleRequisition.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testVehicleRequisition.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testVehicleRequisition.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testVehicleRequisition.getArea()).isEqualTo(UPDATED_AREA);
    }

    @Test
    @Transactional
    void fullUpdateVehicleRequisitionWithPatch() throws Exception {
        // Initialize the database
        vehicleRequisitionRepository.saveAndFlush(vehicleRequisition);

        int databaseSizeBeforeUpdate = vehicleRequisitionRepository.findAll().size();

        // Update the vehicleRequisition using partial update
        VehicleRequisition partialUpdatedVehicleRequisition = new VehicleRequisition();
        partialUpdatedVehicleRequisition.setId(vehicleRequisition.getId());

        partialUpdatedVehicleRequisition
            .purpose(UPDATED_PURPOSE)
            .otherPassengersName(UPDATED_OTHER_PASSENGERS_NAME)
            .totalNumberOfPassengers(UPDATED_TOTAL_NUMBER_OF_PASSENGERS)
            .status(UPDATED_STATUS)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .sanctionAt(UPDATED_SANCTION_AT)
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .area(UPDATED_AREA);

        restVehicleRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicleRequisition))
            )
            .andExpect(status().isOk());

        // Validate the VehicleRequisition in the database
        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeUpdate);
        VehicleRequisition testVehicleRequisition = vehicleRequisitionList.get(vehicleRequisitionList.size() - 1);
        assertThat(testVehicleRequisition.getPurpose()).isEqualTo(UPDATED_PURPOSE);
        assertThat(testVehicleRequisition.getOtherPassengersName()).isEqualTo(UPDATED_OTHER_PASSENGERS_NAME);
        assertThat(testVehicleRequisition.getTotalNumberOfPassengers()).isEqualTo(UPDATED_TOTAL_NUMBER_OF_PASSENGERS);
        assertThat(testVehicleRequisition.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVehicleRequisition.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testVehicleRequisition.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testVehicleRequisition.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testVehicleRequisition.getSanctionAt()).isEqualTo(UPDATED_SANCTION_AT);
        assertThat(testVehicleRequisition.getTransactionNumber()).isEqualTo(UPDATED_TRANSACTION_NUMBER);
        assertThat(testVehicleRequisition.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testVehicleRequisition.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testVehicleRequisition.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testVehicleRequisition.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testVehicleRequisition.getArea()).isEqualTo(UPDATED_AREA);
    }

    @Test
    @Transactional
    void patchNonExistingVehicleRequisition() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRequisitionRepository.findAll().size();
        vehicleRequisition.setId(count.incrementAndGet());

        // Create the VehicleRequisition
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleRequisitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleRequisition in the database
        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleRequisition() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRequisitionRepository.findAll().size();
        vehicleRequisition.setId(count.incrementAndGet());

        // Create the VehicleRequisition
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleRequisition in the database
        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleRequisition() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRequisitionRepository.findAll().size();
        vehicleRequisition.setId(count.incrementAndGet());

        // Create the VehicleRequisition
        VehicleRequisitionDTO vehicleRequisitionDTO = vehicleRequisitionMapper.toDto(vehicleRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicleRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleRequisition in the database
        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleRequisition() throws Exception {
        // Initialize the database
        vehicleRequisitionRepository.saveAndFlush(vehicleRequisition);

        int databaseSizeBeforeDelete = vehicleRequisitionRepository.findAll().size();

        // Delete the vehicleRequisition
        restVehicleRequisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleRequisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAll();
        assertThat(vehicleRequisitionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
