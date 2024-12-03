package com.bits.hr.service.impl;

import com.bits.hr.domain.FestivalBonusDetails;
import com.bits.hr.domain.HoldFbDisbursement;
import com.bits.hr.repository.FestivalBonusDetailsRepository;
import com.bits.hr.repository.HoldFbDisbursementRepository;
import com.bits.hr.service.FestivalBonusDetailsService;
import com.bits.hr.service.HoldFbDisbursementService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FestivalBonusDetailsDTO;
import com.bits.hr.service.dto.HoldFbDisbursementApprovalDTO;
import com.bits.hr.service.dto.HoldFbDisbursementDTO;
import com.bits.hr.service.mapper.HoldFbDisbursementMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HoldFbDisbursement}.
 */
@Service
@Transactional
public class HoldFbDisbursementServiceImpl implements HoldFbDisbursementService {

    private final Logger log = LoggerFactory.getLogger(HoldFbDisbursementServiceImpl.class);

    private final HoldFbDisbursementRepository holdFbDisbursementRepository;

    private final HoldFbDisbursementMapper holdFbDisbursementMapper;

    private final FestivalBonusDetailsRepository festivalBonusDetailsRepository;

    private final FestivalBonusDetailsService festivalBonusDetailsService;

    private final CurrentEmployeeService currentEmployeeService;

    public HoldFbDisbursementServiceImpl(
        HoldFbDisbursementRepository holdFbDisbursementRepository,
        HoldFbDisbursementMapper holdFbDisbursementMapper,
        FestivalBonusDetailsRepository festivalBonusDetailsRepository,
        FestivalBonusDetailsService festivalBonusDetailsService,
        CurrentEmployeeService currentEmployeeService
    ) {
        this.holdFbDisbursementRepository = holdFbDisbursementRepository;
        this.holdFbDisbursementMapper = holdFbDisbursementMapper;
        this.festivalBonusDetailsRepository = festivalBonusDetailsRepository;
        this.festivalBonusDetailsService = festivalBonusDetailsService;
        this.currentEmployeeService = currentEmployeeService;
    }

    @Override
    public HoldFbDisbursementDTO save(HoldFbDisbursementDTO holdFbDisbursementDTO) {
        log.debug("Request to save HoldFbDisbursement : {}", holdFbDisbursementDTO);

        Optional<FestivalBonusDetails> fbDetails = festivalBonusDetailsRepository.findById(
            holdFbDisbursementDTO.getFestivalBonusDetailId()
        );

        FestivalBonusDetails festivalBonusDetails = fbDetails.get();

        if (festivalBonusDetails.getIsHold()) {
            festivalBonusDetails.setIsHold(false);
            festivalBonusDetailsRepository.save(festivalBonusDetails);
        }

        HoldFbDisbursement holdFbDisbursement = holdFbDisbursementMapper.toEntity(holdFbDisbursementDTO);
        holdFbDisbursement = holdFbDisbursementRepository.save(holdFbDisbursement);
        return holdFbDisbursementMapper.toDto(holdFbDisbursement);
    }

    @Override
    public List<HoldFbDisbursementDTO> disburseHoldFestivalBonus(HoldFbDisbursementApprovalDTO holdFbDisbursementApprovalDTO) {
        log.debug("Request to save HoldFbDisbursement : {}", holdFbDisbursementApprovalDTO);

        List<Long> list = holdFbDisbursementApprovalDTO.getListOfId();

        LocalDate disbursedAt = holdFbDisbursementApprovalDTO.getDisbursedAt();
        String remarks = holdFbDisbursementApprovalDTO.getRemarks();
        Long disbursedById = currentEmployeeService.getCurrentUserId().get();

        List<HoldFbDisbursementDTO> dtoList = new ArrayList<>();

        for (long id : list) {
            Optional<FestivalBonusDetailsDTO> result = festivalBonusDetailsService.findOne(id);
            if (result.isPresent()) {
                HoldFbDisbursementDTO holdFbDisbursementDTO = new HoldFbDisbursementDTO();
                holdFbDisbursementDTO.setBonusAmount(result.get().getBonusAmount());

                holdFbDisbursementDTO.setDisbursedAt(disbursedAt);
                holdFbDisbursementDTO.setDisbursedById(disbursedById);
                holdFbDisbursementDTO.setRemarks(remarks);
                holdFbDisbursementDTO.setFestivalBonusDetailId(id);

                Optional<FestivalBonusDetails> fbDetails = festivalBonusDetailsRepository.findById(
                    holdFbDisbursementDTO.getFestivalBonusDetailId()
                );

                FestivalBonusDetails festivalBonusDetails = fbDetails.get();

                if (festivalBonusDetails.getIsHold()) {
                    festivalBonusDetails.setIsHold(false);
                }

                HoldFbDisbursement holdFbDisbursement = holdFbDisbursementMapper.toEntity(holdFbDisbursementDTO);

                // A new hold fb disbursement can not exist on the database. That is why id should be null.
                if (holdFbDisbursement.getId() != null) {
                    continue;
                }

                holdFbDisbursement = holdFbDisbursementRepository.save(holdFbDisbursement);
                festivalBonusDetailsRepository.save(festivalBonusDetails);

                HoldFbDisbursementDTO mappedDTO = holdFbDisbursementMapper.toDto(holdFbDisbursement);

                dtoList.add(mappedDTO);
            }
        }
        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HoldFbDisbursementDTO> findAll(String searchText, Pageable pageable) {
        log.debug("Request to get all HoldFbDisbursements");
        if (searchText == null) {
            searchText = "";
        }
        return holdFbDisbursementRepository.findAllDisbursedFB(searchText, pageable).map(holdFbDisbursementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HoldFbDisbursementDTO> findOne(Long id) {
        log.debug("Request to get HoldFbDisbursement : {}", id);
        return holdFbDisbursementRepository.findById(id).map(holdFbDisbursementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HoldFbDisbursement : {}", id);
        holdFbDisbursementRepository.deleteById(id);
    }
}
