package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.RoomRequisition;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.RoomRequisitionRepository;
import com.bits.hr.service.RoomRequisitionService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.dto.RoomRequisitionBookingDTO;
import com.bits.hr.service.dto.RoomRequisitionDTO;
import com.bits.hr.service.dto.RoomRequisitionSummaryDTO;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import com.bits.hr.service.mapper.RoomRequisitionMapper;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RoomRequisition}.
 */
@Service
@Transactional
@AllArgsConstructor
public class RoomRequisitionServiceImpl implements RoomRequisitionService {

    private final Logger log = LoggerFactory.getLogger(RoomRequisitionServiceImpl.class);

    private final RoomRequisitionRepository roomRequisitionRepository;

    private final RoomRequisitionMapper roomRequisitionMapper;

    private final EmployeeMinimalMapper employeeMinimalMapper;

    private final CurrentEmployeeService employeeService;

    @Override
    public RoomRequisitionDTO save(RoomRequisitionDTO roomRequisitionDTO) {
        log.debug("Request to save RoomRequisition : {}", roomRequisitionDTO);

        List<Long> employeeMinimalDTOS = roomRequisitionDTO.getEmployeeParticipantList();
        if (employeeMinimalDTOS != null && employeeMinimalDTOS.size() > 0) {
            String participantIdList = employeeMinimalDTOS.stream().map(String::valueOf).collect(Collectors.joining(","));
            roomRequisitionDTO.setParticipantList(participantIdList);
        }
        if (
            roomRequisitionDTO.getEmployeeOptionalParticipantList() != null &&
            roomRequisitionDTO.getEmployeeOptionalParticipantList().size() > 0
        ) {
            String optionalParticipantIdList = roomRequisitionDTO
                .getEmployeeOptionalParticipantList()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
            roomRequisitionDTO.setOptionalParticipantList(optionalParticipantIdList);
        }

        if (roomRequisitionDTO.isIsFullDay() != null && roomRequisitionDTO.isIsFullDay()) {
            roomRequisitionDTO.setStartTime(0.0);
            roomRequisitionDTO.setEndTime(23.59);
        }

        RoomRequisition roomRequisition = roomRequisitionMapper.toEntity(roomRequisitionDTO);
        roomRequisition = roomRequisitionRepository.save(roomRequisition);
        return roomRequisitionMapper.toDto(roomRequisition);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoomRequisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RoomRequisitions");
        return roomRequisitionRepository.findAll(pageable).map(roomRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomRequisitionDTO> findOne(Long id) {
        log.debug("Request to get RoomRequisition : {}", id);

        Optional<RoomRequisition> roomRequisition = roomRequisitionRepository.findById(id);

        if (roomRequisition.isPresent()) {
            RoomRequisitionDTO responseRoomRequisitionDTO = roomRequisitionMapper.toDto(roomRequisition.get());

            if (roomRequisition.get().getParticipantList() != null) {
                String[] employeeList = roomRequisition.get().getParticipantList().split(",");
                List<Long> participantList = Arrays.stream(employeeList).map(Long::parseLong).collect(Collectors.toList());
                responseRoomRequisitionDTO.setEmployeeParticipantList(participantList);
            }

            if (roomRequisition.get().getOptionalParticipantList() != null) {
                String[] employeeOptionalList = roomRequisition.get().getOptionalParticipantList().split(",");
                List<Long> optionalParticipantList = Arrays.stream(employeeOptionalList).map(Long::parseLong).collect(Collectors.toList());
                responseRoomRequisitionDTO.setEmployeeOptionalParticipantList(optionalParticipantList);
            }

            return Optional.of(responseRoomRequisitionDTO);
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RoomRequisition : {}", id);
        roomRequisitionRepository.deleteById(id);
    }

    @Override
    public List<RoomRequisitionDTO> getAllRoomRequisitionById() {
        Long id = employeeService.getCurrentEmployeeId().get();
        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.getAllRoomRequisitionById(id);
        return roomRequisitionMapper.toDto(roomRequisitionList);
    }

    @Override
    public boolean checkRoomIsBookedV2(RoomRequisitionDTO roomRequisitionDTO) {
        Long room_Id = roomRequisitionDTO.getRoomId();
        LocalDate bookingStartDate = roomRequisitionDTO.getBookingStartDate();
        LocalDate bookingEndDate = roomRequisitionDTO.getBookingEndDate();
        Double startTime = roomRequisitionDTO.getStartTime();
        Double endTime = roomRequisitionDTO.getEndTime();
        Long requesterId = roomRequisitionDTO.getRequesterId();

        /* LocalDateTime startDateTime = getDateTime(
            booking_date,
            start_Hour,
            start_Minutes);

        LocalDateTime endDateTime = getDateTime(
            booking_date,
            end_Hour,
            end_Minutes);*/

        boolean isRoomBooked = false;
        List<RoomRequisition> roomRequisitionList = new ArrayList<>();

        // check for update with requesterId
        if (requesterId != null) {
            roomRequisitionList = roomRequisitionRepository.checkRoomIsBookedForUpdate(room_Id, requesterId, bookingStartDate);
        } else {
            roomRequisitionList = roomRequisitionRepository.checkRoomIsBookedV2(room_Id, bookingStartDate, bookingEndDate);
        }

        for (RoomRequisition room : roomRequisitionList) {
            /* if (((startDateTime.compareTo(getDateTime(booking_date, room.getStartHour(), room.getStartMinutes())) >= 0
                    && startDateTime.compareTo(getDateTime(booking_date, room.getEndHour(), room.getEndMinutes())) < 0)
                    || (endDateTime.compareTo(getDateTime(booking_date, room.getStartHour(), room.getStartMinutes())) > 0
                    && endDateTime.compareTo(getDateTime(booking_date, room.getEndHour(), room.getEndMinutes())) <= 0))) {
                    isRoomBooked = (room.getRequester().getId() != requesterId);
            }

            if (((getDateTime(booking_date, room.getStartHour(), room.getStartMinutes()).compareTo(startDateTime) >= 0
                    && getDateTime(booking_date, room.getStartHour(), room.getStartMinutes()).compareTo(endDateTime) < 0)
                    || (getDateTime(booking_date, room.getEndHour(), room.getEndMinutes()).compareTo(startDateTime) > 0
                    && getDateTime(booking_date, room.getEndHour(), room.getEndMinutes()).compareTo(endDateTime) <= 0))) {
                isRoomBooked = true;
            }*/

            Double database_startTime = room.getStartTime();
            Double database_endTime = room.getEndTime();
            LocalDate database_bookingStartDate = room.getBookingStartDate();
            LocalDate database_bookingEndDate = room.getBookingEndDate();

            if (bookingStartDate.isAfter(database_bookingStartDate) && bookingStartDate.isBefore(database_bookingEndDate)) {
                isRoomBooked = true;
                break;
            } else if (bookingEndDate.isAfter(database_bookingStartDate) && bookingStartDate.isBefore(database_bookingEndDate)) {
                isRoomBooked = true;
                break;
            } else if (bookingStartDate.isBefore(database_bookingStartDate) && bookingEndDate.isAfter(database_bookingEndDate)) {
                isRoomBooked = true;
                break;
            } else if (bookingStartDate.isAfter(database_bookingStartDate) && bookingEndDate.isBefore(database_bookingEndDate)) {
                isRoomBooked = true;
                break;
            } else if (bookingStartDate.isEqual(database_bookingStartDate) && bookingEndDate.isEqual(database_bookingEndDate)) {
                if (Objects.equals(startTime, database_startTime) && Objects.equals(endTime, database_endTime)) {
                    isRoomBooked = true;
                    break;
                }

                if (startTime > database_startTime && startTime < database_endTime) {
                    isRoomBooked = true;
                    break;
                }

                if (startTime.equals(database_startTime) && endTime < database_endTime) {
                    isRoomBooked = true;
                    break;
                }

                if (startTime.equals(database_startTime) && endTime > database_endTime) {
                    isRoomBooked = true;
                    break;
                }
                if (startTime < database_startTime && endTime > database_endTime) {
                    isRoomBooked = true;
                    break;
                }

                if (startTime < database_startTime && endTime.equals(database_endTime)) {
                    isRoomBooked = true;
                    break;
                }

                if (endTime > database_startTime && endTime < database_endTime) {
                    isRoomBooked = true;
                    break;
                }
            } else if (bookingStartDate.isEqual(database_bookingStartDate) && bookingEndDate.isEqual(database_bookingEndDate)) {
                if (startTime > database_startTime || endTime > database_startTime) {
                    isRoomBooked = true;
                    break;
                }
            } else if (bookingStartDate.isEqual(database_bookingEndDate)) {
                if (startTime < database_endTime || endTime < database_endTime) {
                    isRoomBooked = true;
                    break;
                }
            } else if (bookingStartDate.isEqual(database_bookingStartDate) && bookingEndDate.isEqual(database_bookingStartDate)) {
                if (startTime > database_startTime || endTime > database_startTime) {
                    isRoomBooked = true;
                    break;
                }
            } else if (bookingStartDate.isBefore(database_bookingStartDate) && bookingEndDate.isEqual(database_bookingStartDate)) {
                if (startTime > database_startTime || endTime > database_startTime) {
                    isRoomBooked = true;
                    break;
                }
            }

            /*  Integer database_start_Hour = room.getStartHour();
            Integer database_end_Hour = room.getEndHour();
            Integer database_start_Minutes = room.getStartMinutes();
            Integer database_end_Minutes = room.getEndMinutes();


            // check start Hour equals Database end hour then check Database start minutes equals  start minutes
            if(start_Hour == database_start_Hour  && start_Minutes == database_start_Minutes
                && end_Hour == database_end_Hour && end_Minutes == database_end_Minutes){
                    isRoomBooked = true;
                    break;
            }

            // check start Hour equals Database start hour then check Database start minutes greater than start minutes
            if(start_Hour == database_start_Hour){
                if(start_Minutes < database_start_Minutes){
                    isRoomBooked = true;
                    break;
                }
            }

            // check start Hour equals Database start hour && end Hour equals Database end hour
            // check start minutes greater than database start minutes
            // and end minutes greater than  Database end minutes or  end minutes less than  Database end minutes
            if(start_Hour == database_start_Hour && end_Hour == database_end_Hour){
                if(start_Minutes >= database_start_Minutes && end_Minutes >= database_end_Minutes || end_Minutes <= database_end_Minutes){
                    isRoomBooked = true;
                    break;
                }
            }

            // check start Hour less than Database start hour & Database End hour ,
            // then  end hour equals database start hour , then check end minutes greater than database start minutes
            if(start_Hour < database_start_Hour  && start_Hour < database_end_Hour){
                if(end_Hour == database_start_Hour  &&  end_Minutes > database_start_Minutes){
                    isRoomBooked = true;
                    break;
                }
            }

            // check start Hour less than Database start hour & Database End hour ,
            // then  end hour greater than database start hour.
            if(start_Hour < database_start_Hour  && start_Hour < database_end_Hour){
                if(end_Hour > database_start_Hour ){
                    isRoomBooked = true;
                    break;
                }
            }

            // check end Hour less than Database end hour & Database End hour ,
            //check end Hour greater than Database start hour.
            if(start_Hour < database_start_Hour && start_Hour < database_end_Hour){
                if(end_Hour > database_start_Hour && end_Hour < database_end_Hour ){
                    isRoomBooked = true;
                    break;
                }
            }

            // check start Hour less than Database start hour & start hour less than Database End hour ,
            //check start minutes greater than Database start minutes.
            if(start_Hour == database_start_Hour && start_Hour < database_end_Hour){
                if(start_Minutes > database_start_Minutes){
                    isRoomBooked = true;
                    break;
                }
            }

            // check start Hour less than Database start hour & Database End hour ,
            // then  end hour equals database end hour , then check end minutes greater than database end minutes
            if(start_Hour < database_start_Hour  && start_Hour < database_end_Hour){
                if(end_Hour == database_end_Hour  &&  end_Minutes > database_end_Minutes){
                    isRoomBooked = true;
                    break;
                }
            }

            // check start Hour equals Database start hour & Database End hour ,
            // and   start hour less than database end hour
            if(start_Hour == database_start_Hour  && end_Hour == database_start_Hour
                && start_Hour < database_end_Hour){
                isRoomBooked = true;
                break;
            }

            // check start Hour equals Database start hour,
            // and   end hour less than database end hour
            if(start_Hour == database_start_Hour  && end_Hour < database_end_Hour){
                isRoomBooked = true;
                break;
            }
        }*/
            return isRoomBooked;
        }
        return isRoomBooked;
    }

    @Override
    public List<EmployeeMinimalDTO> getAllRoomRequester() {
        Set<Employee> roomRequesterSet = roomRequisitionRepository
            .getRoomRequisitionByAll()
            .stream()
            .map(RoomRequisition::getRequester)
            .collect(Collectors.toSet());
        return employeeMinimalMapper.toDto(new ArrayList<>(roomRequesterSet));
    }

    @Override
    public List<RoomRequisitionDTO> getRoomRequisitionByIdAndStatus(Long requesterId, String status) {
        /*  LocalDate startDate = LocalDate.parse(bookingStartDate);
        LocalDate endDate = LocalDate.parse(bookingEndDate);*/
        Status status1 = null;
        if (Objects.equals(status, "APPROVED")) {
            status1 = Status.APPROVED;
        } else if (Objects.equals(status, "NOT_APPROVED")) {
            status1 = Status.NOT_APPROVED;
        } else {
            status1 = Status.PENDING;
        }
        if (requesterId == -1L) {
            //return roomRequisitionMapper.toDto(roomRequisitionRepository.getRoomRequisitionByIdAndStatusV2(status1,bookingStartDate,bookingEndDate,startTime,endTime));
            return roomRequisitionMapper.toDto(roomRequisitionRepository.getRoomRequisitionByIdAndStatusV2(status1));
        }
        List<RoomRequisition> roomRequisitionList = roomRequisitionRepository.getRoomRequisitionByIdAndStatus(requesterId, status1);
        return roomRequisitionMapper.toDto(roomRequisitionList);
    }

    @Override
    public List<RoomRequisitionDTO> getRoomRequisitionByUserIdAndStatus(Long requesterId) {
        if (requesterId == -1L) {
            return roomRequisitionMapper.toDto(roomRequisitionRepository.findAll());
        } else {
            return roomRequisitionMapper.toDto(roomRequisitionRepository.getAllRoomRequisitionById(requesterId));
        }
    }

    @Override
    public RoomRequisitionBookingDTO getAllRoomRequisitionForThatDate(Long roomId, LocalDate calendarDate) {
        List<RoomRequisitionDTO> roomRequisitionDTOList = roomRequisitionMapper.toDto(
            roomRequisitionRepository.getAllRoomRequisitionForThatDate(roomId, calendarDate, Status.APPROVED)
        );

        List<RoomRequisitionDTO> filterRoomRequisitionDTOList = new ArrayList<>();

        List<Double> timeArray = getTimeArray();

        HashMap<Double, RoomRequisitionSummaryDTO> summaryHashMap = new HashMap<>();

        List<Double> filteredTimeArray = new ArrayList<>();
        for (int i = 0; i < timeArray.size() - 1; i++) {
            RoomRequisitionSummaryDTO roomRequisitionSummaryDTO = new RoomRequisitionSummaryDTO();
            roomRequisitionSummaryDTO.setSummaryStartTime(timeArray.get(i));
            roomRequisitionSummaryDTO.setSummaryEndTime(timeArray.get(i + 1));
            roomRequisitionSummaryDTO.setSummaryBookingStatus(false);

            summaryHashMap.put(timeArray.get(i), roomRequisitionSummaryDTO);
            //filteredTimeArray = timeArray.stream().filter(time -> time>= startTime && time<= endTime ).collect(Collectors.toList());

        }

        for (RoomRequisitionDTO filterRoomRequisitionDTO : roomRequisitionDTOList) {
            LocalDate filterStartDate = filterRoomRequisitionDTO.getBookingStartDate();
            LocalDate filterEndDate = filterRoomRequisitionDTO.getBookingEndDate();
            Double filterStartTime = filterRoomRequisitionDTO.getStartTime();
            Double filterEndTime = filterRoomRequisitionDTO.getEndTime();

            if (calendarDate.isAfter(filterStartDate) && calendarDate.isBefore(filterEndDate)) {
                filterRoomRequisitionDTO.setCalendarBookingStartTime(0.0);
                filterRoomRequisitionDTO.setCalendarBookingEndTime(23.59);
                filterRoomRequisitionDTOList.add(filterRoomRequisitionDTO);

                filteredTimeArray = timeArray.stream().filter(time -> time >= 0.0 && time <= 23.59).collect(Collectors.toList());
            } else if (calendarDate.isAfter(filterStartDate) && calendarDate.isEqual(filterEndDate)) {
                filterRoomRequisitionDTO.setCalendarBookingStartTime(0.0);
                filterRoomRequisitionDTO.setCalendarBookingEndTime(filterEndTime);
                filterRoomRequisitionDTOList.add(filterRoomRequisitionDTO);

                filteredTimeArray = timeArray.stream().filter(time -> time >= 0.0 && time <= filterEndTime).collect(Collectors.toList());
            } else if (calendarDate.isEqual(filterStartDate) && calendarDate.isBefore(filterEndDate)) {
                filterRoomRequisitionDTO.setCalendarBookingStartTime(filterStartTime);
                filterRoomRequisitionDTO.setCalendarBookingEndTime(23.59);
                filterRoomRequisitionDTOList.add(filterRoomRequisitionDTO);

                filteredTimeArray =
                    timeArray.stream().filter(time -> time >= filterStartTime && time <= 23.59).collect(Collectors.toList());
            } else if (calendarDate.isEqual(filterStartDate) && calendarDate.isEqual(filterEndDate)) {
                filterRoomRequisitionDTO.setCalendarBookingStartTime(filterStartTime);
                filterRoomRequisitionDTO.setCalendarBookingEndTime(filterEndTime);
                filterRoomRequisitionDTOList.add(filterRoomRequisitionDTO);
                /* double startTime = filterRoomRequisitionDTO.getStartTime();
                double endTime = filterRoomRequisitionDTO.getEndTime();*/
                filteredTimeArray =
                    timeArray.stream().filter(time -> time >= filterStartTime && time <= filterEndTime).collect(Collectors.toList());
            }
            for (int i = 0; i < filteredTimeArray.size(); i++) {
                RoomRequisitionSummaryDTO roomRequisitionSummaryDTO = summaryHashMap.get(filteredTimeArray.get(i));
                roomRequisitionSummaryDTO.setSummaryStartTime(roomRequisitionSummaryDTO.getSummaryStartTime());
                roomRequisitionSummaryDTO.setSummaryEndTime(roomRequisitionSummaryDTO.getSummaryEndTime());
                roomRequisitionSummaryDTO.setSummaryStartDate(filterStartDate);
                roomRequisitionSummaryDTO.setSummaryEndTime(filterEndTime);
                roomRequisitionSummaryDTO.setSummaryBookingStatus(true);
            }
        }

        List<RoomRequisitionSummaryDTO> roomRequisitionSummaryDTOList = new ArrayList<RoomRequisitionSummaryDTO>(summaryHashMap.values());
        List<RoomRequisitionSummaryDTO> roomRequisitionSummaryDTOListSorted = roomRequisitionSummaryDTOList
            .stream()
            .sorted(Comparator.comparing(RoomRequisitionSummaryDTO::getSummaryStartTime))
            .collect(Collectors.toList());

        RoomRequisitionBookingDTO roomRequisitionBookingDTO = new RoomRequisitionBookingDTO();
        roomRequisitionBookingDTO.setCurrentDate(calendarDate);
        roomRequisitionBookingDTO.setRoomRequisitionSummaryDTOList(roomRequisitionSummaryDTOListSorted);

        return roomRequisitionBookingDTO;
    }

    private List<Double> getTimeArray() {
        List<Double> timeArray = new ArrayList<>();
        timeArray.add(0.00);
        timeArray.add(0.30);
        timeArray.add(1.00);
        timeArray.add(1.30);
        timeArray.add(2.00);
        timeArray.add(2.30);
        timeArray.add(3.00);
        timeArray.add(3.30);
        timeArray.add(4.00);
        timeArray.add(4.30);
        timeArray.add(5.00);
        timeArray.add(5.30);
        timeArray.add(6.00);
        timeArray.add(6.30);
        timeArray.add(7.00);
        timeArray.add(7.30);
        timeArray.add(8.00);
        timeArray.add(8.30);
        timeArray.add(9.00);
        timeArray.add(9.30);
        timeArray.add(10.00);
        timeArray.add(10.30);
        timeArray.add(11.00);
        timeArray.add(11.30);
        timeArray.add(12.00);
        timeArray.add(12.30);
        timeArray.add(13.00);
        timeArray.add(13.30);
        timeArray.add(14.00);
        timeArray.add(14.30);
        timeArray.add(15.00);
        timeArray.add(15.30);
        timeArray.add(16.00);
        timeArray.add(16.30);
        timeArray.add(17.00);
        timeArray.add(17.30);
        timeArray.add(18.00);
        timeArray.add(18.30);
        timeArray.add(19.00);
        timeArray.add(19.30);
        timeArray.add(20.00);
        timeArray.add(20.30);
        timeArray.add(21.00);
        timeArray.add(21.30);
        timeArray.add(22.00);
        timeArray.add(22.30);
        timeArray.add(23.00);
        timeArray.add(23.30);
        timeArray.add(23.59);
        return timeArray;
    }
}
