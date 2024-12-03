package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.InsuranceClaim;
import com.bits.hr.domain.InsuranceRegistration;
import com.bits.hr.domain.enumeration.ClaimStatus;
import com.bits.hr.repository.InsuranceClaimRepository;
import com.bits.hr.service.InsuranceClaimService;
import com.bits.hr.service.dto.InsuranceClaimDTO;
import com.bits.hr.service.mapper.InsuranceClaimMapper;
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
 * Integration tests for the {@link InsuranceClaimResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InsuranceClaimResourceIT {

    private static final LocalDate DEFAULT_SETTLEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SETTLEMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_REGRET_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REGRET_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REGRET_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REGRET_REASON = "BBBBBBBBBB";

    private static final Double DEFAULT_CLAIMED_AMOUNT = 1D;
    private static final Double UPDATED_CLAIMED_AMOUNT = 2D;

    private static final Double DEFAULT_SETTLED_AMOUNT = 1D;
    private static final Double UPDATED_SETTLED_AMOUNT = 2D;

    private static final ClaimStatus DEFAULT_CLAIM_STATUS = ClaimStatus.SETTLED;
    private static final ClaimStatus UPDATED_CLAIM_STATUS = ClaimStatus.REGRETTED;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/insurance-claims";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InsuranceClaimRepository insuranceClaimRepository;

    @Mock
    private InsuranceClaimRepository insuranceClaimRepositoryMock;

    @Autowired
    private InsuranceClaimMapper insuranceClaimMapper;

    @Mock
    private InsuranceClaimService insuranceClaimServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceClaimMockMvc;

    private InsuranceClaim insuranceClaim;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceClaim createEntity(EntityManager em) {
        InsuranceClaim insuranceClaim = new InsuranceClaim()
            .settlementDate(DEFAULT_SETTLEMENT_DATE)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .regretDate(DEFAULT_REGRET_DATE)
            .regretReason(DEFAULT_REGRET_REASON)
            .claimedAmount(DEFAULT_CLAIMED_AMOUNT)
            .settledAmount(DEFAULT_SETTLED_AMOUNT)
            .claimStatus(DEFAULT_CLAIM_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        InsuranceRegistration insuranceRegistration;
        if (TestUtil.findAll(em, InsuranceRegistration.class).isEmpty()) {
            insuranceRegistration = InsuranceRegistrationResourceIT.createEntity(em);
            em.persist(insuranceRegistration);
            em.flush();
        } else {
            insuranceRegistration = TestUtil.findAll(em, InsuranceRegistration.class).get(0);
        }
        insuranceClaim.setInsuranceRegistration(insuranceRegistration);
        return insuranceClaim;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceClaim createUpdatedEntity(EntityManager em) {
        InsuranceClaim insuranceClaim = new InsuranceClaim()
            .settlementDate(UPDATED_SETTLEMENT_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .regretDate(UPDATED_REGRET_DATE)
            .regretReason(UPDATED_REGRET_REASON)
            .claimedAmount(UPDATED_CLAIMED_AMOUNT)
            .settledAmount(UPDATED_SETTLED_AMOUNT)
            .claimStatus(UPDATED_CLAIM_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        InsuranceRegistration insuranceRegistration;
        if (TestUtil.findAll(em, InsuranceRegistration.class).isEmpty()) {
            insuranceRegistration = InsuranceRegistrationResourceIT.createUpdatedEntity(em);
            em.persist(insuranceRegistration);
            em.flush();
        } else {
            insuranceRegistration = TestUtil.findAll(em, InsuranceRegistration.class).get(0);
        }
        insuranceClaim.setInsuranceRegistration(insuranceRegistration);
        return insuranceClaim;
    }

    @BeforeEach
    public void initTest() {
        insuranceClaim = createEntity(em);
    }

    @Test
    @Transactional
    void createInsuranceClaim() throws Exception {
        int databaseSizeBeforeCreate = insuranceClaimRepository.findAll().size();
        // Create the InsuranceClaim
        InsuranceClaimDTO insuranceClaimDTO = insuranceClaimMapper.toDto(insuranceClaim);
        restInsuranceClaimMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(insuranceClaimDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InsuranceClaim in the database
        List<InsuranceClaim> insuranceClaimList = insuranceClaimRepository.findAll();
        assertThat(insuranceClaimList).hasSize(databaseSizeBeforeCreate + 1);
        InsuranceClaim testInsuranceClaim = insuranceClaimList.get(insuranceClaimList.size() - 1);
        assertThat(testInsuranceClaim.getSettlementDate()).isEqualTo(DEFAULT_SETTLEMENT_DATE);
        assertThat(testInsuranceClaim.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testInsuranceClaim.getRegretDate()).isEqualTo(DEFAULT_REGRET_DATE);
        assertThat(testInsuranceClaim.getRegretReason()).isEqualTo(DEFAULT_REGRET_REASON);
        assertThat(testInsuranceClaim.getClaimedAmount()).isEqualTo(DEFAULT_CLAIMED_AMOUNT);
        assertThat(testInsuranceClaim.getSettledAmount()).isEqualTo(DEFAULT_SETTLED_AMOUNT);
        assertThat(testInsuranceClaim.getClaimStatus()).isEqualTo(DEFAULT_CLAIM_STATUS);
        assertThat(testInsuranceClaim.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testInsuranceClaim.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createInsuranceClaimWithExistingId() throws Exception {
        // Create the InsuranceClaim with an existing ID
        insuranceClaim.setId(1L);
        InsuranceClaimDTO insuranceClaimDTO = insuranceClaimMapper.toDto(insuranceClaim);

        int databaseSizeBeforeCreate = insuranceClaimRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceClaimMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(insuranceClaimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceClaim in the database
        List<InsuranceClaim> insuranceClaimList = insuranceClaimRepository.findAll();
        assertThat(insuranceClaimList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsuranceClaims() throws Exception {
        // Initialize the database
        insuranceClaimRepository.saveAndFlush(insuranceClaim);

        // Get all the insuranceClaimList
        restInsuranceClaimMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceClaim.getId().intValue())))
            .andExpect(jsonPath("$.[*].settlementDate").value(hasItem(DEFAULT_SETTLEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].regretDate").value(hasItem(DEFAULT_REGRET_DATE.toString())))
            .andExpect(jsonPath("$.[*].regretReason").value(hasItem(DEFAULT_REGRET_REASON)))
            .andExpect(jsonPath("$.[*].claimedAmount").value(hasItem(DEFAULT_CLAIMED_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].settledAmount").value(hasItem(DEFAULT_SETTLED_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].claimStatus").value(hasItem(DEFAULT_CLAIM_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInsuranceClaimsWithEagerRelationshipsIsEnabled() throws Exception {
        when(insuranceClaimServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInsuranceClaimMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(insuranceClaimServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInsuranceClaimsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(insuranceClaimServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInsuranceClaimMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(insuranceClaimRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInsuranceClaim() throws Exception {
        // Initialize the database
        insuranceClaimRepository.saveAndFlush(insuranceClaim);

        // Get the insuranceClaim
        restInsuranceClaimMockMvc
            .perform(get(ENTITY_API_URL_ID, insuranceClaim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceClaim.getId().intValue()))
            .andExpect(jsonPath("$.settlementDate").value(DEFAULT_SETTLEMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.regretDate").value(DEFAULT_REGRET_DATE.toString()))
            .andExpect(jsonPath("$.regretReason").value(DEFAULT_REGRET_REASON))
            .andExpect(jsonPath("$.claimedAmount").value(DEFAULT_CLAIMED_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.settledAmount").value(DEFAULT_SETTLED_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.claimStatus").value(DEFAULT_CLAIM_STATUS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInsuranceClaim() throws Exception {
        // Get the insuranceClaim
        restInsuranceClaimMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsuranceClaim() throws Exception {
        // Initialize the database
        insuranceClaimRepository.saveAndFlush(insuranceClaim);

        int databaseSizeBeforeUpdate = insuranceClaimRepository.findAll().size();

        // Update the insuranceClaim
        InsuranceClaim updatedInsuranceClaim = insuranceClaimRepository.findById(insuranceClaim.getId()).get();
        // Disconnect from session so that the updates on updatedInsuranceClaim are not directly saved in db
        em.detach(updatedInsuranceClaim);
        updatedInsuranceClaim
            .settlementDate(UPDATED_SETTLEMENT_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .regretDate(UPDATED_REGRET_DATE)
            .regretReason(UPDATED_REGRET_REASON)
            .claimedAmount(UPDATED_CLAIMED_AMOUNT)
            .settledAmount(UPDATED_SETTLED_AMOUNT)
            .claimStatus(UPDATED_CLAIM_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        InsuranceClaimDTO insuranceClaimDTO = insuranceClaimMapper.toDto(updatedInsuranceClaim);

        restInsuranceClaimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceClaimDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceClaimDTO))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceClaim in the database
        List<InsuranceClaim> insuranceClaimList = insuranceClaimRepository.findAll();
        assertThat(insuranceClaimList).hasSize(databaseSizeBeforeUpdate);
        InsuranceClaim testInsuranceClaim = insuranceClaimList.get(insuranceClaimList.size() - 1);
        assertThat(testInsuranceClaim.getSettlementDate()).isEqualTo(UPDATED_SETTLEMENT_DATE);
        assertThat(testInsuranceClaim.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testInsuranceClaim.getRegretDate()).isEqualTo(UPDATED_REGRET_DATE);
        assertThat(testInsuranceClaim.getRegretReason()).isEqualTo(UPDATED_REGRET_REASON);
        assertThat(testInsuranceClaim.getClaimedAmount()).isEqualTo(UPDATED_CLAIMED_AMOUNT);
        assertThat(testInsuranceClaim.getSettledAmount()).isEqualTo(UPDATED_SETTLED_AMOUNT);
        assertThat(testInsuranceClaim.getClaimStatus()).isEqualTo(UPDATED_CLAIM_STATUS);
        assertThat(testInsuranceClaim.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testInsuranceClaim.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingInsuranceClaim() throws Exception {
        int databaseSizeBeforeUpdate = insuranceClaimRepository.findAll().size();
        insuranceClaim.setId(count.incrementAndGet());

        // Create the InsuranceClaim
        InsuranceClaimDTO insuranceClaimDTO = insuranceClaimMapper.toDto(insuranceClaim);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceClaimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceClaimDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceClaimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceClaim in the database
        List<InsuranceClaim> insuranceClaimList = insuranceClaimRepository.findAll();
        assertThat(insuranceClaimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsuranceClaim() throws Exception {
        int databaseSizeBeforeUpdate = insuranceClaimRepository.findAll().size();
        insuranceClaim.setId(count.incrementAndGet());

        // Create the InsuranceClaim
        InsuranceClaimDTO insuranceClaimDTO = insuranceClaimMapper.toDto(insuranceClaim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceClaimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceClaimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceClaim in the database
        List<InsuranceClaim> insuranceClaimList = insuranceClaimRepository.findAll();
        assertThat(insuranceClaimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsuranceClaim() throws Exception {
        int databaseSizeBeforeUpdate = insuranceClaimRepository.findAll().size();
        insuranceClaim.setId(count.incrementAndGet());

        // Create the InsuranceClaim
        InsuranceClaimDTO insuranceClaimDTO = insuranceClaimMapper.toDto(insuranceClaim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceClaimMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(insuranceClaimDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceClaim in the database
        List<InsuranceClaim> insuranceClaimList = insuranceClaimRepository.findAll();
        assertThat(insuranceClaimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsuranceClaimWithPatch() throws Exception {
        // Initialize the database
        insuranceClaimRepository.saveAndFlush(insuranceClaim);

        int databaseSizeBeforeUpdate = insuranceClaimRepository.findAll().size();

        // Update the insuranceClaim using partial update
        InsuranceClaim partialUpdatedInsuranceClaim = new InsuranceClaim();
        partialUpdatedInsuranceClaim.setId(insuranceClaim.getId());

        partialUpdatedInsuranceClaim
            .paymentDate(UPDATED_PAYMENT_DATE)
            .regretDate(UPDATED_REGRET_DATE)
            .regretReason(UPDATED_REGRET_REASON)
            .settledAmount(UPDATED_SETTLED_AMOUNT)
            .claimStatus(UPDATED_CLAIM_STATUS)
            .updatedAt(UPDATED_UPDATED_AT);

        restInsuranceClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceClaim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInsuranceClaim))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceClaim in the database
        List<InsuranceClaim> insuranceClaimList = insuranceClaimRepository.findAll();
        assertThat(insuranceClaimList).hasSize(databaseSizeBeforeUpdate);
        InsuranceClaim testInsuranceClaim = insuranceClaimList.get(insuranceClaimList.size() - 1);
        assertThat(testInsuranceClaim.getSettlementDate()).isEqualTo(DEFAULT_SETTLEMENT_DATE);
        assertThat(testInsuranceClaim.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testInsuranceClaim.getRegretDate()).isEqualTo(UPDATED_REGRET_DATE);
        assertThat(testInsuranceClaim.getRegretReason()).isEqualTo(UPDATED_REGRET_REASON);
        assertThat(testInsuranceClaim.getClaimedAmount()).isEqualTo(DEFAULT_CLAIMED_AMOUNT);
        assertThat(testInsuranceClaim.getSettledAmount()).isEqualTo(UPDATED_SETTLED_AMOUNT);
        assertThat(testInsuranceClaim.getClaimStatus()).isEqualTo(UPDATED_CLAIM_STATUS);
        assertThat(testInsuranceClaim.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testInsuranceClaim.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateInsuranceClaimWithPatch() throws Exception {
        // Initialize the database
        insuranceClaimRepository.saveAndFlush(insuranceClaim);

        int databaseSizeBeforeUpdate = insuranceClaimRepository.findAll().size();

        // Update the insuranceClaim using partial update
        InsuranceClaim partialUpdatedInsuranceClaim = new InsuranceClaim();
        partialUpdatedInsuranceClaim.setId(insuranceClaim.getId());

        partialUpdatedInsuranceClaim
            .settlementDate(UPDATED_SETTLEMENT_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .regretDate(UPDATED_REGRET_DATE)
            .regretReason(UPDATED_REGRET_REASON)
            .claimedAmount(UPDATED_CLAIMED_AMOUNT)
            .settledAmount(UPDATED_SETTLED_AMOUNT)
            .claimStatus(UPDATED_CLAIM_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restInsuranceClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceClaim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInsuranceClaim))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceClaim in the database
        List<InsuranceClaim> insuranceClaimList = insuranceClaimRepository.findAll();
        assertThat(insuranceClaimList).hasSize(databaseSizeBeforeUpdate);
        InsuranceClaim testInsuranceClaim = insuranceClaimList.get(insuranceClaimList.size() - 1);
        assertThat(testInsuranceClaim.getSettlementDate()).isEqualTo(UPDATED_SETTLEMENT_DATE);
        assertThat(testInsuranceClaim.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testInsuranceClaim.getRegretDate()).isEqualTo(UPDATED_REGRET_DATE);
        assertThat(testInsuranceClaim.getRegretReason()).isEqualTo(UPDATED_REGRET_REASON);
        assertThat(testInsuranceClaim.getClaimedAmount()).isEqualTo(UPDATED_CLAIMED_AMOUNT);
        assertThat(testInsuranceClaim.getSettledAmount()).isEqualTo(UPDATED_SETTLED_AMOUNT);
        assertThat(testInsuranceClaim.getClaimStatus()).isEqualTo(UPDATED_CLAIM_STATUS);
        assertThat(testInsuranceClaim.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testInsuranceClaim.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingInsuranceClaim() throws Exception {
        int databaseSizeBeforeUpdate = insuranceClaimRepository.findAll().size();
        insuranceClaim.setId(count.incrementAndGet());

        // Create the InsuranceClaim
        InsuranceClaimDTO insuranceClaimDTO = insuranceClaimMapper.toDto(insuranceClaim);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insuranceClaimDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insuranceClaimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceClaim in the database
        List<InsuranceClaim> insuranceClaimList = insuranceClaimRepository.findAll();
        assertThat(insuranceClaimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsuranceClaim() throws Exception {
        int databaseSizeBeforeUpdate = insuranceClaimRepository.findAll().size();
        insuranceClaim.setId(count.incrementAndGet());

        // Create the InsuranceClaim
        InsuranceClaimDTO insuranceClaimDTO = insuranceClaimMapper.toDto(insuranceClaim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insuranceClaimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceClaim in the database
        List<InsuranceClaim> insuranceClaimList = insuranceClaimRepository.findAll();
        assertThat(insuranceClaimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsuranceClaim() throws Exception {
        int databaseSizeBeforeUpdate = insuranceClaimRepository.findAll().size();
        insuranceClaim.setId(count.incrementAndGet());

        // Create the InsuranceClaim
        InsuranceClaimDTO insuranceClaimDTO = insuranceClaimMapper.toDto(insuranceClaim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceClaimMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insuranceClaimDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceClaim in the database
        List<InsuranceClaim> insuranceClaimList = insuranceClaimRepository.findAll();
        assertThat(insuranceClaimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsuranceClaim() throws Exception {
        // Initialize the database
        insuranceClaimRepository.saveAndFlush(insuranceClaim);

        int databaseSizeBeforeDelete = insuranceClaimRepository.findAll().size();

        // Delete the insuranceClaim
        restInsuranceClaimMockMvc
            .perform(delete(ENTITY_API_URL_ID, insuranceClaim.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InsuranceClaim> insuranceClaimList = insuranceClaimRepository.findAll();
        assertThat(insuranceClaimList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
