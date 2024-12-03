package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.LeaveAllocation;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.repository.LeaveAllocationRepository;
import com.bits.hr.service.dto.LeaveAllocationDTO;
import com.bits.hr.service.mapper.LeaveAllocationMapper;
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
 * Integration tests for the {@link LeaveAllocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LeaveAllocationResourceIT {

    private static final Integer DEFAULT_YEAR = 1900;
    private static final Integer UPDATED_YEAR = 1901;

    private static final LeaveType DEFAULT_LEAVE_TYPE = LeaveType.MENTIONABLE_ANNUAL_LEAVE;
    private static final LeaveType UPDATED_LEAVE_TYPE = LeaveType.MENTIONABLE_CASUAL_LEAVE;

    private static final Integer DEFAULT_ALLOCATED_DAYS = 0;
    private static final Integer UPDATED_ALLOCATED_DAYS = 1;

    private static final String ENTITY_API_URL = "/api/leave-allocations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaveAllocationRepository leaveAllocationRepository;

    @Autowired
    private LeaveAllocationMapper leaveAllocationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaveAllocationMockMvc;

    private LeaveAllocation leaveAllocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveAllocation createEntity(EntityManager em) {
        LeaveAllocation leaveAllocation = new LeaveAllocation()
            .year(DEFAULT_YEAR)
            .leaveType(DEFAULT_LEAVE_TYPE)
            .allocatedDays(DEFAULT_ALLOCATED_DAYS);
        return leaveAllocation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveAllocation createUpdatedEntity(EntityManager em) {
        LeaveAllocation leaveAllocation = new LeaveAllocation()
            .year(UPDATED_YEAR)
            .leaveType(UPDATED_LEAVE_TYPE)
            .allocatedDays(UPDATED_ALLOCATED_DAYS);
        return leaveAllocation;
    }

    @BeforeEach
    public void initTest() {
        leaveAllocation = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaveAllocation() throws Exception {
        int databaseSizeBeforeCreate = leaveAllocationRepository.findAll().size();
        // Create the LeaveAllocation
        LeaveAllocationDTO leaveAllocationDTO = leaveAllocationMapper.toDto(leaveAllocation);
        restLeaveAllocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveAllocationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaveAllocation in the database
        List<LeaveAllocation> leaveAllocationList = leaveAllocationRepository.findAll();
        assertThat(leaveAllocationList).hasSize(databaseSizeBeforeCreate + 1);
        LeaveAllocation testLeaveAllocation = leaveAllocationList.get(leaveAllocationList.size() - 1);
        assertThat(testLeaveAllocation.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testLeaveAllocation.getLeaveType()).isEqualTo(DEFAULT_LEAVE_TYPE);
        assertThat(testLeaveAllocation.getAllocatedDays()).isEqualTo(DEFAULT_ALLOCATED_DAYS);
    }

    @Test
    @Transactional
    void createLeaveAllocationWithExistingId() throws Exception {
        // Create the LeaveAllocation with an existing ID
        leaveAllocation.setId(1L);
        LeaveAllocationDTO leaveAllocationDTO = leaveAllocationMapper.toDto(leaveAllocation);

        int databaseSizeBeforeCreate = leaveAllocationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaveAllocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveAllocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveAllocation in the database
        List<LeaveAllocation> leaveAllocationList = leaveAllocationRepository.findAll();
        assertThat(leaveAllocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaveAllocationRepository.findAll().size();
        // set the field null
        leaveAllocation.setYear(null);

        // Create the LeaveAllocation, which fails.
        LeaveAllocationDTO leaveAllocationDTO = leaveAllocationMapper.toDto(leaveAllocation);

        restLeaveAllocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveAllocationDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaveAllocation> leaveAllocationList = leaveAllocationRepository.findAll();
        assertThat(leaveAllocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAllocatedDaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaveAllocationRepository.findAll().size();
        // set the field null
        leaveAllocation.setAllocatedDays(null);

        // Create the LeaveAllocation, which fails.
        LeaveAllocationDTO leaveAllocationDTO = leaveAllocationMapper.toDto(leaveAllocation);

        restLeaveAllocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveAllocationDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaveAllocation> leaveAllocationList = leaveAllocationRepository.findAll();
        assertThat(leaveAllocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeaveAllocations() throws Exception {
        // Initialize the database
        leaveAllocationRepository.saveAndFlush(leaveAllocation);

        // Get all the leaveAllocationList
        restLeaveAllocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveAllocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].leaveType").value(hasItem(DEFAULT_LEAVE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].allocatedDays").value(hasItem(DEFAULT_ALLOCATED_DAYS)));
    }

    @Test
    @Transactional
    void getLeaveAllocation() throws Exception {
        // Initialize the database
        leaveAllocationRepository.saveAndFlush(leaveAllocation);

        // Get the leaveAllocation
        restLeaveAllocationMockMvc
            .perform(get(ENTITY_API_URL_ID, leaveAllocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaveAllocation.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.leaveType").value(DEFAULT_LEAVE_TYPE.toString()))
            .andExpect(jsonPath("$.allocatedDays").value(DEFAULT_ALLOCATED_DAYS));
    }

    @Test
    @Transactional
    void getNonExistingLeaveAllocation() throws Exception {
        // Get the leaveAllocation
        restLeaveAllocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLeaveAllocation() throws Exception {
        // Initialize the database
        leaveAllocationRepository.saveAndFlush(leaveAllocation);

        int databaseSizeBeforeUpdate = leaveAllocationRepository.findAll().size();

        // Update the leaveAllocation
        LeaveAllocation updatedLeaveAllocation = leaveAllocationRepository.findById(leaveAllocation.getId()).get();
        // Disconnect from session so that the updates on updatedLeaveAllocation are not directly saved in db
        em.detach(updatedLeaveAllocation);
        updatedLeaveAllocation.year(UPDATED_YEAR).leaveType(UPDATED_LEAVE_TYPE).allocatedDays(UPDATED_ALLOCATED_DAYS);
        LeaveAllocationDTO leaveAllocationDTO = leaveAllocationMapper.toDto(updatedLeaveAllocation);

        restLeaveAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaveAllocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveAllocationDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaveAllocation in the database
        List<LeaveAllocation> leaveAllocationList = leaveAllocationRepository.findAll();
        assertThat(leaveAllocationList).hasSize(databaseSizeBeforeUpdate);
        LeaveAllocation testLeaveAllocation = leaveAllocationList.get(leaveAllocationList.size() - 1);
        assertThat(testLeaveAllocation.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testLeaveAllocation.getLeaveType()).isEqualTo(UPDATED_LEAVE_TYPE);
        assertThat(testLeaveAllocation.getAllocatedDays()).isEqualTo(UPDATED_ALLOCATED_DAYS);
    }

    @Test
    @Transactional
    void putNonExistingLeaveAllocation() throws Exception {
        int databaseSizeBeforeUpdate = leaveAllocationRepository.findAll().size();
        leaveAllocation.setId(count.incrementAndGet());

        // Create the LeaveAllocation
        LeaveAllocationDTO leaveAllocationDTO = leaveAllocationMapper.toDto(leaveAllocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaveAllocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveAllocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveAllocation in the database
        List<LeaveAllocation> leaveAllocationList = leaveAllocationRepository.findAll();
        assertThat(leaveAllocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaveAllocation() throws Exception {
        int databaseSizeBeforeUpdate = leaveAllocationRepository.findAll().size();
        leaveAllocation.setId(count.incrementAndGet());

        // Create the LeaveAllocation
        LeaveAllocationDTO leaveAllocationDTO = leaveAllocationMapper.toDto(leaveAllocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveAllocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveAllocation in the database
        List<LeaveAllocation> leaveAllocationList = leaveAllocationRepository.findAll();
        assertThat(leaveAllocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaveAllocation() throws Exception {
        int databaseSizeBeforeUpdate = leaveAllocationRepository.findAll().size();
        leaveAllocation.setId(count.incrementAndGet());

        // Create the LeaveAllocation
        LeaveAllocationDTO leaveAllocationDTO = leaveAllocationMapper.toDto(leaveAllocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveAllocationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveAllocationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaveAllocation in the database
        List<LeaveAllocation> leaveAllocationList = leaveAllocationRepository.findAll();
        assertThat(leaveAllocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLeaveAllocationWithPatch() throws Exception {
        // Initialize the database
        leaveAllocationRepository.saveAndFlush(leaveAllocation);

        int databaseSizeBeforeUpdate = leaveAllocationRepository.findAll().size();

        // Update the leaveAllocation using partial update
        LeaveAllocation partialUpdatedLeaveAllocation = new LeaveAllocation();
        partialUpdatedLeaveAllocation.setId(leaveAllocation.getId());

        partialUpdatedLeaveAllocation.allocatedDays(UPDATED_ALLOCATED_DAYS);

        restLeaveAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaveAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaveAllocation))
            )
            .andExpect(status().isOk());

        // Validate the LeaveAllocation in the database
        List<LeaveAllocation> leaveAllocationList = leaveAllocationRepository.findAll();
        assertThat(leaveAllocationList).hasSize(databaseSizeBeforeUpdate);
        LeaveAllocation testLeaveAllocation = leaveAllocationList.get(leaveAllocationList.size() - 1);
        assertThat(testLeaveAllocation.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testLeaveAllocation.getLeaveType()).isEqualTo(DEFAULT_LEAVE_TYPE);
        assertThat(testLeaveAllocation.getAllocatedDays()).isEqualTo(UPDATED_ALLOCATED_DAYS);
    }

    @Test
    @Transactional
    void fullUpdateLeaveAllocationWithPatch() throws Exception {
        // Initialize the database
        leaveAllocationRepository.saveAndFlush(leaveAllocation);

        int databaseSizeBeforeUpdate = leaveAllocationRepository.findAll().size();

        // Update the leaveAllocation using partial update
        LeaveAllocation partialUpdatedLeaveAllocation = new LeaveAllocation();
        partialUpdatedLeaveAllocation.setId(leaveAllocation.getId());

        partialUpdatedLeaveAllocation.year(UPDATED_YEAR).leaveType(UPDATED_LEAVE_TYPE).allocatedDays(UPDATED_ALLOCATED_DAYS);

        restLeaveAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaveAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaveAllocation))
            )
            .andExpect(status().isOk());

        // Validate the LeaveAllocation in the database
        List<LeaveAllocation> leaveAllocationList = leaveAllocationRepository.findAll();
        assertThat(leaveAllocationList).hasSize(databaseSizeBeforeUpdate);
        LeaveAllocation testLeaveAllocation = leaveAllocationList.get(leaveAllocationList.size() - 1);
        assertThat(testLeaveAllocation.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testLeaveAllocation.getLeaveType()).isEqualTo(UPDATED_LEAVE_TYPE);
        assertThat(testLeaveAllocation.getAllocatedDays()).isEqualTo(UPDATED_ALLOCATED_DAYS);
    }

    @Test
    @Transactional
    void patchNonExistingLeaveAllocation() throws Exception {
        int databaseSizeBeforeUpdate = leaveAllocationRepository.findAll().size();
        leaveAllocation.setId(count.incrementAndGet());

        // Create the LeaveAllocation
        LeaveAllocationDTO leaveAllocationDTO = leaveAllocationMapper.toDto(leaveAllocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaveAllocationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveAllocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveAllocation in the database
        List<LeaveAllocation> leaveAllocationList = leaveAllocationRepository.findAll();
        assertThat(leaveAllocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaveAllocation() throws Exception {
        int databaseSizeBeforeUpdate = leaveAllocationRepository.findAll().size();
        leaveAllocation.setId(count.incrementAndGet());

        // Create the LeaveAllocation
        LeaveAllocationDTO leaveAllocationDTO = leaveAllocationMapper.toDto(leaveAllocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveAllocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveAllocation in the database
        List<LeaveAllocation> leaveAllocationList = leaveAllocationRepository.findAll();
        assertThat(leaveAllocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaveAllocation() throws Exception {
        int databaseSizeBeforeUpdate = leaveAllocationRepository.findAll().size();
        leaveAllocation.setId(count.incrementAndGet());

        // Create the LeaveAllocation
        LeaveAllocationDTO leaveAllocationDTO = leaveAllocationMapper.toDto(leaveAllocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveAllocationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaveAllocation in the database
        List<LeaveAllocation> leaveAllocationList = leaveAllocationRepository.findAll();
        assertThat(leaveAllocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLeaveAllocation() throws Exception {
        // Initialize the database
        leaveAllocationRepository.saveAndFlush(leaveAllocation);

        int databaseSizeBeforeDelete = leaveAllocationRepository.findAll().size();

        // Delete the leaveAllocation
        restLeaveAllocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaveAllocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaveAllocation> leaveAllocationList = leaveAllocationRepository.findAll();
        assertThat(leaveAllocationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
