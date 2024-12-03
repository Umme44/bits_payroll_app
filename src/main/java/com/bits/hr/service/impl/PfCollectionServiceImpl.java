package com.bits.hr.service.impl;

import com.bits.hr.domain.PfCollection;
import com.bits.hr.domain.enumeration.PfCollectionType;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.service.PfCollectionService;
import com.bits.hr.service.dto.PfCollectionDTO;
import com.bits.hr.service.mapper.PfCollectionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PfCollection}.
 */
@Service
@Transactional
public class PfCollectionServiceImpl implements PfCollectionService {

    private final Logger log = LoggerFactory.getLogger(PfCollectionServiceImpl.class);

    private final PfCollectionRepository pfCollectionRepository;

    private final PfCollectionMapper pfCollectionMapper;

    public PfCollectionServiceImpl(PfCollectionRepository pfCollectionRepository, PfCollectionMapper pfCollectionMapper) {
        this.pfCollectionRepository = pfCollectionRepository;
        this.pfCollectionMapper = pfCollectionMapper;
    }

    @Override
    public PfCollectionDTO create(PfCollectionDTO pfCollectionDTO) {
        log.debug("Request to save PfCollection : {}", pfCollectionDTO);

        Optional<PfCollection> pfCollectionOptional = pfCollectionRepository.getPfCollectionByMonthAndYear(
            pfCollectionDTO.getPfAccountId(),
            pfCollectionDTO.getYear(),
            pfCollectionDTO.getMonth()
        );

        if (pfCollectionOptional.isPresent()) {
            throw new BadRequestAlertException(
                "PF Collections is already exists for selected month and year",
                "PFCollection",
                "entryExists"
            );
        }

        PfCollection pfCollection = pfCollectionMapper.toEntity(pfCollectionDTO);
        pfCollection = pfCollectionRepository.save(pfCollection);
        return pfCollectionMapper.toDto(pfCollection);
    }

    @Override
    public PfCollectionDTO update(PfCollectionDTO pfCollectionDTO) {
        log.debug("Request to save PfCollection : {}", pfCollectionDTO);

        Optional<PfCollection> pfCollectionOptional = pfCollectionRepository.findById(pfCollectionDTO.getId());
        if (!pfCollectionOptional.isPresent()) {
            throw new BadRequestAlertException("PF Collections not found", "PFCollection", "idnull");
        }

        boolean isIdSame = pfCollectionDTO.getId().equals(pfCollectionOptional.get().getId());
        boolean isYearSame = pfCollectionDTO.getYear().equals(pfCollectionOptional.get().getYear());
        boolean isMonthSame = pfCollectionDTO.getMonth().equals(pfCollectionOptional.get().getMonth());
        boolean isCollectionTypeSame = pfCollectionDTO.getCollectionType().equals(pfCollectionOptional.get().getCollectionType());

        boolean isValidPfCollection = isIdSame && isYearSame && isMonthSame && isCollectionTypeSame;

        if (isValidPfCollection == false) {
            throw new BadRequestAlertException("Found duplicate PF collection entry", "PFCollection", "entryExists");
        }

        PfCollection pfCollection = pfCollectionMapper.toEntity(pfCollectionDTO);
        pfCollection = pfCollectionRepository.save(pfCollection);
        return pfCollectionMapper.toDto(pfCollection);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PfCollectionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PfCollections");
        return pfCollectionRepository.findAll(pageable).map(pfCollectionMapper::toDto);
    }

    @Override
    public Page<PfCollectionDTO> findAll(
        Pageable pageable,
        Long pfAccountId,
        Integer year,
        Integer month,
        PfCollectionType collectionType
    ) {
        if (pfAccountId == null && year == null && month == null && collectionType == null) {
            log.debug("Request to get all PfCollections");
            return pfCollectionRepository.findAllSortedByPinYearAndMonth(pageable).map(pfCollectionMapper::toDto);
        } else {
            log.debug(
                "Request to get PfCollections by filtering: pf account id = {}, year = {}, month = {}, collection type = {}",
                pfAccountId,
                year,
                month,
                collectionType
            );
            return pfCollectionRepository.findAll(pageable, pfAccountId, year, month, collectionType).map(pfCollectionMapper::toDto);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PfCollectionDTO> findOne(Long id) {
        log.debug("Request to get PfCollection : {}", id);
        return pfCollectionRepository.findById(id).map(pfCollectionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PfCollection : {}", id);
        pfCollectionRepository.deleteById(id);
    }
}
