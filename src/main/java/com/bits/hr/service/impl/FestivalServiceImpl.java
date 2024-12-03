package com.bits.hr.service.impl;

import com.bits.hr.domain.Festival;
import com.bits.hr.domain.FestivalBonusDetails;
import com.bits.hr.repository.FestivalBonusDetailsRepository;
import com.bits.hr.repository.FestivalRepository;
import com.bits.hr.service.FestivalService;
import com.bits.hr.service.dto.FestivalDTO;
import com.bits.hr.service.festivalBonus.FBService;
import com.bits.hr.service.mapper.FestivalMapper;
import com.bits.hr.service.search.FilterDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Festival}.
 */
@Service
@Transactional
public class FestivalServiceImpl implements FestivalService {

    private final Logger log = LoggerFactory.getLogger(FestivalServiceImpl.class);

    private final FestivalRepository festivalRepository;

    @Autowired
    private FestivalBonusDetailsRepository festivalBonusDetailsRepository;

    private final FestivalMapper festivalMapper;

    private final FBService fbService;

    public FestivalServiceImpl(FestivalRepository festivalRepository, FestivalMapper festivalMapper, FBService fbService) {
        this.festivalRepository = festivalRepository;
        this.festivalMapper = festivalMapper;
        this.fbService = fbService;
    }

    @Override
    public FestivalDTO save(FestivalDTO festivalDTO) {
        log.debug("Request to save Festival : {}", festivalDTO);
        Festival festival = festivalMapper.toEntity(festivalDTO);
        festival = festivalRepository.save(festival);

        if (
            festivalDTO.getBonusDisbursementDate().equals(LocalDate.now()) ||
            festivalDTO.getBonusDisbursementDate().isAfter(LocalDate.now())
        ) {
            fbService.generateAndSave(festival);
        }
        return festivalMapper.toDto(festival);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FestivalDTO> findAll(FilterDto filterDto, Pageable pageable) {
        if (filterDto.getSearchText() == null) {
            filterDto.setSearchText("");
        }
        String searchText = "%" + filterDto.getSearchText().trim().toLowerCase() + "%";
        log.debug("Request to get all Festivals");
        Page<FestivalDTO> festivals = festivalRepository.findAll(searchText, pageable).map(festivalMapper::toDto);
        festivals
            .stream()
            .forEach(festivalDTO -> {
                int total = festivalBonusDetailsRepository.getTotalBonus(festivalDTO.getId());
                festivalDTO.setNumberOfBonus(total);
            });
        return festivals;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FestivalDTO> findAll() {
        log.debug("Request to get all Festivals");
        log.debug("Request to get all Festivals");
        List<Festival> festivals = festivalRepository.findAll();
        List<FestivalDTO> festivalDTOList = festivalMapper.toDto(festivals);
        festivalDTOList
            .stream()
            .forEach(festivalDTO -> {
                int total = festivalBonusDetailsRepository.getTotalBonus(festivalDTO.getId());
                festivalDTO.setNumberOfBonus(total);
            });
        return festivalDTOList;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FestivalDTO> findOne(Long id) {
        log.debug("Request to get Festival : {}", id);
        return festivalRepository.findById(id).map(festivalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Festival : {}", id);
        List<FestivalBonusDetails> festivalBonusDetails = festivalBonusDetailsRepository.findByFestivalId(id);
        try {
            festivalBonusDetailsRepository.deleteAll(festivalBonusDetails);
            festivalRepository.deleteById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
