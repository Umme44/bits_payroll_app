package com.bits.hr.service.impl;

import com.bits.hr.domain.IncomeTaxChallan;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.IncomeTaxChallanRepository;
import com.bits.hr.service.IncomeTaxChallanService;
import com.bits.hr.service.dto.IncomeTaxChallanDTO;
import com.bits.hr.service.mapper.IncomeTaxChallanMapper;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IncomeTaxChallan}.
 */
@Service
@Transactional
public class IncomeTaxChallanServiceImpl implements IncomeTaxChallanService {

    private final Logger log = LoggerFactory.getLogger(IncomeTaxChallanServiceImpl.class);

    private final IncomeTaxChallanRepository incomeTaxChallanRepository;

    private final IncomeTaxChallanMapper incomeTaxChallanMapper;

    public IncomeTaxChallanServiceImpl(
        IncomeTaxChallanRepository incomeTaxChallanRepository,
        IncomeTaxChallanMapper incomeTaxChallanMapper
    ) {
        this.incomeTaxChallanRepository = incomeTaxChallanRepository;
        this.incomeTaxChallanMapper = incomeTaxChallanMapper;
    }

    @Override
    public IncomeTaxChallanDTO save(IncomeTaxChallanDTO incomeTaxChallanDTO) {
        log.debug("Request to save IncomeTaxChallan : {}", incomeTaxChallanDTO);
        IncomeTaxChallan incomeTaxChallan = incomeTaxChallanMapper.toEntity(incomeTaxChallanDTO);
        incomeTaxChallan = incomeTaxChallanRepository.save(incomeTaxChallan);
        return incomeTaxChallanMapper.toDto(incomeTaxChallan);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IncomeTaxChallanDTO> findAll(Pageable pageable, Long aitConfigId) {
        log.debug("Request to get all IncomeTaxChallans");

        Page<IncomeTaxChallanDTO> incomeTaxChallans = incomeTaxChallanRepository
            .findAll(pageable, aitConfigId)
            .map(incomeTaxChallanMapper::toDto);
        for (IncomeTaxChallanDTO incomeTaxChallanDTO : incomeTaxChallans) {
            if (incomeTaxChallanDTO.getStartDate() != null) {
                incomeTaxChallanDTO.setStartYear(incomeTaxChallanDTO.getStartDate().getYear());
            }
            if (incomeTaxChallanDTO.getEndDate() != null) {
                incomeTaxChallanDTO.setEndYear(incomeTaxChallanDTO.getEndDate().getYear());
            }
        }

        return incomeTaxChallans;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IncomeTaxChallanDTO> findOne(Long id) {
        log.debug("Request to get IncomeTaxChallan : {}", id);
        return incomeTaxChallanRepository.findById(id).map(incomeTaxChallanMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IncomeTaxChallan : {}", id);
        incomeTaxChallanRepository.deleteById(id);
    }

    @Override
    public List<IncomeTaxChallanDTO> getIncomeTaxChallanListByAitConfigId(long aitConfigId) {
        List<IncomeTaxChallanDTO> incomeTaxChallanDTOList = incomeTaxChallanMapper.toDto(
            incomeTaxChallanRepository.getIncomeTaxChallanListByAitConfig(aitConfigId)
        );
        // sort by year and month
        incomeTaxChallanDTOList.sort((o1, o2) -> {
            if (o1.getYear() - o2.getYear() == 0) {
                return o1.getMonth().ordinal() - o2.getMonth().ordinal();
            } else {
                return o2.getMonth().ordinal() - o1.getMonth().ordinal();
            }
        });
        return incomeTaxChallanDTOList;
    }

    @Override
    public String getIncomeTaxChallanForIncomeTaxStatement(long aitConfigId) {
        List<IncomeTaxChallanDTO> incomeTaxChallanDTOList = getIncomeTaxChallanListByAitConfigId(aitConfigId);
        // sort by year and month
        incomeTaxChallanDTOList.sort(
            new Comparator<IncomeTaxChallanDTO>() {
                @Override
                public int compare(IncomeTaxChallanDTO o1, IncomeTaxChallanDTO o2) {
                    if (o1.getYear() - o2.getYear() == 0) {
                        return o1.getMonth().ordinal() - o2.getMonth().ordinal();
                    } else {
                        return o2.getMonth().ordinal() - o1.getMonth().ordinal();
                    }
                }
            }
        );
        String result = "";
        int count = 1;
        for (IncomeTaxChallanDTO incomeTaxChallanDTO : incomeTaxChallanDTOList) {
            if (
                incomeTaxChallanDTO.getChallanNo() != null &&
                incomeTaxChallanDTO.getYear() != null &&
                incomeTaxChallanDTO.getMonth() != null
            ) {
                result +=
                    incomeTaxChallanDTO.getChallanNo() +
                    " " +
                    getMonthNameAsReadableFormat(incomeTaxChallanDTO.getMonth()) +
                    "-" +
                    incomeTaxChallanDTO.getYear();

                if (incomeTaxChallanDTOList.size() > count) {
                    result += ", ";
                } else {
                    result += " ";
                }
                count++;
            }
        }
        return result;
    }

    private String getMonthNameAsReadableFormat(Month month) {
        return month.toString().toUpperCase().charAt(0) + "" + month.toString().toLowerCase().substring(1);
    }
}
