package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.FlexSchedule;
import com.bits.hr.domain.User;
import com.bits.hr.repository.FlexScheduleRepository;
import com.bits.hr.service.FlexScheduleService;
import com.bits.hr.service.dto.FlexScheduleDTO;
import com.bits.hr.service.mapper.FlexScheduleMapper;
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
 * Integration tests for the {@link FlexScheduleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FlexScheduleResourceIT {

    private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_IN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_IN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_OUT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OUT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/flex-schedules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlexScheduleRepository flexScheduleRepository;

    @Mock
    private FlexScheduleRepository flexScheduleRepositoryMock;

    @Autowired
    private FlexScheduleMapper flexScheduleMapper;

    @Mock
    private FlexScheduleService flexScheduleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlexScheduleMockMvc;

    private FlexSchedule flexSchedule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlexSchedule createEntity(EntityManager em) {
        FlexSchedule flexSchedule = new FlexSchedule()
            .effectiveDate(DEFAULT_EFFECTIVE_DATE)
            .inTime(DEFAULT_IN_TIME)
            .outTime(DEFAULT_OUT_TIME);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        flexSchedule.setEmployee(employee);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        flexSchedule.setCreatedBy(user);
        return flexSchedule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlexSchedule createUpdatedEntity(EntityManager em) {
        FlexSchedule flexSchedule = new FlexSchedule()
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .inTime(UPDATED_IN_TIME)
            .outTime(UPDATED_OUT_TIME);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        flexSchedule.setEmployee(employee);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        flexSchedule.setCreatedBy(user);
        return flexSchedule;
    }

    @BeforeEach
    public void initTest() {
        flexSchedule = createEntity(em);
    }

    @Test
    @Transactional
    void createFlexSchedule() throws Exception {
        int databaseSizeBeforeCreate = flexScheduleRepository.findAll().size();
        // Create the FlexSchedule
        FlexScheduleDTO flexScheduleDTO = flexScheduleMapper.toDto(flexSchedule);
        restFlexScheduleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flexScheduleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FlexSchedule in the database
        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();
        assertThat(flexScheduleList).hasSize(databaseSizeBeforeCreate + 1);
        FlexSchedule testFlexSchedule = flexScheduleList.get(flexScheduleList.size() - 1);
        assertThat(testFlexSchedule.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testFlexSchedule.getInTime()).isEqualTo(DEFAULT_IN_TIME);
        assertThat(testFlexSchedule.getOutTime()).isEqualTo(DEFAULT_OUT_TIME);
    }

    @Test
    @Transactional
    void createFlexScheduleWithExistingId() throws Exception {
        // Create the FlexSchedule with an existing ID
        flexSchedule.setId(1L);
        FlexScheduleDTO flexScheduleDTO = flexScheduleMapper.toDto(flexSchedule);

        int databaseSizeBeforeCreate = flexScheduleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlexScheduleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flexScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexSchedule in the database
        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();
        assertThat(flexScheduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEffectiveDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = flexScheduleRepository.findAll().size();
        // set the field null
        flexSchedule.setEffectiveDate(null);

        // Create the FlexSchedule, which fails.
        FlexScheduleDTO flexScheduleDTO = flexScheduleMapper.toDto(flexSchedule);

        restFlexScheduleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flexScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();
        assertThat(flexScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = flexScheduleRepository.findAll().size();
        // set the field null
        flexSchedule.setInTime(null);

        // Create the FlexSchedule, which fails.
        FlexScheduleDTO flexScheduleDTO = flexScheduleMapper.toDto(flexSchedule);

        restFlexScheduleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flexScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();
        assertThat(flexScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOutTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = flexScheduleRepository.findAll().size();
        // set the field null
        flexSchedule.setOutTime(null);

        // Create the FlexSchedule, which fails.
        FlexScheduleDTO flexScheduleDTO = flexScheduleMapper.toDto(flexSchedule);

        restFlexScheduleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flexScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();
        assertThat(flexScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFlexSchedules() throws Exception {
        // Initialize the database
        flexScheduleRepository.saveAndFlush(flexSchedule);

        // Get all the flexScheduleList
        restFlexScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flexSchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].inTime").value(hasItem(DEFAULT_IN_TIME.toString())))
            .andExpect(jsonPath("$.[*].outTime").value(hasItem(DEFAULT_OUT_TIME.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFlexSchedulesWithEagerRelationshipsIsEnabled() throws Exception {
        when(flexScheduleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFlexScheduleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(flexScheduleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFlexSchedulesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(flexScheduleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFlexScheduleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(flexScheduleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFlexSchedule() throws Exception {
        // Initialize the database
        flexScheduleRepository.saveAndFlush(flexSchedule);

        // Get the flexSchedule
        restFlexScheduleMockMvc
            .perform(get(ENTITY_API_URL_ID, flexSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flexSchedule.getId().intValue()))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.inTime").value(DEFAULT_IN_TIME.toString()))
            .andExpect(jsonPath("$.outTime").value(DEFAULT_OUT_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFlexSchedule() throws Exception {
        // Get the flexSchedule
        restFlexScheduleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFlexSchedule() throws Exception {
        // Initialize the database
        flexScheduleRepository.saveAndFlush(flexSchedule);

        int databaseSizeBeforeUpdate = flexScheduleRepository.findAll().size();

        // Update the flexSchedule
        FlexSchedule updatedFlexSchedule = flexScheduleRepository.findById(flexSchedule.getId()).get();
        // Disconnect from session so that the updates on updatedFlexSchedule are not directly saved in db
        em.detach(updatedFlexSchedule);
        updatedFlexSchedule.effectiveDate(UPDATED_EFFECTIVE_DATE).inTime(UPDATED_IN_TIME).outTime(UPDATED_OUT_TIME);
        FlexScheduleDTO flexScheduleDTO = flexScheduleMapper.toDto(updatedFlexSchedule);

        restFlexScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flexScheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleDTO))
            )
            .andExpect(status().isOk());

        // Validate the FlexSchedule in the database
        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();
        assertThat(flexScheduleList).hasSize(databaseSizeBeforeUpdate);
        FlexSchedule testFlexSchedule = flexScheduleList.get(flexScheduleList.size() - 1);
        assertThat(testFlexSchedule.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testFlexSchedule.getInTime()).isEqualTo(UPDATED_IN_TIME);
        assertThat(testFlexSchedule.getOutTime()).isEqualTo(UPDATED_OUT_TIME);
    }

    @Test
    @Transactional
    void putNonExistingFlexSchedule() throws Exception {
        int databaseSizeBeforeUpdate = flexScheduleRepository.findAll().size();
        flexSchedule.setId(count.incrementAndGet());

        // Create the FlexSchedule
        FlexScheduleDTO flexScheduleDTO = flexScheduleMapper.toDto(flexSchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlexScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flexScheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexSchedule in the database
        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();
        assertThat(flexScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlexSchedule() throws Exception {
        int databaseSizeBeforeUpdate = flexScheduleRepository.findAll().size();
        flexSchedule.setId(count.incrementAndGet());

        // Create the FlexSchedule
        FlexScheduleDTO flexScheduleDTO = flexScheduleMapper.toDto(flexSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexSchedule in the database
        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();
        assertThat(flexScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlexSchedule() throws Exception {
        int databaseSizeBeforeUpdate = flexScheduleRepository.findAll().size();
        flexSchedule.setId(count.incrementAndGet());

        // Create the FlexSchedule
        FlexScheduleDTO flexScheduleDTO = flexScheduleMapper.toDto(flexSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexScheduleMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flexScheduleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlexSchedule in the database
        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();
        assertThat(flexScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlexScheduleWithPatch() throws Exception {
        // Initialize the database
        flexScheduleRepository.saveAndFlush(flexSchedule);

        int databaseSizeBeforeUpdate = flexScheduleRepository.findAll().size();

        // Update the flexSchedule using partial update
        FlexSchedule partialUpdatedFlexSchedule = new FlexSchedule();
        partialUpdatedFlexSchedule.setId(flexSchedule.getId());

        partialUpdatedFlexSchedule.outTime(UPDATED_OUT_TIME);

        restFlexScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlexSchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlexSchedule))
            )
            .andExpect(status().isOk());

        // Validate the FlexSchedule in the database
        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();
        assertThat(flexScheduleList).hasSize(databaseSizeBeforeUpdate);
        FlexSchedule testFlexSchedule = flexScheduleList.get(flexScheduleList.size() - 1);
        assertThat(testFlexSchedule.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testFlexSchedule.getInTime()).isEqualTo(DEFAULT_IN_TIME);
        assertThat(testFlexSchedule.getOutTime()).isEqualTo(UPDATED_OUT_TIME);
    }

    @Test
    @Transactional
    void fullUpdateFlexScheduleWithPatch() throws Exception {
        // Initialize the database
        flexScheduleRepository.saveAndFlush(flexSchedule);

        int databaseSizeBeforeUpdate = flexScheduleRepository.findAll().size();

        // Update the flexSchedule using partial update
        FlexSchedule partialUpdatedFlexSchedule = new FlexSchedule();
        partialUpdatedFlexSchedule.setId(flexSchedule.getId());

        partialUpdatedFlexSchedule.effectiveDate(UPDATED_EFFECTIVE_DATE).inTime(UPDATED_IN_TIME).outTime(UPDATED_OUT_TIME);

        restFlexScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlexSchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlexSchedule))
            )
            .andExpect(status().isOk());

        // Validate the FlexSchedule in the database
        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();
        assertThat(flexScheduleList).hasSize(databaseSizeBeforeUpdate);
        FlexSchedule testFlexSchedule = flexScheduleList.get(flexScheduleList.size() - 1);
        assertThat(testFlexSchedule.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testFlexSchedule.getInTime()).isEqualTo(UPDATED_IN_TIME);
        assertThat(testFlexSchedule.getOutTime()).isEqualTo(UPDATED_OUT_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingFlexSchedule() throws Exception {
        int databaseSizeBeforeUpdate = flexScheduleRepository.findAll().size();
        flexSchedule.setId(count.incrementAndGet());

        // Create the FlexSchedule
        FlexScheduleDTO flexScheduleDTO = flexScheduleMapper.toDto(flexSchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlexScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flexScheduleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexSchedule in the database
        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();
        assertThat(flexScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlexSchedule() throws Exception {
        int databaseSizeBeforeUpdate = flexScheduleRepository.findAll().size();
        flexSchedule.setId(count.incrementAndGet());

        // Create the FlexSchedule
        FlexScheduleDTO flexScheduleDTO = flexScheduleMapper.toDto(flexSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexSchedule in the database
        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();
        assertThat(flexScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlexSchedule() throws Exception {
        int databaseSizeBeforeUpdate = flexScheduleRepository.findAll().size();
        flexSchedule.setId(count.incrementAndGet());

        // Create the FlexSchedule
        FlexScheduleDTO flexScheduleDTO = flexScheduleMapper.toDto(flexSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlexSchedule in the database
        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();
        assertThat(flexScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFlexSchedule() throws Exception {
        // Initialize the database
        flexScheduleRepository.saveAndFlush(flexSchedule);

        int databaseSizeBeforeDelete = flexScheduleRepository.findAll().size();

        // Delete the flexSchedule
        restFlexScheduleMockMvc
            .perform(delete(ENTITY_API_URL_ID, flexSchedule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();
        assertThat(flexScheduleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
