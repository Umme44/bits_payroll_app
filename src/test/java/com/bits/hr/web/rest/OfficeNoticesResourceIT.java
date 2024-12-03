package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.OfficeNotices;
import com.bits.hr.domain.enumeration.NoticeStatus;
import com.bits.hr.repository.OfficeNoticesRepository;
import com.bits.hr.service.dto.OfficeNoticesDTO;
import com.bits.hr.service.mapper.OfficeNoticesMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link OfficeNoticesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OfficeNoticesResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final NoticeStatus DEFAULT_STATUS = NoticeStatus.PUBLISHED;
    private static final NoticeStatus UPDATED_STATUS = NoticeStatus.UNPUBLISHED;

    private static final LocalDate DEFAULT_PUBLISH_FORM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLISH_FORM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PUBLISH_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLISH_TO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/office-notices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OfficeNoticesRepository officeNoticesRepository;

    @Autowired
    private OfficeNoticesMapper officeNoticesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOfficeNoticesMockMvc;

    private OfficeNotices officeNotices;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OfficeNotices createEntity(EntityManager em) {
        OfficeNotices officeNotices = new OfficeNotices()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .publishForm(DEFAULT_PUBLISH_FORM)
            .publishTo(DEFAULT_PUBLISH_TO)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return officeNotices;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OfficeNotices createUpdatedEntity(EntityManager em) {
        OfficeNotices officeNotices = new OfficeNotices()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .publishForm(UPDATED_PUBLISH_FORM)
            .publishTo(UPDATED_PUBLISH_TO)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return officeNotices;
    }

    @BeforeEach
    public void initTest() {
        officeNotices = createEntity(em);
    }

    @Test
    @Transactional
    void createOfficeNotices() throws Exception {
        int databaseSizeBeforeCreate = officeNoticesRepository.findAll().size();
        // Create the OfficeNotices
        OfficeNoticesDTO officeNoticesDTO = officeNoticesMapper.toDto(officeNotices);
        restOfficeNoticesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(officeNoticesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OfficeNotices in the database
        List<OfficeNotices> officeNoticesList = officeNoticesRepository.findAll();
        assertThat(officeNoticesList).hasSize(databaseSizeBeforeCreate + 1);
        OfficeNotices testOfficeNotices = officeNoticesList.get(officeNoticesList.size() - 1);
        assertThat(testOfficeNotices.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOfficeNotices.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOfficeNotices.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOfficeNotices.getPublishForm()).isEqualTo(DEFAULT_PUBLISH_FORM);
        assertThat(testOfficeNotices.getPublishTo()).isEqualTo(DEFAULT_PUBLISH_TO);
        assertThat(testOfficeNotices.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testOfficeNotices.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testOfficeNotices.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOfficeNotices.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createOfficeNoticesWithExistingId() throws Exception {
        // Create the OfficeNotices with an existing ID
        officeNotices.setId(1L);
        OfficeNoticesDTO officeNoticesDTO = officeNoticesMapper.toDto(officeNotices);

        int databaseSizeBeforeCreate = officeNoticesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfficeNoticesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(officeNoticesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfficeNotices in the database
        List<OfficeNotices> officeNoticesList = officeNoticesRepository.findAll();
        assertThat(officeNoticesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = officeNoticesRepository.findAll().size();
        // set the field null
        officeNotices.setTitle(null);

        // Create the OfficeNotices, which fails.
        OfficeNoticesDTO officeNoticesDTO = officeNoticesMapper.toDto(officeNotices);

        restOfficeNoticesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(officeNoticesDTO))
            )
            .andExpect(status().isBadRequest());

        List<OfficeNotices> officeNoticesList = officeNoticesRepository.findAll();
        assertThat(officeNoticesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOfficeNotices() throws Exception {
        // Initialize the database
        officeNoticesRepository.saveAndFlush(officeNotices);

        // Get all the officeNoticesList
        restOfficeNoticesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(officeNotices.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].publishForm").value(hasItem(DEFAULT_PUBLISH_FORM.toString())))
            .andExpect(jsonPath("$.[*].publishTo").value(hasItem(DEFAULT_PUBLISH_TO.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getOfficeNotices() throws Exception {
        // Initialize the database
        officeNoticesRepository.saveAndFlush(officeNotices);

        // Get the officeNotices
        restOfficeNoticesMockMvc
            .perform(get(ENTITY_API_URL_ID, officeNotices.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(officeNotices.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.publishForm").value(DEFAULT_PUBLISH_FORM.toString()))
            .andExpect(jsonPath("$.publishTo").value(DEFAULT_PUBLISH_TO.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingOfficeNotices() throws Exception {
        // Get the officeNotices
        restOfficeNoticesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOfficeNotices() throws Exception {
        // Initialize the database
        officeNoticesRepository.saveAndFlush(officeNotices);

        int databaseSizeBeforeUpdate = officeNoticesRepository.findAll().size();

        // Update the officeNotices
        OfficeNotices updatedOfficeNotices = officeNoticesRepository.findById(officeNotices.getId()).get();
        // Disconnect from session so that the updates on updatedOfficeNotices are not directly saved in db
        em.detach(updatedOfficeNotices);
        updatedOfficeNotices
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .publishForm(UPDATED_PUBLISH_FORM)
            .publishTo(UPDATED_PUBLISH_TO)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        OfficeNoticesDTO officeNoticesDTO = officeNoticesMapper.toDto(updatedOfficeNotices);

        restOfficeNoticesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, officeNoticesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(officeNoticesDTO))
            )
            .andExpect(status().isOk());

        // Validate the OfficeNotices in the database
        List<OfficeNotices> officeNoticesList = officeNoticesRepository.findAll();
        assertThat(officeNoticesList).hasSize(databaseSizeBeforeUpdate);
        OfficeNotices testOfficeNotices = officeNoticesList.get(officeNoticesList.size() - 1);
        assertThat(testOfficeNotices.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOfficeNotices.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOfficeNotices.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOfficeNotices.getPublishForm()).isEqualTo(UPDATED_PUBLISH_FORM);
        assertThat(testOfficeNotices.getPublishTo()).isEqualTo(UPDATED_PUBLISH_TO);
        assertThat(testOfficeNotices.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOfficeNotices.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testOfficeNotices.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOfficeNotices.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingOfficeNotices() throws Exception {
        int databaseSizeBeforeUpdate = officeNoticesRepository.findAll().size();
        officeNotices.setId(count.incrementAndGet());

        // Create the OfficeNotices
        OfficeNoticesDTO officeNoticesDTO = officeNoticesMapper.toDto(officeNotices);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfficeNoticesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, officeNoticesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(officeNoticesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfficeNotices in the database
        List<OfficeNotices> officeNoticesList = officeNoticesRepository.findAll();
        assertThat(officeNoticesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOfficeNotices() throws Exception {
        int databaseSizeBeforeUpdate = officeNoticesRepository.findAll().size();
        officeNotices.setId(count.incrementAndGet());

        // Create the OfficeNotices
        OfficeNoticesDTO officeNoticesDTO = officeNoticesMapper.toDto(officeNotices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfficeNoticesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(officeNoticesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfficeNotices in the database
        List<OfficeNotices> officeNoticesList = officeNoticesRepository.findAll();
        assertThat(officeNoticesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOfficeNotices() throws Exception {
        int databaseSizeBeforeUpdate = officeNoticesRepository.findAll().size();
        officeNotices.setId(count.incrementAndGet());

        // Create the OfficeNotices
        OfficeNoticesDTO officeNoticesDTO = officeNoticesMapper.toDto(officeNotices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfficeNoticesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(officeNoticesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OfficeNotices in the database
        List<OfficeNotices> officeNoticesList = officeNoticesRepository.findAll();
        assertThat(officeNoticesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOfficeNoticesWithPatch() throws Exception {
        // Initialize the database
        officeNoticesRepository.saveAndFlush(officeNotices);

        int databaseSizeBeforeUpdate = officeNoticesRepository.findAll().size();

        // Update the officeNotices using partial update
        OfficeNotices partialUpdatedOfficeNotices = new OfficeNotices();
        partialUpdatedOfficeNotices.setId(officeNotices.getId());

        partialUpdatedOfficeNotices.description(UPDATED_DESCRIPTION).publishForm(UPDATED_PUBLISH_FORM);

        restOfficeNoticesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOfficeNotices.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOfficeNotices))
            )
            .andExpect(status().isOk());

        // Validate the OfficeNotices in the database
        List<OfficeNotices> officeNoticesList = officeNoticesRepository.findAll();
        assertThat(officeNoticesList).hasSize(databaseSizeBeforeUpdate);
        OfficeNotices testOfficeNotices = officeNoticesList.get(officeNoticesList.size() - 1);
        assertThat(testOfficeNotices.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOfficeNotices.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOfficeNotices.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOfficeNotices.getPublishForm()).isEqualTo(UPDATED_PUBLISH_FORM);
        assertThat(testOfficeNotices.getPublishTo()).isEqualTo(DEFAULT_PUBLISH_TO);
        assertThat(testOfficeNotices.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testOfficeNotices.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testOfficeNotices.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOfficeNotices.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateOfficeNoticesWithPatch() throws Exception {
        // Initialize the database
        officeNoticesRepository.saveAndFlush(officeNotices);

        int databaseSizeBeforeUpdate = officeNoticesRepository.findAll().size();

        // Update the officeNotices using partial update
        OfficeNotices partialUpdatedOfficeNotices = new OfficeNotices();
        partialUpdatedOfficeNotices.setId(officeNotices.getId());

        partialUpdatedOfficeNotices
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .publishForm(UPDATED_PUBLISH_FORM)
            .publishTo(UPDATED_PUBLISH_TO)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restOfficeNoticesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOfficeNotices.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOfficeNotices))
            )
            .andExpect(status().isOk());

        // Validate the OfficeNotices in the database
        List<OfficeNotices> officeNoticesList = officeNoticesRepository.findAll();
        assertThat(officeNoticesList).hasSize(databaseSizeBeforeUpdate);
        OfficeNotices testOfficeNotices = officeNoticesList.get(officeNoticesList.size() - 1);
        assertThat(testOfficeNotices.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOfficeNotices.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOfficeNotices.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOfficeNotices.getPublishForm()).isEqualTo(UPDATED_PUBLISH_FORM);
        assertThat(testOfficeNotices.getPublishTo()).isEqualTo(UPDATED_PUBLISH_TO);
        assertThat(testOfficeNotices.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOfficeNotices.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testOfficeNotices.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOfficeNotices.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingOfficeNotices() throws Exception {
        int databaseSizeBeforeUpdate = officeNoticesRepository.findAll().size();
        officeNotices.setId(count.incrementAndGet());

        // Create the OfficeNotices
        OfficeNoticesDTO officeNoticesDTO = officeNoticesMapper.toDto(officeNotices);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfficeNoticesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, officeNoticesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(officeNoticesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfficeNotices in the database
        List<OfficeNotices> officeNoticesList = officeNoticesRepository.findAll();
        assertThat(officeNoticesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOfficeNotices() throws Exception {
        int databaseSizeBeforeUpdate = officeNoticesRepository.findAll().size();
        officeNotices.setId(count.incrementAndGet());

        // Create the OfficeNotices
        OfficeNoticesDTO officeNoticesDTO = officeNoticesMapper.toDto(officeNotices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfficeNoticesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(officeNoticesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfficeNotices in the database
        List<OfficeNotices> officeNoticesList = officeNoticesRepository.findAll();
        assertThat(officeNoticesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOfficeNotices() throws Exception {
        int databaseSizeBeforeUpdate = officeNoticesRepository.findAll().size();
        officeNotices.setId(count.incrementAndGet());

        // Create the OfficeNotices
        OfficeNoticesDTO officeNoticesDTO = officeNoticesMapper.toDto(officeNotices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfficeNoticesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(officeNoticesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OfficeNotices in the database
        List<OfficeNotices> officeNoticesList = officeNoticesRepository.findAll();
        assertThat(officeNoticesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOfficeNotices() throws Exception {
        // Initialize the database
        officeNoticesRepository.saveAndFlush(officeNotices);

        int databaseSizeBeforeDelete = officeNoticesRepository.findAll().size();

        // Delete the officeNotices
        restOfficeNoticesMockMvc
            .perform(delete(ENTITY_API_URL_ID, officeNotices.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OfficeNotices> officeNoticesList = officeNoticesRepository.findAll();
        assertThat(officeNoticesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
