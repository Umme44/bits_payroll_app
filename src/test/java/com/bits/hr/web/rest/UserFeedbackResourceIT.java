package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.UserFeedback;
import com.bits.hr.repository.UserFeedbackRepository;
import com.bits.hr.service.UserFeedbackService;
import com.bits.hr.service.dto.UserFeedbackDTO;
import com.bits.hr.service.mapper.UserFeedbackMapper;
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
 * Integration tests for the {@link UserFeedbackResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserFeedbackResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    private static final String DEFAULT_SUGGESTION = "AAAAAAAAAA";
    private static final String UPDATED_SUGGESTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-feedbacks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserFeedbackRepository userFeedbackRepository;

    @Mock
    private UserFeedbackRepository userFeedbackRepositoryMock;

    @Autowired
    private UserFeedbackMapper userFeedbackMapper;

    @Mock
    private UserFeedbackService userFeedbackServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserFeedbackMockMvc;

    private UserFeedback userFeedback;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserFeedback createEntity(EntityManager em) {
        UserFeedback userFeedback = new UserFeedback().rating(DEFAULT_RATING).suggestion(DEFAULT_SUGGESTION);
        return userFeedback;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserFeedback createUpdatedEntity(EntityManager em) {
        UserFeedback userFeedback = new UserFeedback().rating(UPDATED_RATING).suggestion(UPDATED_SUGGESTION);
        return userFeedback;
    }

    @BeforeEach
    public void initTest() {
        userFeedback = createEntity(em);
    }

    @Test
    @Transactional
    void createUserFeedback() throws Exception {
        int databaseSizeBeforeCreate = userFeedbackRepository.findAll().size();
        // Create the UserFeedback
        UserFeedbackDTO userFeedbackDTO = userFeedbackMapper.toDto(userFeedback);
        restUserFeedbackMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userFeedbackDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserFeedback in the database
        List<UserFeedback> userFeedbackList = userFeedbackRepository.findAll();
        assertThat(userFeedbackList).hasSize(databaseSizeBeforeCreate + 1);
        UserFeedback testUserFeedback = userFeedbackList.get(userFeedbackList.size() - 1);
        assertThat(testUserFeedback.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testUserFeedback.getSuggestion()).isEqualTo(DEFAULT_SUGGESTION);
    }

    @Test
    @Transactional
    void createUserFeedbackWithExistingId() throws Exception {
        // Create the UserFeedback with an existing ID
        userFeedback.setId(1L);
        UserFeedbackDTO userFeedbackDTO = userFeedbackMapper.toDto(userFeedback);

        int databaseSizeBeforeCreate = userFeedbackRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserFeedbackMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userFeedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserFeedback in the database
        List<UserFeedback> userFeedbackList = userFeedbackRepository.findAll();
        assertThat(userFeedbackList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = userFeedbackRepository.findAll().size();
        // set the field null
        userFeedback.setRating(null);

        // Create the UserFeedback, which fails.
        UserFeedbackDTO userFeedbackDTO = userFeedbackMapper.toDto(userFeedback);

        restUserFeedbackMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userFeedbackDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserFeedback> userFeedbackList = userFeedbackRepository.findAll();
        assertThat(userFeedbackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserFeedbacks() throws Exception {
        // Initialize the database
        userFeedbackRepository.saveAndFlush(userFeedback);

        // Get all the userFeedbackList
        restUserFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userFeedback.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].suggestion").value(hasItem(DEFAULT_SUGGESTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserFeedbacksWithEagerRelationshipsIsEnabled() throws Exception {
        when(userFeedbackServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserFeedbackMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userFeedbackServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserFeedbacksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userFeedbackServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserFeedbackMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userFeedbackRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserFeedback() throws Exception {
        // Initialize the database
        userFeedbackRepository.saveAndFlush(userFeedback);

        // Get the userFeedback
        restUserFeedbackMockMvc
            .perform(get(ENTITY_API_URL_ID, userFeedback.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userFeedback.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.suggestion").value(DEFAULT_SUGGESTION));
    }

    @Test
    @Transactional
    void getNonExistingUserFeedback() throws Exception {
        // Get the userFeedback
        restUserFeedbackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserFeedback() throws Exception {
        // Initialize the database
        userFeedbackRepository.saveAndFlush(userFeedback);

        int databaseSizeBeforeUpdate = userFeedbackRepository.findAll().size();

        // Update the userFeedback
        UserFeedback updatedUserFeedback = userFeedbackRepository.findById(userFeedback.getId()).get();
        // Disconnect from session so that the updates on updatedUserFeedback are not directly saved in db
        em.detach(updatedUserFeedback);
        updatedUserFeedback.rating(UPDATED_RATING).suggestion(UPDATED_SUGGESTION);
        UserFeedbackDTO userFeedbackDTO = userFeedbackMapper.toDto(updatedUserFeedback);

        restUserFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userFeedbackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userFeedbackDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserFeedback in the database
        List<UserFeedback> userFeedbackList = userFeedbackRepository.findAll();
        assertThat(userFeedbackList).hasSize(databaseSizeBeforeUpdate);
        UserFeedback testUserFeedback = userFeedbackList.get(userFeedbackList.size() - 1);
        assertThat(testUserFeedback.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testUserFeedback.getSuggestion()).isEqualTo(UPDATED_SUGGESTION);
    }

    @Test
    @Transactional
    void putNonExistingUserFeedback() throws Exception {
        int databaseSizeBeforeUpdate = userFeedbackRepository.findAll().size();
        userFeedback.setId(count.incrementAndGet());

        // Create the UserFeedback
        UserFeedbackDTO userFeedbackDTO = userFeedbackMapper.toDto(userFeedback);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userFeedbackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userFeedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserFeedback in the database
        List<UserFeedback> userFeedbackList = userFeedbackRepository.findAll();
        assertThat(userFeedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserFeedback() throws Exception {
        int databaseSizeBeforeUpdate = userFeedbackRepository.findAll().size();
        userFeedback.setId(count.incrementAndGet());

        // Create the UserFeedback
        UserFeedbackDTO userFeedbackDTO = userFeedbackMapper.toDto(userFeedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userFeedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserFeedback in the database
        List<UserFeedback> userFeedbackList = userFeedbackRepository.findAll();
        assertThat(userFeedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserFeedback() throws Exception {
        int databaseSizeBeforeUpdate = userFeedbackRepository.findAll().size();
        userFeedback.setId(count.incrementAndGet());

        // Create the UserFeedback
        UserFeedbackDTO userFeedbackDTO = userFeedbackMapper.toDto(userFeedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userFeedbackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserFeedback in the database
        List<UserFeedback> userFeedbackList = userFeedbackRepository.findAll();
        assertThat(userFeedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserFeedbackWithPatch() throws Exception {
        // Initialize the database
        userFeedbackRepository.saveAndFlush(userFeedback);

        int databaseSizeBeforeUpdate = userFeedbackRepository.findAll().size();

        // Update the userFeedback using partial update
        UserFeedback partialUpdatedUserFeedback = new UserFeedback();
        partialUpdatedUserFeedback.setId(userFeedback.getId());

        partialUpdatedUserFeedback.rating(UPDATED_RATING);

        restUserFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserFeedback.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserFeedback))
            )
            .andExpect(status().isOk());

        // Validate the UserFeedback in the database
        List<UserFeedback> userFeedbackList = userFeedbackRepository.findAll();
        assertThat(userFeedbackList).hasSize(databaseSizeBeforeUpdate);
        UserFeedback testUserFeedback = userFeedbackList.get(userFeedbackList.size() - 1);
        assertThat(testUserFeedback.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testUserFeedback.getSuggestion()).isEqualTo(DEFAULT_SUGGESTION);
    }

    @Test
    @Transactional
    void fullUpdateUserFeedbackWithPatch() throws Exception {
        // Initialize the database
        userFeedbackRepository.saveAndFlush(userFeedback);

        int databaseSizeBeforeUpdate = userFeedbackRepository.findAll().size();

        // Update the userFeedback using partial update
        UserFeedback partialUpdatedUserFeedback = new UserFeedback();
        partialUpdatedUserFeedback.setId(userFeedback.getId());

        partialUpdatedUserFeedback.rating(UPDATED_RATING).suggestion(UPDATED_SUGGESTION);

        restUserFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserFeedback.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserFeedback))
            )
            .andExpect(status().isOk());

        // Validate the UserFeedback in the database
        List<UserFeedback> userFeedbackList = userFeedbackRepository.findAll();
        assertThat(userFeedbackList).hasSize(databaseSizeBeforeUpdate);
        UserFeedback testUserFeedback = userFeedbackList.get(userFeedbackList.size() - 1);
        assertThat(testUserFeedback.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testUserFeedback.getSuggestion()).isEqualTo(UPDATED_SUGGESTION);
    }

    @Test
    @Transactional
    void patchNonExistingUserFeedback() throws Exception {
        int databaseSizeBeforeUpdate = userFeedbackRepository.findAll().size();
        userFeedback.setId(count.incrementAndGet());

        // Create the UserFeedback
        UserFeedbackDTO userFeedbackDTO = userFeedbackMapper.toDto(userFeedback);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userFeedbackDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userFeedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserFeedback in the database
        List<UserFeedback> userFeedbackList = userFeedbackRepository.findAll();
        assertThat(userFeedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserFeedback() throws Exception {
        int databaseSizeBeforeUpdate = userFeedbackRepository.findAll().size();
        userFeedback.setId(count.incrementAndGet());

        // Create the UserFeedback
        UserFeedbackDTO userFeedbackDTO = userFeedbackMapper.toDto(userFeedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userFeedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserFeedback in the database
        List<UserFeedback> userFeedbackList = userFeedbackRepository.findAll();
        assertThat(userFeedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserFeedback() throws Exception {
        int databaseSizeBeforeUpdate = userFeedbackRepository.findAll().size();
        userFeedback.setId(count.incrementAndGet());

        // Create the UserFeedback
        UserFeedbackDTO userFeedbackDTO = userFeedbackMapper.toDto(userFeedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userFeedbackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserFeedback in the database
        List<UserFeedback> userFeedbackList = userFeedbackRepository.findAll();
        assertThat(userFeedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserFeedback() throws Exception {
        // Initialize the database
        userFeedbackRepository.saveAndFlush(userFeedback);

        int databaseSizeBeforeDelete = userFeedbackRepository.findAll().size();

        // Delete the userFeedback
        restUserFeedbackMockMvc
            .perform(delete(ENTITY_API_URL_ID, userFeedback.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserFeedback> userFeedbackList = userFeedbackRepository.findAll();
        assertThat(userFeedbackList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
