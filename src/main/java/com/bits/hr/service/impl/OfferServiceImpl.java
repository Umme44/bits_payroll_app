package com.bits.hr.service.impl;

import com.bits.hr.domain.Offer;
import com.bits.hr.repository.OfferRepository;
import com.bits.hr.service.OfferService;
import com.bits.hr.service.dto.OfferDTO;
import com.bits.hr.service.mapper.OfferMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Offer}.
 */
@Service
@Transactional
public class OfferServiceImpl implements OfferService {

    private final Logger log = LoggerFactory.getLogger(OfferServiceImpl.class);

    private final OfferRepository offerRepository;

    private final OfferMapper offerMapper;

    public OfferServiceImpl(OfferRepository offerRepository, OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    @Override
    public OfferDTO save(OfferDTO offerDTO) {
        log.debug("Request to save Offer : {}", offerDTO);
        Offer offer = offerMapper.toEntity(offerDTO);
        offer = offerRepository.save(offer);
        return offerMapper.toDto(offer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OfferDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Offers");
        return offerRepository.findAll(pageable).map(offerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OfferDTO> findOne(Long id) {
        log.debug("Request to get Offer : {}", id);
        return offerRepository.findById(id).map(offerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Offer : {}", id);
        offerRepository.deleteById(id);
    }

    @Override
    public List<OfferDTO> recentOffer() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Offer> recentOffers = offerRepository.getRecentOffer(pageable);
        return offerMapper.toDto(recentOffers);
    }
}
