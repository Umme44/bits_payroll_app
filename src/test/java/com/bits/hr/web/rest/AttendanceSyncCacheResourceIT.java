package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.AttendanceSyncCache;
import com.bits.hr.repository.AttendanceSyncCacheRepository;
import com.bits.hr.service.dto.AttendanceSyncCacheDTO;
import com.bits.hr.service.mapper.AttendanceSyncCacheMapper;
import java.time.Instant;
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
 * Integration tests for the {@link AttendanceSyncCacheResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttendanceSyncCacheResourceIT {

    private static final String DEFAULT_EMPLOYEE_PIN = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_PIN = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_TERMINAL = 1;
    private static final Integer UPDATED_TERMINAL = 2;

    private static final String ENTITY_API_URL = "/api/attendance-sync-caches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttendanceSyncCacheRepository attendanceSyncCacheRepository;

    @Autowired
    private AttendanceSyncCacheMapper attendanceSyncCacheMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttendanceSyncCacheMockMvc;

    private AttendanceSyncCache attendanceSyncCache;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttendanceSyncCache createEntity(EntityManager em) {
        AttendanceSyncCache attendanceSyncCache = new AttendanceSyncCache()
            .employeePin(DEFAULT_EMPLOYEE_PIN)
            .timestamp(DEFAULT_TIMESTAMP)
            .terminal(DEFAULT_TERMINAL);
        return attendanceSyncCache;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttendanceSyncCache createUpdatedEntity(EntityManager em) {
        AttendanceSyncCache attendanceSyncCache = new AttendanceSyncCache()
            .employeePin(UPDATED_EMPLOYEE_PIN)
            .timestamp(UPDATED_TIMESTAMP)
            .terminal(UPDATED_TERMINAL);
        return attendanceSyncCache;
    }

    @BeforeEach
    public void initTest() {
        attendanceSyncCache = createEntity(em);
    }

    @Test
    @Transactional
    void createAttendanceSyncCache() throws Exception {
        int databaseSizeBeforeCreate = attendanceSyncCacheRepository.findAll().size();
        // Create the AttendanceSyncCache
        AttendanceSyncCacheDTO attendanceSyncCacheDTO = attendanceSyncCacheMapper.toDto(attendanceSyncCache);
        restAttendanceSyncCacheMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSyncCacheDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AttendanceSyncCache in the database
        List<AttendanceSyncCache> attendanceSyncCacheList = attendanceSyncCacheRepository.findAll();
        assertThat(attendanceSyncCacheList).hasSize(databaseSizeBeforeCreate + 1);
        AttendanceSyncCache testAttendanceSyncCache = attendanceSyncCacheList.get(attendanceSyncCacheList.size() - 1);
        assertThat(testAttendanceSyncCache.getEmployeePin()).isEqualTo(DEFAULT_EMPLOYEE_PIN);
        assertThat(testAttendanceSyncCache.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testAttendanceSyncCache.getTerminal()).isEqualTo(DEFAULT_TERMINAL);
    }

    @Test
    @Transactional
    void createAttendanceSyncCacheWithExistingId() throws Exception {
        // Create the AttendanceSyncCache with an existing ID
        attendanceSyncCache.setId(1L);
        AttendanceSyncCacheDTO attendanceSyncCacheDTO = attendanceSyncCacheMapper.toDto(attendanceSyncCache);

        int databaseSizeBeforeCreate = attendanceSyncCacheRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttendanceSyncCacheMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSyncCacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttendanceSyncCache in the database
        List<AttendanceSyncCache> attendanceSyncCacheList = attendanceSyncCacheRepository.findAll();
        assertThat(attendanceSyncCacheList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAttendanceSyncCaches() throws Exception {
        // Initialize the database
        attendanceSyncCacheRepository.saveAndFlush(attendanceSyncCache);

        // Get all the attendanceSyncCacheList
        restAttendanceSyncCacheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attendanceSyncCache.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeePin").value(hasItem(DEFAULT_EMPLOYEE_PIN)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].terminal").value(hasItem(DEFAULT_TERMINAL)));
    }

    @Test
    @Transactional
    void getAttendanceSyncCache() throws Exception {
        // Initialize the database
        attendanceSyncCacheRepository.saveAndFlush(attendanceSyncCache);

        // Get the attendanceSyncCache
        restAttendanceSyncCacheMockMvc
            .perform(get(ENTITY_API_URL_ID, attendanceSyncCache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attendanceSyncCache.getId().intValue()))
            .andExpect(jsonPath("$.employeePin").value(DEFAULT_EMPLOYEE_PIN))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.terminal").value(DEFAULT_TERMINAL));
    }

    @Test
    @Transactional
    void getNonExistingAttendanceSyncCache() throws Exception {
        // Get the attendanceSyncCache
        restAttendanceSyncCacheMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAttendanceSyncCache() throws Exception {
        // Initialize the database
        attendanceSyncCacheRepository.saveAndFlush(attendanceSyncCache);

        int databaseSizeBeforeUpdate = attendanceSyncCacheRepository.findAll().size();

        // Update the attendanceSyncCache
        AttendanceSyncCache updatedAttendanceSyncCache = attendanceSyncCacheRepository.findById(attendanceSyncCache.getId()).get();
        // Disconnect from session so that the updates on updatedAttendanceSyncCache are not directly saved in db
        em.detach(updatedAttendanceSyncCache);
        updatedAttendanceSyncCache.employeePin(UPDATED_EMPLOYEE_PIN).timestamp(UPDATED_TIMESTAMP).terminal(UPDATED_TERMINAL);
        AttendanceSyncCacheDTO attendanceSyncCacheDTO = attendanceSyncCacheMapper.toDto(updatedAttendanceSyncCache);

        restAttendanceSyncCacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attendanceSyncCacheDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSyncCacheDTO))
            )
            .andExpect(status().isOk());

        // Validate the AttendanceSyncCache in the database
        List<AttendanceSyncCache> attendanceSyncCacheList = attendanceSyncCacheRepository.findAll();
        assertThat(attendanceSyncCacheList).hasSize(databaseSizeBeforeUpdate);
        AttendanceSyncCache testAttendanceSyncCache = attendanceSyncCacheList.get(attendanceSyncCacheList.size() - 1);
        assertThat(testAttendanceSyncCache.getEmployeePin()).isEqualTo(UPDATED_EMPLOYEE_PIN);
        assertThat(testAttendanceSyncCache.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testAttendanceSyncCache.getTerminal()).isEqualTo(UPDATED_TERMINAL);
    }

    @Test
    @Transactional
    void putNonExistingAttendanceSyncCache() throws Exception {
        int databaseSizeBeforeUpdate = attendanceSyncCacheRepository.findAll().size();
        attendanceSyncCache.setId(count.incrementAndGet());

        // Create the AttendanceSyncCache
        AttendanceSyncCacheDTO attendanceSyncCacheDTO = attendanceSyncCacheMapper.toDto(attendanceSyncCache);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttendanceSyncCacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attendanceSyncCacheDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSyncCacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttendanceSyncCache in the database
        List<AttendanceSyncCache> attendanceSyncCacheList = attendanceSyncCacheRepository.findAll();
        assertThat(attendanceSyncCacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttendanceSyncCache() throws Exception {
        int databaseSizeBeforeUpdate = attendanceSyncCacheRepository.findAll().size();
        attendanceSyncCache.setId(count.incrementAndGet());

        // Create the AttendanceSyncCache
        AttendanceSyncCacheDTO attendanceSyncCacheDTO = attendanceSyncCacheMapper.toDto(attendanceSyncCache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceSyncCacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSyncCacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttendanceSyncCache in the database
        List<AttendanceSyncCache> attendanceSyncCacheList = attendanceSyncCacheRepository.findAll();
        assertThat(attendanceSyncCacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttendanceSyncCache() throws Exception {
        int databaseSizeBeforeUpdate = attendanceSyncCacheRepository.findAll().size();
        attendanceSyncCache.setId(count.incrementAndGet());

        // Create the AttendanceSyncCache
        AttendanceSyncCacheDTO attendanceSyncCacheDTO = attendanceSyncCacheMapper.toDto(attendanceSyncCache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceSyncCacheMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSyncCacheDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttendanceSyncCache in the database
        List<AttendanceSyncCache> attendanceSyncCacheList = attendanceSyncCacheRepository.findAll();
        assertThat(attendanceSyncCacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttendanceSyncCacheWithPatch() throws Exception {
        // Initialize the database
        attendanceSyncCacheRepository.saveAndFlush(attendanceSyncCache);

        int databaseSizeBeforeUpdate = attendanceSyncCacheRepository.findAll().size();

        // Update the attendanceSyncCache using partial update
        AttendanceSyncCache partialUpdatedAttendanceSyncCache = new AttendanceSyncCache();
        partialUpdatedAttendanceSyncCache.setId(attendanceSyncCache.getId());

        partialUpdatedAttendanceSyncCache.employeePin(UPDATED_EMPLOYEE_PIN);

        restAttendanceSyncCacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttendanceSyncCache.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttendanceSyncCache))
            )
            .andExpect(status().isOk());

        // Validate the AttendanceSyncCache in the database
        List<AttendanceSyncCache> attendanceSyncCacheList = attendanceSyncCacheRepository.findAll();
        assertThat(attendanceSyncCacheList).hasSize(databaseSizeBeforeUpdate);
        AttendanceSyncCache testAttendanceSyncCache = attendanceSyncCacheList.get(attendanceSyncCacheList.size() - 1);
        assertThat(testAttendanceSyncCache.getEmployeePin()).isEqualTo(UPDATED_EMPLOYEE_PIN);
        assertThat(testAttendanceSyncCache.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testAttendanceSyncCache.getTerminal()).isEqualTo(DEFAULT_TERMINAL);
    }

    @Test
    @Transactional
    void fullUpdateAttendanceSyncCacheWithPatch() throws Exception {
        // Initialize the database
        attendanceSyncCacheRepository.saveAndFlush(attendanceSyncCache);

        int databaseSizeBeforeUpdate = attendanceSyncCacheRepository.findAll().size();

        // Update the attendanceSyncCache using partial update
        AttendanceSyncCache partialUpdatedAttendanceSyncCache = new AttendanceSyncCache();
        partialUpdatedAttendanceSyncCache.setId(attendanceSyncCache.getId());

        partialUpdatedAttendanceSyncCache.employeePin(UPDATED_EMPLOYEE_PIN).timestamp(UPDATED_TIMESTAMP).terminal(UPDATED_TERMINAL);

        restAttendanceSyncCacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttendanceSyncCache.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttendanceSyncCache))
            )
            .andExpect(status().isOk());

        // Validate the AttendanceSyncCache in the database
        List<AttendanceSyncCache> attendanceSyncCacheList = attendanceSyncCacheRepository.findAll();
        assertThat(attendanceSyncCacheList).hasSize(databaseSizeBeforeUpdate);
        AttendanceSyncCache testAttendanceSyncCache = attendanceSyncCacheList.get(attendanceSyncCacheList.size() - 1);
        assertThat(testAttendanceSyncCache.getEmployeePin()).isEqualTo(UPDATED_EMPLOYEE_PIN);
        assertThat(testAttendanceSyncCache.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testAttendanceSyncCache.getTerminal()).isEqualTo(UPDATED_TERMINAL);
    }

    @Test
    @Transactional
    void patchNonExistingAttendanceSyncCache() throws Exception {
        int databaseSizeBeforeUpdate = attendanceSyncCacheRepository.findAll().size();
        attendanceSyncCache.setId(count.incrementAndGet());

        // Create the AttendanceSyncCache
        AttendanceSyncCacheDTO attendanceSyncCacheDTO = attendanceSyncCacheMapper.toDto(attendanceSyncCache);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttendanceSyncCacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attendanceSyncCacheDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSyncCacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttendanceSyncCache in the database
        List<AttendanceSyncCache> attendanceSyncCacheList = attendanceSyncCacheRepository.findAll();
        assertThat(attendanceSyncCacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttendanceSyncCache() throws Exception {
        int databaseSizeBeforeUpdate = attendanceSyncCacheRepository.findAll().size();
        attendanceSyncCache.setId(count.incrementAndGet());

        // Create the AttendanceSyncCache
        AttendanceSyncCacheDTO attendanceSyncCacheDTO = attendanceSyncCacheMapper.toDto(attendanceSyncCache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceSyncCacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSyncCacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttendanceSyncCache in the database
        List<AttendanceSyncCache> attendanceSyncCacheList = attendanceSyncCacheRepository.findAll();
        assertThat(attendanceSyncCacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttendanceSyncCache() throws Exception {
        int databaseSizeBeforeUpdate = attendanceSyncCacheRepository.findAll().size();
        attendanceSyncCache.setId(count.incrementAndGet());

        // Create the AttendanceSyncCache
        AttendanceSyncCacheDTO attendanceSyncCacheDTO = attendanceSyncCacheMapper.toDto(attendanceSyncCache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttendanceSyncCacheMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attendanceSyncCacheDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttendanceSyncCache in the database
        List<AttendanceSyncCache> attendanceSyncCacheList = attendanceSyncCacheRepository.findAll();
        assertThat(attendanceSyncCacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttendanceSyncCache() throws Exception {
        // Initialize the database
        attendanceSyncCacheRepository.saveAndFlush(attendanceSyncCache);

        int databaseSizeBeforeDelete = attendanceSyncCacheRepository.findAll().size();

        // Delete the attendanceSyncCache
        restAttendanceSyncCacheMockMvc
            .perform(delete(ENTITY_API_URL_ID, attendanceSyncCache.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttendanceSyncCache> attendanceSyncCacheList = attendanceSyncCacheRepository.findAll();
        assertThat(attendanceSyncCacheList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
