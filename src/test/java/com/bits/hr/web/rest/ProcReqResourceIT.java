package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.ItemInformation;
import com.bits.hr.domain.ProcReq;
import com.bits.hr.domain.ProcReqMaster;
import com.bits.hr.repository.ProcReqRepository;
import com.bits.hr.service.dto.ProcReqDTO;
import com.bits.hr.service.mapper.ProcReqMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import com.bits.hr.service.procurementRequisition.ProcReqService;
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
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProcReqResourceIT {

    private static final Double DEFAULT_QUANTITY = 1D;
    private static final Double UPDATED_QUANTITY = 2D;

    private static final String DEFAULT_REFERENCE_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_FILE_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/proc-reqs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcReqRepository procReqRepository;

    @Mock
    private ProcReqRepository procReqRepositoryMock;

    @Autowired
    private ProcReqMapper procReqMapper;

    @Mock
    private ProcReqService procReqServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcReqMockMvc;

    private ProcReq procReq;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcReq createEntity(EntityManager em) {
        ProcReq procReq = new ProcReq().quantity(DEFAULT_QUANTITY).referenceFilePath(DEFAULT_REFERENCE_FILE_PATH);
        // Add required entity
        ItemInformation itemInformation;
        if (TestUtil.findAll(em, ItemInformation.class).isEmpty()) {
            itemInformation = ItemInformationResourceIT.createEntity(em);
            em.persist(itemInformation);
            em.flush();
        } else {
            itemInformation = TestUtil.findAll(em, ItemInformation.class).get(0);
        }
        procReq.setItemInformation(itemInformation);
        // Add required entity
        ProcReqMaster procReqMaster;
        if (TestUtil.findAll(em, ProcReqMaster.class).isEmpty()) {
            procReqMaster = ProcReqMasterResourceIT.createEntity(em);
            em.persist(procReqMaster);
            em.flush();
        } else {
            procReqMaster = TestUtil.findAll(em, ProcReqMaster.class).get(0);
        }
        procReq.setProcReqMaster(procReqMaster);
        return procReq;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcReq createUpdatedEntity(EntityManager em) {
        ProcReq procReq = new ProcReq().quantity(UPDATED_QUANTITY).referenceFilePath(UPDATED_REFERENCE_FILE_PATH);
        // Add required entity
        ItemInformation itemInformation;
        if (TestUtil.findAll(em, ItemInformation.class).isEmpty()) {
            itemInformation = ItemInformationResourceIT.createUpdatedEntity(em);
            em.persist(itemInformation);
            em.flush();
        } else {
            itemInformation = TestUtil.findAll(em, ItemInformation.class).get(0);
        }
        procReq.setItemInformation(itemInformation);
        // Add required entity
        ProcReqMaster procReqMaster;
        if (TestUtil.findAll(em, ProcReqMaster.class).isEmpty()) {
            procReqMaster = ProcReqMasterResourceIT.createUpdatedEntity(em);
            em.persist(procReqMaster);
            em.flush();
        } else {
            procReqMaster = TestUtil.findAll(em, ProcReqMaster.class).get(0);
        }
        procReq.setProcReqMaster(procReqMaster);
        return procReq;
    }

    @BeforeEach
    public void initTest() {
        procReq = createEntity(em);
    }

    @Test
    @Transactional
    void createProcReq() throws Exception {
        int databaseSizeBeforeCreate = procReqRepository.findAll().size();
        // Create the ProcReq
        ProcReqDTO procReqDTO = procReqMapper.toDto(procReq);
        restProcReqMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(procReqDTO)))
            .andExpect(status().isCreated());

        // Validate the ProcReq in the database
        List<ProcReq> procReqList = procReqRepository.findAll();
        assertThat(procReqList).hasSize(databaseSizeBeforeCreate + 1);
        ProcReq testProcReq = procReqList.get(procReqList.size() - 1);
        assertThat(testProcReq.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testProcReq.getReferenceFilePath()).isEqualTo(DEFAULT_REFERENCE_FILE_PATH);
    }

    @Test
    @Transactional
    void createProcReqWithExistingId() throws Exception {
        // Create the ProcReq with an existing ID
        procReq.setId(1L);
        ProcReqDTO procReqDTO = procReqMapper.toDto(procReq);

        int databaseSizeBeforeCreate = procReqRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcReqMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(procReqDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProcReq in the database
        List<ProcReq> procReqList = procReqRepository.findAll();
        assertThat(procReqList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = procReqRepository.findAll().size();
        // set the field null
        procReq.setQuantity(null);

        // Create the ProcReq, which fails.
        ProcReqDTO procReqDTO = procReqMapper.toDto(procReq);

        restProcReqMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(procReqDTO)))
            .andExpect(status().isBadRequest());

        List<ProcReq> procReqList = procReqRepository.findAll();
        assertThat(procReqList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProcReqs() throws Exception {
        // Initialize the database
        procReqRepository.saveAndFlush(procReq);

        // Get all the procReqList
        restProcReqMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procReq.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].referenceFilePath").value(hasItem(DEFAULT_REFERENCE_FILE_PATH.toString())));
    }

//    @SuppressWarnings({ "unchecked" })
//    void getAllProcReqsWithEagerRelationshipsIsEnabled() throws Exception {
//     when(procReqServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
//        restProcReqMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());
//
//       verify(procReqServiceMock, times(1)).findAllWithEagerRelationships(any());
//    }

//    @SuppressWarnings({ "unchecked" })
//    void getAllProcReqsWithEagerRelationshipsIsNotEnabled() throws Exception {
//        when(procReqServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
//
//        restProcReqMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
//        verify(procReqRepositoryMock, times(1)).findAll(any(Pageable.class));
//    }

    @Test
    @Transactional
    void getProcReq() throws Exception {
        // Initialize the database
        procReqRepository.saveAndFlush(procReq);

        // Get the procReq
        restProcReqMockMvc
            .perform(get(ENTITY_API_URL_ID, procReq.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(procReq.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.referenceFilePath").value(DEFAULT_REFERENCE_FILE_PATH.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProcReq() throws Exception {
        // Get the procReq
        restProcReqMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcReq() throws Exception {
        // Initialize the database
        procReqRepository.saveAndFlush(procReq);

        int databaseSizeBeforeUpdate = procReqRepository.findAll().size();

        // Update the procReq
        ProcReq updatedProcReq = procReqRepository.findById(procReq.getId()).get();
        // Disconnect from session so that the updates on updatedProcReq are not directly saved in db
        em.detach(updatedProcReq);
        updatedProcReq.quantity(UPDATED_QUANTITY).referenceFilePath(UPDATED_REFERENCE_FILE_PATH);
        ProcReqDTO procReqDTO = procReqMapper.toDto(updatedProcReq);

        restProcReqMockMvc
            .perform(
                put(ENTITY_API_URL_ID, procReqDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(procReqDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProcReq in the database
        List<ProcReq> procReqList = procReqRepository.findAll();
        assertThat(procReqList).hasSize(databaseSizeBeforeUpdate);
        ProcReq testProcReq = procReqList.get(procReqList.size() - 1);
        assertThat(testProcReq.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testProcReq.getReferenceFilePath()).isEqualTo(UPDATED_REFERENCE_FILE_PATH);
    }

    @Test
    @Transactional
    void putNonExistingProcReq() throws Exception {
        int databaseSizeBeforeUpdate = procReqRepository.findAll().size();
        procReq.setId(count.incrementAndGet());

        // Create the ProcReq
        ProcReqDTO procReqDTO = procReqMapper.toDto(procReq);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcReqMockMvc
            .perform(
                put(ENTITY_API_URL_ID, procReqDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(procReqDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcReq in the database
        List<ProcReq> procReqList = procReqRepository.findAll();
        assertThat(procReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcReq() throws Exception {
        int databaseSizeBeforeUpdate = procReqRepository.findAll().size();
        procReq.setId(count.incrementAndGet());

        // Create the ProcReq
        ProcReqDTO procReqDTO = procReqMapper.toDto(procReq);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcReqMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(procReqDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcReq in the database
        List<ProcReq> procReqList = procReqRepository.findAll();
        assertThat(procReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcReq() throws Exception {
        int databaseSizeBeforeUpdate = procReqRepository.findAll().size();
        procReq.setId(count.incrementAndGet());

        // Create the ProcReq
        ProcReqDTO procReqDTO = procReqMapper.toDto(procReq);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcReqMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(procReqDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcReq in the database
        List<ProcReq> procReqList = procReqRepository.findAll();
        assertThat(procReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcReqWithPatch() throws Exception {
        // Initialize the database
        procReqRepository.saveAndFlush(procReq);

        int databaseSizeBeforeUpdate = procReqRepository.findAll().size();

        // Update the procReq using partial update
        ProcReq partialUpdatedProcReq = new ProcReq();
        partialUpdatedProcReq.setId(procReq.getId());

        restProcReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcReq.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcReq))
            )
            .andExpect(status().isOk());

        // Validate the ProcReq in the database
        List<ProcReq> procReqList = procReqRepository.findAll();
        assertThat(procReqList).hasSize(databaseSizeBeforeUpdate);
        ProcReq testProcReq = procReqList.get(procReqList.size() - 1);
        assertThat(testProcReq.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testProcReq.getReferenceFilePath()).isEqualTo(DEFAULT_REFERENCE_FILE_PATH);
    }

    @Test
    @Transactional
    void fullUpdateProcReqWithPatch() throws Exception {
        // Initialize the database
        procReqRepository.saveAndFlush(procReq);

        int databaseSizeBeforeUpdate = procReqRepository.findAll().size();

        // Update the procReq using partial update
        ProcReq partialUpdatedProcReq = new ProcReq();
        partialUpdatedProcReq.setId(procReq.getId());

        partialUpdatedProcReq.quantity(UPDATED_QUANTITY).referenceFilePath(UPDATED_REFERENCE_FILE_PATH);

        restProcReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcReq.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcReq))
            )
            .andExpect(status().isOk());

        // Validate the ProcReq in the database
        List<ProcReq> procReqList = procReqRepository.findAll();
        assertThat(procReqList).hasSize(databaseSizeBeforeUpdate);
        ProcReq testProcReq = procReqList.get(procReqList.size() - 1);
        assertThat(testProcReq.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testProcReq.getReferenceFilePath()).isEqualTo(UPDATED_REFERENCE_FILE_PATH);
    }

    @Test
    @Transactional
    void patchNonExistingProcReq() throws Exception {
        int databaseSizeBeforeUpdate = procReqRepository.findAll().size();
        procReq.setId(count.incrementAndGet());

        // Create the ProcReq
        ProcReqDTO procReqDTO = procReqMapper.toDto(procReq);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, procReqDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(procReqDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcReq in the database
        List<ProcReq> procReqList = procReqRepository.findAll();
        assertThat(procReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcReq() throws Exception {
        int databaseSizeBeforeUpdate = procReqRepository.findAll().size();
        procReq.setId(count.incrementAndGet());

        // Create the ProcReq
        ProcReqDTO procReqDTO = procReqMapper.toDto(procReq);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(procReqDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcReq in the database
        List<ProcReq> procReqList = procReqRepository.findAll();
        assertThat(procReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcReq() throws Exception {
        int databaseSizeBeforeUpdate = procReqRepository.findAll().size();
        procReq.setId(count.incrementAndGet());

        // Create the ProcReq
        ProcReqDTO procReqDTO = procReqMapper.toDto(procReq);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcReqMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(procReqDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcReq in the database
        List<ProcReq> procReqList = procReqRepository.findAll();
        assertThat(procReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcReq() throws Exception {
        // Initialize the database
        procReqRepository.saveAndFlush(procReq);

        int databaseSizeBeforeDelete = procReqRepository.findAll().size();

        // Delete the procReq
        restProcReqMockMvc
            .perform(delete(ENTITY_API_URL_ID, procReq.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcReq> procReqList = procReqRepository.findAll();
        assertThat(procReqList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
