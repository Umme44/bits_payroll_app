package com.bits.hr.service.impl;

import com.bits.hr.domain.BankBranch;
import com.bits.hr.repository.BankBranchRepository;
import com.bits.hr.service.BankBranchService;
import com.bits.hr.service.dto.BankBranchDTO;
import com.bits.hr.service.mapper.BankBranchMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BankBranch}.
 */
@Service
@Transactional
public class BankBranchServiceImpl implements BankBranchService {

    private final Logger log = LoggerFactory.getLogger(BankBranchServiceImpl.class);

    private final BankBranchRepository bankBranchRepository;

    private final BankBranchMapper bankBranchMapper;

    public BankBranchServiceImpl(BankBranchRepository bankBranchRepository, BankBranchMapper bankBranchMapper) {
        this.bankBranchRepository = bankBranchRepository;
        this.bankBranchMapper = bankBranchMapper;
    }

    @Override
    public BankBranchDTO save(BankBranchDTO bankBranchDTO) {
        log.debug("Request to save BankBranch : {}", bankBranchDTO);
        BankBranch bankBranch = bankBranchMapper.toEntity(bankBranchDTO);
        bankBranch = bankBranchRepository.save(bankBranch);
        return bankBranchMapper.toDto(bankBranch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankBranchDTO> findAll() {
        log.debug("Request to get all BankBranches");
        return bankBranchRepository.findAll().stream().map(bankBranchMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BankBranchDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BankBranches");
        return bankBranchRepository.findAll(pageable)
            .map(bankBranchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BankBranchDTO> findOne(Long id) {
        log.debug("Request to get BankBranch : {}", id);
        return bankBranchRepository.findById(id).map(bankBranchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BankBranch : {}", id);
        bankBranchRepository.deleteById(id);
    }
}
