package com.bits.hr.service.impl;

import com.bits.hr.domain.Holidays;
import com.bits.hr.repository.HolidaysRepository;
import com.bits.hr.service.HolidaysService;
import com.bits.hr.service.LeaveManagement.LeaveDaysCount.LeaveDaysCalculationService;
import com.bits.hr.service.dto.HolidaysDTO;
import com.bits.hr.service.mapper.HolidaysMapper;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Holidays}.
 */
@Service
public class HolidaysServiceImpl implements HolidaysService {

    private final Logger log = LoggerFactory.getLogger(HolidaysServiceImpl.class);

    private final HolidaysRepository holidaysRepository;

    private final HolidaysMapper holidaysMapper;

    private final LeaveDaysCalculationService leaveDaysCalculationService;

    public HolidaysServiceImpl(
        HolidaysRepository holidaysRepository,
        HolidaysMapper holidaysMapper,
        LeaveDaysCalculationService leaveDaysCalculationService
    ) {
        this.holidaysRepository = holidaysRepository;
        this.holidaysMapper = holidaysMapper;
        this.leaveDaysCalculationService = leaveDaysCalculationService;
    }

    @Override
    public HolidaysDTO create(HolidaysDTO holidaysDTO) {
        log.debug("Request to save Holidays : {}", holidaysDTO);
        Holidays holidays = holidaysMapper.toEntity(holidaysDTO);
        holidays = holidaysRepository.save(holidays);

        leaveDaysCalculationService.holidayReCalculation(holidays.getStartDate(), holidays.getEndDate());
        return holidaysMapper.toDto(holidays);
    }

    @Override
    public HolidaysDTO update(HolidaysDTO holidaysDTO) {
        log.debug("Request to save Holidays : {}", holidaysDTO);
        Holidays holidays = holidaysMapper.toEntity(holidaysDTO);
        holidays = holidaysRepository.save(holidays);
        leaveDaysCalculationService.holidayReCalculation(holidays.getStartDate(), holidays.getEndDate());
        return holidaysMapper.toDto(holidays);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HolidaysDTO> findAll() {
        log.debug("Request to get all Holidays");
        holidaysRepository.findAll().sort(Comparator.comparing(Holidays::getStartDate).reversed());
        return holidaysRepository.findAll().stream().map(holidaysMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HolidaysDTO> findAllOfAYear(int year) {
        log.debug("Request to get all Holidays");
        LocalDate startDate = LocalDate.of(year, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(year, Month.DECEMBER, 31);
        List<Holidays> holidaysEntityList = holidaysRepository.findAllHolidaysBetweenDates(startDate, endDate);
        List<HolidaysDTO> holidaysDTOList = new ArrayList<>();

        /* add days of week in dto */
        for (Holidays holidays : holidaysEntityList) {
            String allDayNames = "";
            for (LocalDate date = holidays.getStartDate(); holidays.getEndDate().plusDays(1).isAfter(date); date = date.plusDays(1)) {
                String day = String.valueOf(date.getDayOfWeek());
                day = convertToTitleCaseIteratingChars(day);
                if (!holidays.getEndDate().isEqual(date)) {
                    allDayNames += day + ", ";
                } else {
                    allDayNames += day;
                }
            }
            HolidaysDTO holidaysDTO = holidaysMapper.toDto(holidays);
            holidaysDTO.setAllDayNames(allDayNames);
            holidaysDTOList.add(holidaysDTO);
        }

        return holidaysDTOList;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HolidaysDTO> findOne(Long id) {
        log.debug("Request to get Holidays : {}", id);

        return holidaysRepository.findById(id).map(holidaysMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Holidays : {}", id);

        Optional<Holidays> holidays = holidaysRepository.findById(id);
        holidaysRepository.deleteById(id);

        leaveDaysCalculationService.holidayReCalculation(holidays.get().getStartDate(), holidays.get().getEndDate());
    }

    public static String convertToTitleCaseIteratingChars(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }
}
