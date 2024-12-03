package com.bits.hr.service.impl;

import com.bits.hr.domain.Vehicle;
import com.bits.hr.domain.VehicleRequisition;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.VehicleRepository;
import com.bits.hr.repository.VehicleRequisitionRepository;
import com.bits.hr.service.UserVehicleRequisitionService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.VehicleDTO;
import com.bits.hr.service.dto.VehicleRequisitionDTO;
import com.bits.hr.service.dto.VehicleRequisitionSummary;
import com.bits.hr.service.dto.VehicleRequisitionSummaryMasterDTO;
import com.bits.hr.service.mapper.VehicleMapper;
import com.bits.hr.service.mapper.VehicleRequisitionMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserVehicleRequisitionServiceImpl implements UserVehicleRequisitionService {

    private final Logger log = LoggerFactory.getLogger(VehicleRequisitionServiceImpl.class);
    private final CurrentEmployeeService currentEmployeeService;

    private final VehicleRequisitionMapper vehicleRequisitionMapper;

    private final VehicleRepository vehicleRepository;

    private final VehicleRequisitionRepository vehicleRequisitionRepository;

    private final VehicleMapper vehicleMapper;

    public UserVehicleRequisitionServiceImpl(
        CurrentEmployeeService currentEmployeeService,
        VehicleRequisitionMapper vehicleRequisitionMapper,
        VehicleRepository vehicleRepository,
        VehicleRequisitionRepository vehicleRequisitionRepository,
        VehicleMapper vehicleMapper
    ) {
        this.currentEmployeeService = currentEmployeeService;
        this.vehicleRequisitionMapper = vehicleRequisitionMapper;
        this.vehicleRepository = vehicleRepository;
        this.vehicleRequisitionRepository = vehicleRequisitionRepository;
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public VehicleRequisitionDTO save(VehicleRequisitionDTO vehicleRequisitionDTO) {
        try {
            Long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
            Long currentUserId = currentEmployeeService.getCurrentUserId().get();

            if (vehicleRequisitionDTO.getId() == null) {
                List<VehicleDTO> vehicleDTOList = getAllRemainingVehicleBetweenTwoDates(
                    vehicleRequisitionDTO.getStartDate(),
                    vehicleRequisitionDTO.getEndDate(),
                    vehicleRequisitionDTO.getStartTime(),
                    vehicleRequisitionDTO.getEndTime()
                );

                if (vehicleDTOList.size() == 0) {
                    throw new RuntimeException();
                }

                List<VehicleRequisitionDTO> pendingRequisitionList = getAllPendingRequestBetweenTwoDates(
                    vehicleRequisitionDTO.getStartDate(),
                    vehicleRequisitionDTO.getEndDate(),
                    vehicleRequisitionDTO.getStartTime(),
                    vehicleRequisitionDTO.getEndTime()
                );

                if (vehicleDTOList.size() <= pendingRequisitionList.size()) {
                    throw new RuntimeException();
                }

                if (vehicleDTOList.size() > pendingRequisitionList.size()) {
                    int totalAvailableCapacity = 0;
                    Long totalPassengers = 0L;

                    for (int i = 0; i < vehicleDTOList.size(); i++) {
                        totalAvailableCapacity += vehicleDTOList.get(i).getCapacity();
                    }

                    for (int i = 0; i < pendingRequisitionList.size(); i++) {
                        totalPassengers += pendingRequisitionList.get(i).getTotalNumberOfPassengers();
                    }

                    Long remainingCapacity = (totalAvailableCapacity - totalPassengers);

                    if (remainingCapacity < vehicleRequisitionDTO.getTotalNumberOfPassengers()) {
                        throw new RuntimeException();
                    }
                }

                vehicleRequisitionDTO.setCreatedAt(Instant.now());
                vehicleRequisitionDTO.setCreatedById(currentUserId);
                vehicleRequisitionDTO.setRequesterId(employeeId);
            }

            if (vehicleRequisitionDTO.getId() != null) {
                vehicleRequisitionDTO.setUpdatedAt(Instant.now());
                vehicleRequisitionDTO.setUpdatedById(currentUserId);
            }

            VehicleRequisition vehicleRequisition = vehicleRequisitionMapper.toEntity(vehicleRequisitionDTO);
            VehicleRequisition vehicleRequisitionSaved = vehicleRequisitionRepository.save(vehicleRequisition);
            return vehicleRequisitionMapper.toDto(vehicleRequisitionSaved);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public List<VehicleRequisitionDTO> findAllByEmployeeId() {
        try {
            Long employeeId = currentEmployeeService.getCurrentEmployeeId().get();

            List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAllVehicleRequisitionByEmployeeID(
                employeeId
            );

            return vehicleRequisitionMapper.toDto(vehicleRequisitionList);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public VehicleRequisitionDTO findOneById(Long id) {
        try {
            Long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
            Optional<VehicleRequisition> vehicleRequisition = vehicleRequisitionRepository.findById(id);

            if (vehicleRequisition.isPresent()) {
                return vehicleRequisitionMapper.toDto(vehicleRequisition.get());
            } else {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VehicleRequisition : {}", id);
        try {
            Long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
            Optional<VehicleRequisition> vehicleRequisition = vehicleRequisitionRepository.findById(id);

            if (vehicleRequisition.isPresent()) {
                vehicleRequisitionRepository.deleteById(id);
            } else {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }
    }

    public List<VehicleRequisitionDTO> getAllPendingRequestBetweenTwoDates(
        LocalDate startDate,
        LocalDate endDate,
        double startTime,
        double endTime
    ) {
        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAllPendingVehicleRequisition(
            startDate,
            endDate,
            Status.PENDING
        );
        List<VehicleRequisition> pendingList = new ArrayList<>();

        if (startDate == endDate) {
            List<VehicleRequisition> pendingVehicleRequisitions = vehicleRequisitionList
                .stream()
                .filter(vehicleRequisition ->
                    (vehicleRequisition.getStartTime() <= startTime && vehicleRequisition.getEndTime() >= startTime) ||
                    vehicleRequisition.getStartTime() <= endTime &&
                    vehicleRequisition.getEndTime() >= endTime
                )
                .collect(Collectors.toList());

            return vehicleRequisitionMapper.toDto(pendingVehicleRequisitions);
        } else {
            int requesterStartHour = (int) (Math.floor(startTime));
            int requesterStartMinute = (int) ((startTime - Math.floor(startTime)) * 100);

            int requesterEndHour = (int) (Math.floor(endTime));
            int requesterEndMinute = (int) ((endTime - Math.floor(endTime)) * 100);

            LocalDateTime requesterStartMoment = LocalDateTime.of(
                startDate.getYear(),
                startDate.getMonth(),
                startDate.getDayOfMonth(),
                requesterStartHour,
                requesterStartMinute
            );
            LocalDateTime requesterEndMoment = LocalDateTime.of(
                endDate.getYear(),
                endDate.getMonth(),
                endDate.getDayOfMonth(),
                requesterEndHour,
                requesterEndMinute
            );

            for (int i = 0; i < vehicleRequisitionList.size(); i++) {
                int startHour = (int) (Math.floor(vehicleRequisitionList.get(i).getStartTime()));
                int startMinute = (int) (
                    (vehicleRequisitionList.get(i).getStartTime() - Math.floor(vehicleRequisitionList.get(i).getStartTime())) * 100
                );

                int endHour = (int) (Math.floor(vehicleRequisitionList.get(i).getEndTime()));
                int endMinute = (int) (
                    (vehicleRequisitionList.get(i).getEndTime() - Math.floor(vehicleRequisitionList.get(i).getEndTime())) * 100
                );

                LocalDate startdate = vehicleRequisitionList.get(i).getStartDate();
                LocalDate enddate = vehicleRequisitionList.get(i).getEndDate();

                LocalDateTime startMoment = LocalDateTime.of(
                    startdate.getYear(),
                    startdate.getMonth(),
                    startdate.getDayOfMonth(),
                    startHour,
                    startMinute
                );
                LocalDateTime endMoment = LocalDateTime.of(
                    enddate.getYear(),
                    enddate.getMonth(),
                    enddate.getDayOfMonth(),
                    endHour,
                    endMinute
                );

                if (
                    (
                        (startMoment.isBefore(requesterStartMoment) || startMoment.isEqual(requesterStartMoment)) &&
                        (endMoment.isAfter(requesterStartMoment) || endMoment.isEqual(requesterStartMoment))
                    ) ||
                    (
                        (startMoment.isBefore(requesterEndMoment) || startMoment.isEqual(requesterEndMoment)) &&
                        (endMoment.isAfter(requesterEndMoment) || endMoment.isEqual(requesterEndMoment))
                    )
                ) {
                    pendingList.add(vehicleRequisitionList.get(i));
                }
            }
            return vehicleRequisitionMapper.toDto(pendingList);
        }
    }

    @Override
    public List<VehicleDTO> getAllRemainingVehicleBetweenTwoDates(
        LocalDate startDate,
        LocalDate endDate,
        double startTime,
        double endTime
    ) {
        List<VehicleRequisition> approvedRequisitionListBetweenTwoDate = vehicleRequisitionRepository.findAllApprovedRequisitionBetweenTwoDate(
            startDate,
            endDate,
            Status.APPROVED
        );

        int requesterStartHour = (int) (Math.floor(startTime));
        int requesterStartMinute = (int) ((startTime - Math.floor(startTime)) * 100);

        int requesterEndHour = (int) (Math.floor(endTime));
        int requesterEndMinute = (int) ((endTime - Math.floor(endTime)) * 100);

        LocalDateTime requesterStartMoment = LocalDateTime.of(
            startDate.getYear(),
            startDate.getMonth(),
            startDate.getDayOfMonth(),
            requesterStartHour,
            requesterStartMinute
        );
        LocalDateTime requesterEndMoment = LocalDateTime.of(
            endDate.getYear(),
            endDate.getMonth(),
            endDate.getDayOfMonth(),
            requesterEndHour,
            requesterEndMinute
        );

        List<Vehicle> assignedVehicleBetweenDateAndTime = new ArrayList<>();

        for (int i = 0; i < approvedRequisitionListBetweenTwoDate.size(); i++) {
            LocalDate startdate = approvedRequisitionListBetweenTwoDate.get(i).getStartDate();
            LocalDate enddate = approvedRequisitionListBetweenTwoDate.get(i).getEndDate();

            int startHour = (int) (Math.floor(approvedRequisitionListBetweenTwoDate.get(i).getStartTime()));
            int startMinute = (int) (
                (
                    approvedRequisitionListBetweenTwoDate.get(i).getStartTime() -
                    Math.floor(approvedRequisitionListBetweenTwoDate.get(i).getStartTime())
                ) *
                100
            );

            int endHour = (int) (Math.floor(approvedRequisitionListBetweenTwoDate.get(i).getEndTime()));
            int endMinute = (int) (
                (
                    approvedRequisitionListBetweenTwoDate.get(i).getEndTime() -
                    Math.floor(approvedRequisitionListBetweenTwoDate.get(i).getEndTime())
                ) *
                100
            );

            LocalDateTime startMoment = LocalDateTime.of(
                startdate.getYear(),
                startdate.getMonth(),
                startdate.getDayOfMonth(),
                startHour,
                startMinute
            );
            LocalDateTime endMoment = LocalDateTime.of(enddate.getYear(), enddate.getMonth(), enddate.getDayOfMonth(), endHour, endMinute);

            if (
                (
                    (startMoment.isBefore(requesterStartMoment) || startMoment.isEqual(requesterStartMoment)) &&
                    (endMoment.isAfter(requesterStartMoment) || endMoment.isEqual(requesterStartMoment))
                ) ||
                (
                    (startMoment.isBefore(requesterEndMoment) || startMoment.isEqual(requesterEndMoment)) &&
                    (endMoment.isAfter(requesterEndMoment) || endMoment.isEqual(requesterEndMoment))
                )
            ) {
                assignedVehicleBetweenDateAndTime.add(approvedRequisitionListBetweenTwoDate.get(i).getVehicle());
            }
        }

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        // removing assigned vehicles
        allVehicles.removeAll(assignedVehicleBetweenDateAndTime);
        return vehicleMapper.toDto(allVehicles);
    }

    @Override
    public List<VehicleRequisitionDTO> getAllApprovedVehicleRequisitionBetweenTwoDates(
        LocalDate startDate,
        LocalDate endDate,
        double startTime,
        double endTime
    ) {
        List<VehicleRequisition> approvedRequisitionListBetweenTwoDate = vehicleRequisitionRepository.findAllApprovedRequisitionBetweenTwoDate(
            startDate,
            endDate,
            Status.APPROVED
        );

        int requesterStartHour = (int) (Math.floor(startTime));
        int requesterStartMinute = (int) ((startTime - Math.floor(startTime)) * 100);

        int requesterEndHour = (int) (Math.floor(endTime));
        int requesterEndMinute = (int) ((endTime - Math.floor(endTime)) * 100);

        LocalDateTime requesterStartMoment = LocalDateTime.of(
            startDate.getYear(),
            startDate.getMonth(),
            startDate.getDayOfMonth(),
            requesterStartHour,
            requesterStartMinute
        );
        LocalDateTime requesterEndMoment = LocalDateTime.of(
            endDate.getYear(),
            endDate.getMonth(),
            endDate.getDayOfMonth(),
            requesterEndHour,
            requesterEndMinute
        );

        List<VehicleRequisition> approvedRequisition = new ArrayList<>();

        for (int i = 0; i < approvedRequisitionListBetweenTwoDate.size(); i++) {
            LocalDate startdate = approvedRequisitionListBetweenTwoDate.get(i).getStartDate();
            LocalDate enddate = approvedRequisitionListBetweenTwoDate.get(i).getEndDate();

            int startHour = (int) (Math.floor(approvedRequisitionListBetweenTwoDate.get(i).getStartTime()));
            int startMinute = (int) (
                (
                    approvedRequisitionListBetweenTwoDate.get(i).getStartTime() -
                    Math.floor(approvedRequisitionListBetweenTwoDate.get(i).getStartTime())
                ) *
                100
            );

            int endHour = (int) (Math.floor(approvedRequisitionListBetweenTwoDate.get(i).getEndTime()));
            int endMinute = (int) (
                (
                    approvedRequisitionListBetweenTwoDate.get(i).getEndTime() -
                    Math.floor(approvedRequisitionListBetweenTwoDate.get(i).getEndTime())
                ) *
                100
            );

            LocalDateTime startMoment = LocalDateTime.of(
                startdate.getYear(),
                startdate.getMonth(),
                startdate.getDayOfMonth(),
                startHour,
                startMinute
            );
            LocalDateTime endMoment = LocalDateTime.of(enddate.getYear(), enddate.getMonth(), enddate.getDayOfMonth(), endHour, endMinute);

            if (
                (
                    (startMoment.isBefore(requesterStartMoment) || startMoment.isEqual(requesterStartMoment)) &&
                    (endMoment.isAfter(requesterStartMoment) || endMoment.isEqual(requesterStartMoment))
                ) ||
                (
                    (startMoment.isBefore(requesterEndMoment) || startMoment.isEqual(requesterEndMoment)) &&
                    (endMoment.isAfter(requesterEndMoment) || endMoment.isEqual(requesterEndMoment))
                )
            ) {
                approvedRequisition.add(approvedRequisitionListBetweenTwoDate.get(i));
            }
        }
        return vehicleRequisitionMapper.toDto(approvedRequisition);
    }

    @Override
    public VehicleRequisitionSummaryMasterDTO getVehicleRequisitionSummaryByDate(LocalDate date) {
        List<Double> timeArray = getTimeArray();

        HashMap<Double, VehicleRequisitionSummary> summaryHashMap = new HashMap<>();

        for (int i = 0; i < timeArray.size() - 1; i++) {
            VehicleRequisitionSummary vehicleRequisitionSummary = new VehicleRequisitionSummary();
            vehicleRequisitionSummary.setStartTime(timeArray.get(i));
            vehicleRequisitionSummary.setEndTime(timeArray.get(i + 1));

            summaryHashMap.put(timeArray.get(i), vehicleRequisitionSummary);
        }

        List<VehicleRequisition> vehicleRequisitionList = vehicleRequisitionRepository.findAllRequisitionByDate(date);

        for (int i = 0; i < vehicleRequisitionList.size(); i++) {
            if (vehicleRequisitionList.get(i).getStatus() == Status.PENDING) {
                List<Double> filteredTimeArray = new ArrayList<>();

                if (vehicleRequisitionList.get(i).getStartDate().equals(vehicleRequisitionList.get(i).getEndDate())) {
                    double startTime = vehicleRequisitionList.get(i).getStartTime();
                    double endTime = vehicleRequisitionList.get(i).getEndTime();

                    filteredTimeArray =
                        timeArray.stream().filter(time -> time >= startTime && time <= endTime).collect(Collectors.toList());
                } else if (
                    vehicleRequisitionList.get(i).getStartDate().isBefore(date) && vehicleRequisitionList.get(i).getEndDate().isEqual(date)
                ) {
                    double startTime = timeArray.get(0);
                    double endTime = vehicleRequisitionList.get(i).getEndTime();

                    filteredTimeArray =
                        timeArray.stream().filter(time -> time >= startTime && time <= endTime).collect(Collectors.toList());
                } else if (
                    vehicleRequisitionList.get(i).getStartDate().isEqual(date) && vehicleRequisitionList.get(i).getEndDate().isAfter(date)
                ) {
                    double startTime = vehicleRequisitionList.get(i).getStartTime();
                    double endTime = timeArray.get(timeArray.size() - 2);

                    filteredTimeArray =
                        timeArray.stream().filter(time -> time >= startTime && time <= endTime).collect(Collectors.toList());
                } else if (
                    vehicleRequisitionList.get(i).getStartDate().isBefore(date) && vehicleRequisitionList.get(i).getEndDate().isAfter(date)
                ) {
                    double startTime = timeArray.get(0);
                    double endTime = timeArray.get(timeArray.size() - 2);

                    filteredTimeArray =
                        timeArray.stream().filter(time -> time >= startTime && time <= endTime).collect(Collectors.toList());
                }

                for (int j = 0; j < filteredTimeArray.size(); j++) {
                    VehicleRequisitionSummary vehicleRequisitionSummary = summaryHashMap.get(filteredTimeArray.get(j));
                    vehicleRequisitionSummary.setPendingRequest(vehicleRequisitionSummary.getPendingRequest() + 1);
                    vehicleRequisitionSummary.setPendingTotalCapacity(
                        vehicleRequisitionSummary.getPendingTotalCapacity() + vehicleRequisitionList.get(i).getTotalNumberOfPassengers()
                    );
                }
            }

            if (vehicleRequisitionList.get(i).getStatus() == Status.APPROVED) {
                List<Double> filteredTimeArray = new ArrayList<>();

                if (vehicleRequisitionList.get(i).getStartDate().equals(vehicleRequisitionList.get(i).getEndDate())) {
                    double startTime = vehicleRequisitionList.get(i).getStartTime();
                    double endTime = vehicleRequisitionList.get(i).getEndTime();

                    filteredTimeArray =
                        timeArray.stream().filter(time -> time >= startTime && time <= endTime).collect(Collectors.toList());
                } else if (
                    vehicleRequisitionList.get(i).getStartDate().isBefore(date) && vehicleRequisitionList.get(i).getEndDate().isEqual(date)
                ) {
                    double startTime = timeArray.get(0);
                    double endTime = vehicleRequisitionList.get(i).getEndTime();

                    filteredTimeArray =
                        timeArray.stream().filter(time -> time >= startTime && time <= endTime).collect(Collectors.toList());
                } else if (
                    vehicleRequisitionList.get(i).getStartDate().isEqual(date) && vehicleRequisitionList.get(i).getEndDate().isAfter(date)
                ) {
                    double startTime = vehicleRequisitionList.get(i).getStartTime();
                    double endTime = timeArray.get(timeArray.size() - 2);

                    filteredTimeArray =
                        timeArray.stream().filter(time -> time >= startTime && time <= endTime).collect(Collectors.toList());
                } else if (
                    vehicleRequisitionList.get(i).getStartDate().isBefore(date) && vehicleRequisitionList.get(i).getEndDate().isAfter(date)
                ) {
                    double startTime = timeArray.get(0);
                    double endTime = timeArray.get(timeArray.size() - 2);

                    filteredTimeArray =
                        timeArray.stream().filter(time -> time >= startTime && time <= endTime).collect(Collectors.toList());
                }

                for (int j = 0; j < filteredTimeArray.size(); j++) {
                    VehicleRequisitionSummary vehicleRequisitionSummary = summaryHashMap.get(filteredTimeArray.get(j));
                    vehicleRequisitionSummary.setApprovedRequest(vehicleRequisitionSummary.getApprovedRequest() + 1);
                    vehicleRequisitionSummary.setApprovedPassengers(
                        vehicleRequisitionSummary.getApprovedPassengers() + vehicleRequisitionList.get(i).getTotalNumberOfPassengers()
                    );

                    VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicleRequisitionList.get(i).getVehicle());
                    List<VehicleDTO> vehicleDTOList = vehicleRequisitionSummary.getApprovedVehicle();

                    if (vehicleDTOList != null) {
                        vehicleDTOList.add(vehicleDTO);
                        vehicleRequisitionSummary.setApprovedVehicle(vehicleDTOList);
                    } else {
                        List<VehicleDTO> vehicleDTOList1 = Arrays.asList(vehicleDTO);
                        vehicleRequisitionSummary.setApprovedVehicle(vehicleDTOList1);
                    }
                }
            }
        }

        List<VehicleRequisitionSummary> vehicleRequisitionSummaryList = new ArrayList<VehicleRequisitionSummary>(summaryHashMap.values());
        List<VehicleRequisitionSummary> vehicleRequisitionSummaryListSorted = vehicleRequisitionSummaryList
            .stream()
            .sorted(Comparator.comparing(VehicleRequisitionSummary::getStartTime))
            .collect(Collectors.toList());

        List<Vehicle> vehicleList = vehicleRepository.findAll();

        for (int i = 0; i < vehicleRequisitionSummaryListSorted.size(); i++) {
            List<VehicleDTO> allVehicles = vehicleMapper.toDto(vehicleList);
            List<VehicleDTO> assignedVehicle = vehicleRequisitionSummaryListSorted.get(i).getApprovedVehicle();

            if (assignedVehicle != null) {
                allVehicles.removeAll(assignedVehicle);
            }

            long totalAvailableCapacity = 0;

            for (int j = 0; j < allVehicles.size(); j++) {
                totalAvailableCapacity += allVehicles.get(j).getCapacity();
            }

            if (allVehicles.size() == vehicleRequisitionSummaryListSorted.get(i).getPendingRequest()) {
                vehicleRequisitionSummaryListSorted.get(i).setRemainingTotalCapacity(0);
                vehicleRequisitionSummaryListSorted.get(i).setRemainingCar(0);
            } else if (allVehicles.size() > vehicleRequisitionSummaryListSorted.get(i).getPendingRequest()) {
                long pendingRequestTotalPassengers = vehicleRequisitionSummaryListSorted.get(i).getPendingTotalCapacity();
                long availableCapacity = 0;
                long availableCars = 0;

                for (int j = 0; j < allVehicles.size(); j++) {
                    if (pendingRequestTotalPassengers > 0) {
                        pendingRequestTotalPassengers -= allVehicles.get(j).getCapacity();
                    } else {
                        availableCapacity += allVehicles.get(j).getCapacity();
                        availableCars++;
                    }
                }
                vehicleRequisitionSummaryListSorted.get(i).setRemainingTotalCapacity(availableCapacity);
                vehicleRequisitionSummaryListSorted.get(i).setRemainingCar(availableCars);
            }
        }

        VehicleRequisitionSummaryMasterDTO masterDTO = new VehicleRequisitionSummaryMasterDTO();
        masterDTO.setDate(date);
        masterDTO.setVehicleRequisitionSummaryList(vehicleRequisitionSummaryListSorted);

        return masterDTO;
    }

    //    public boolean checkAvailableCars(VehicleRequisitionDTO vehicleRequisitionDTO){
    //        List<VehicleRequisitionDTO> pendingVehicleRequisitionDTOList = getAllPendingRequestBetweenTwoDates(vehicleRequisitionDTO.getStartDate(), vehicleRequisitionDTO.getEndDate());
    //        pendingVehicleRequisitionDTOList.add(vehicleRequisitionDTO);
    //
    ////        List<Vehicle> availableVehicle = vehicleRequisitionRepository
    //        return false;
    //    }

    public static List<Double> getTimeArray() {
        List<Double> timeArray = new ArrayList<>();

        timeArray.add(0.0);
        timeArray.add(0.3);
        timeArray.add(1.0);
        timeArray.add(1.3);
        timeArray.add(2.0);
        timeArray.add(2.3);
        timeArray.add(3.0);
        timeArray.add(3.3);
        timeArray.add(4.0);
        timeArray.add(4.3);
        timeArray.add(5.0);
        timeArray.add(5.3);
        timeArray.add(6.0);
        timeArray.add(6.3);
        timeArray.add(7.0);
        timeArray.add(7.3);
        timeArray.add(8.0);
        timeArray.add(8.3);
        timeArray.add(9.0);
        timeArray.add(9.3);
        timeArray.add(10.0);
        timeArray.add(10.3);
        timeArray.add(11.0);
        timeArray.add(11.3);
        timeArray.add(12.0);

        timeArray.add(12.3);
        timeArray.add(13.0);
        timeArray.add(13.3);
        timeArray.add(14.0);
        timeArray.add(14.3);
        timeArray.add(15.0);
        timeArray.add(15.3);
        timeArray.add(16.0);
        timeArray.add(16.3);
        timeArray.add(17.0);
        timeArray.add(17.3);
        timeArray.add(18.0);
        timeArray.add(18.3);
        timeArray.add(19.0);
        timeArray.add(19.3);
        timeArray.add(20.0);
        timeArray.add(20.3);
        timeArray.add(21.0);
        timeArray.add(21.3);
        timeArray.add(22.0);
        timeArray.add(22.3);
        timeArray.add(23.0);
        timeArray.add(23.3);

        return timeArray;
    }
}
