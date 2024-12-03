package com.bits.hr.service.impl;

import com.bits.hr.domain.Department;
import com.bits.hr.domain.ItemInformation;
import com.bits.hr.repository.ItemInformationRepository;
import com.bits.hr.service.ItemInformationService;
import com.bits.hr.service.dto.DepartmentItemsDTO;
import com.bits.hr.service.dto.ItemInformationDTO;
import com.bits.hr.service.mapper.ItemInformationMapper;
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
 * Service Implementation for managing {@link ItemInformation}.
 */
@Service
@Transactional
public class ItemInformationServiceImpl implements ItemInformationService {

    private final Logger log = LoggerFactory.getLogger(ItemInformationServiceImpl.class);

    private final ItemInformationRepository itemInformationRepository;

    private final ItemInformationMapper itemInformationMapper;

    public ItemInformationServiceImpl(ItemInformationRepository itemInformationRepository, ItemInformationMapper itemInformationMapper) {
        this.itemInformationRepository = itemInformationRepository;
        this.itemInformationMapper = itemInformationMapper;
    }

    @Override
    public ItemInformationDTO save(ItemInformationDTO itemInformationDTO) {
        log.debug("Request to save ItemInformation : {}", itemInformationDTO);
        ItemInformation itemInformation = itemInformationMapper.toEntity(itemInformationDTO);
        itemInformation = itemInformationRepository.save(itemInformation);
        return itemInformationMapper.toDto(itemInformation);
    }

    @Override
    public ItemInformationDTO create(ItemInformationDTO itemInformationDTO) {
        itemInformationDTO.setCode(generateNextItemCode());
        return save(itemInformationDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemInformationDTO> findAll(Long departmentId, Pageable pageable) {
        log.debug("Request to get all ItemInformations");
        return itemInformationRepository.findAll(departmentId, pageable).map(itemInformationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemInformationDTO> findOne(Long id) {
        log.debug("Request to get ItemInformation : {}", id);
        return itemInformationRepository.findById(id).map(itemInformationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        // todo: need decision for if item want to delete already used in procurement requisition
        // srs: The procurement officer should be able to cancel or save the created item information
        log.debug("Request to delete ItemInformation : {}", id);
        itemInformationRepository.deleteById(id);
    }

    @Override
    public String generateNextItemCode() {
        // it should be unique
        // example => ITM001, ITM002; Here ITM is prefix, and it comes from configuration (Config Table)
        // no need to configure item prefix
        // todo: what will happen when code will be deleted
        //itemInformationRepository.findByCodePrefix(""); // issue: if prefix is configurable; then code will be dependent to prefix
        long totalItem = itemInformationRepository.count();
        long nextItem = totalItem + 1;
        String code;
        if (nextItem < 10) {
            code = "ITM00" + nextItem;
        } else if (nextItem < 100) {
            code = "ITM0" + nextItem;
        } else {
            code = "ITM" + nextItem;
        }
        while (!isCodeUnique(null, code)) {
            // todo: how many zeros will add
            if (nextItem < 10) {
                code = "ITM00" + ++nextItem;
            } else if (nextItem < 100) {
                code = "ITM0" + ++nextItem;
            } else {
                code = "ITM" + ++nextItem;
            }
        }
        return code;
    }

    @Override
    public boolean isCodeUnique(Long id, String code) {
        Optional<ItemInformation> itemInformation = itemInformationRepository.findByCode(code);
        if (!itemInformation.isPresent()) {
            return true;
        } else {
            if (id == null) {
                return false;
            } else {
                return itemInformation.get().getId().equals(id);
            }
        }
    }

    @Override
    public List<ItemInformationDTO> findByDepartmentId(long departmentId) {
        return itemInformationMapper.toDto(itemInformationRepository.findByDepartmentId(departmentId));
    }

    @Override
    public List<DepartmentItemsDTO> findDepartmentsAndItemsMapping() {
        List<Department> departmentList = itemInformationRepository.findAllDepartments();
        List<DepartmentItemsDTO> departmentItemsDTOList = new ArrayList<>();
        for (Department department : departmentList) {
            DepartmentItemsDTO departmentItemsDTO = new DepartmentItemsDTO();
            departmentItemsDTO.setDepartmentId(department.getId());
            departmentItemsDTO.setDepartmentName(department.getDepartmentName());
            departmentItemsDTO.setTotalItems(itemInformationRepository.countByDepartmentId(department.getId()));

            departmentItemsDTOList.add(departmentItemsDTO);
        }
        return departmentItemsDTOList;
    }
}
