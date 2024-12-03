package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.EventLog;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.repository.EventLogRepository;
import com.bits.hr.service.EventLogService;
import com.bits.hr.service.dto.EventLogDTO;
import com.bits.hr.service.mapper.EventLogMapper;
import java.time.Instant;
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
 * Integration tests for the {@link EventLogResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EventLogResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final RequestMethod DEFAULT_REQUEST_METHOD = RequestMethod.GET;
    private static final RequestMethod UPDATED_REQUEST_METHOD = RequestMethod.POST;

    private static final Instant DEFAULT_PERFORMED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PERFORMED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/event-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventLogRepository eventLogRepository;

    @Mock
    private EventLogRepository eventLogRepositoryMock;

    @Autowired
    private EventLogMapper eventLogMapper;

    @Mock
    private EventLogService eventLogServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventLogMockMvc;

    private EventLog eventLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventLog createEntity(EntityManager em) {
        EventLog eventLog = new EventLog()
            .title(DEFAULT_TITLE)
            .requestMethod(DEFAULT_REQUEST_METHOD)
            .performedAt(DEFAULT_PERFORMED_AT)
            .data(DEFAULT_DATA)
            .entityName(DEFAULT_ENTITY_NAME);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        eventLog.setPerformedBy(user);
        return eventLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventLog createUpdatedEntity(EntityManager em) {
        EventLog eventLog = new EventLog()
            .title(UPDATED_TITLE)
            .requestMethod(UPDATED_REQUEST_METHOD)
            .performedAt(UPDATED_PERFORMED_AT)
            .data(UPDATED_DATA)
            .entityName(UPDATED_ENTITY_NAME);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        eventLog.setPerformedBy(user);
        return eventLog;
    }

    @BeforeEach
    public void initTest() {
        eventLog = createEntity(em);
    }

    @Test
    @Transactional
    void createEventLog() throws Exception {
        int databaseSizeBeforeCreate = eventLogRepository.findAll().size();
        // Create the EventLog
        EventLogDTO eventLogDTO = eventLogMapper.toDto(eventLog);
        restEventLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLogDTO)))
            .andExpect(status().isCreated());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeCreate + 1);
        EventLog testEventLog = eventLogList.get(eventLogList.size() - 1);
        assertThat(testEventLog.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEventLog.getRequestMethod()).isEqualTo(DEFAULT_REQUEST_METHOD);
        assertThat(testEventLog.getPerformedAt()).isEqualTo(DEFAULT_PERFORMED_AT);
        assertThat(testEventLog.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testEventLog.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
    }

    @Test
    @Transactional
    void createEventLogWithExistingId() throws Exception {
        // Create the EventLog with an existing ID
        eventLog.setId(1L);
        EventLogDTO eventLogDTO = eventLogMapper.toDto(eventLog);

        int databaseSizeBeforeCreate = eventLogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPerformedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventLogRepository.findAll().size();
        // set the field null
        eventLog.setPerformedAt(null);

        // Create the EventLog, which fails.
        EventLogDTO eventLogDTO = eventLogMapper.toDto(eventLog);

        restEventLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLogDTO)))
            .andExpect(status().isBadRequest());

        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventLogs() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList
        restEventLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].requestMethod").value(hasItem(DEFAULT_REQUEST_METHOD.toString())))
            .andExpect(jsonPath("$.[*].performedAt").value(hasItem(DEFAULT_PERFORMED_AT.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEventLogsWithEagerRelationshipsIsEnabled() throws Exception {
        when(eventLogServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEventLogMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(eventLogServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEventLogsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(eventLogServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEventLogMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(eventLogRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEventLog() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get the eventLog
        restEventLogMockMvc
            .perform(get(ENTITY_API_URL_ID, eventLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventLog.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.requestMethod").value(DEFAULT_REQUEST_METHOD.toString()))
            .andExpect(jsonPath("$.performedAt").value(DEFAULT_PERFORMED_AT.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME));
    }

    @Test
    @Transactional
    void getNonExistingEventLog() throws Exception {
        // Get the eventLog
        restEventLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventLog() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();

        // Update the eventLog
        EventLog updatedEventLog = eventLogRepository.findById(eventLog.getId()).get();
        // Disconnect from session so that the updates on updatedEventLog are not directly saved in db
        em.detach(updatedEventLog);
        updatedEventLog
            .title(UPDATED_TITLE)
            .requestMethod(UPDATED_REQUEST_METHOD)
            .performedAt(UPDATED_PERFORMED_AT)
            .data(UPDATED_DATA)
            .entityName(UPDATED_ENTITY_NAME);
        EventLogDTO eventLogDTO = eventLogMapper.toDto(updatedEventLog);

        restEventLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
        EventLog testEventLog = eventLogList.get(eventLogList.size() - 1);
        assertThat(testEventLog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventLog.getRequestMethod()).isEqualTo(UPDATED_REQUEST_METHOD);
        assertThat(testEventLog.getPerformedAt()).isEqualTo(UPDATED_PERFORMED_AT);
        assertThat(testEventLog.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testEventLog.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingEventLog() throws Exception {
        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();
        eventLog.setId(count.incrementAndGet());

        // Create the EventLog
        EventLogDTO eventLogDTO = eventLogMapper.toDto(eventLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventLog() throws Exception {
        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();
        eventLog.setId(count.incrementAndGet());

        // Create the EventLog
        EventLogDTO eventLogDTO = eventLogMapper.toDto(eventLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventLog() throws Exception {
        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();
        eventLog.setId(count.incrementAndGet());

        // Create the EventLog
        EventLogDTO eventLogDTO = eventLogMapper.toDto(eventLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventLogWithPatch() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();

        // Update the eventLog using partial update
        EventLog partialUpdatedEventLog = new EventLog();
        partialUpdatedEventLog.setId(eventLog.getId());

        partialUpdatedEventLog.title(UPDATED_TITLE).performedAt(UPDATED_PERFORMED_AT);

        restEventLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventLog))
            )
            .andExpect(status().isOk());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
        EventLog testEventLog = eventLogList.get(eventLogList.size() - 1);
        assertThat(testEventLog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventLog.getRequestMethod()).isEqualTo(DEFAULT_REQUEST_METHOD);
        assertThat(testEventLog.getPerformedAt()).isEqualTo(UPDATED_PERFORMED_AT);
        assertThat(testEventLog.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testEventLog.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateEventLogWithPatch() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();

        // Update the eventLog using partial update
        EventLog partialUpdatedEventLog = new EventLog();
        partialUpdatedEventLog.setId(eventLog.getId());

        partialUpdatedEventLog
            .title(UPDATED_TITLE)
            .requestMethod(UPDATED_REQUEST_METHOD)
            .performedAt(UPDATED_PERFORMED_AT)
            .data(UPDATED_DATA)
            .entityName(UPDATED_ENTITY_NAME);

        restEventLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventLog))
            )
            .andExpect(status().isOk());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
        EventLog testEventLog = eventLogList.get(eventLogList.size() - 1);
        assertThat(testEventLog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventLog.getRequestMethod()).isEqualTo(UPDATED_REQUEST_METHOD);
        assertThat(testEventLog.getPerformedAt()).isEqualTo(UPDATED_PERFORMED_AT);
        assertThat(testEventLog.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testEventLog.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingEventLog() throws Exception {
        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();
        eventLog.setId(count.incrementAndGet());

        // Create the EventLog
        EventLogDTO eventLogDTO = eventLogMapper.toDto(eventLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventLog() throws Exception {
        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();
        eventLog.setId(count.incrementAndGet());

        // Create the EventLog
        EventLogDTO eventLogDTO = eventLogMapper.toDto(eventLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventLog() throws Exception {
        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();
        eventLog.setId(count.incrementAndGet());

        // Create the EventLog
        EventLogDTO eventLogDTO = eventLogMapper.toDto(eventLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventLog() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        int databaseSizeBeforeDelete = eventLogRepository.findAll().size();

        // Delete the eventLog
        restEventLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
