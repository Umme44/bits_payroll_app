package com.bits.hr.service;

import com.bits.hr.domain.VehicleRequisition;
import com.bits.hr.service.dto.VehicleDTO;
import com.bits.hr.service.dto.VehicleRequisitionDTO;
import com.bits.hr.service.dto.VehicleRequisitionSummaryMasterDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserVehicleRequisitionService {
    VehicleRequisitionDTO save(VehicleRequisitionDTO vehicleRequisitionDTO);

    List<VehicleRequisitionDTO> findAllByEmployeeId();

    VehicleRequisitionDTO findOneById(Long id);

    void delete(Long id);

    List<VehicleRequisitionDTO> getAllPendingRequestBetweenTwoDates(
        LocalDate startDate,
        LocalDate endDate,
        double startTime,
        double endTime
    );

    List<VehicleDTO> getAllRemainingVehicleBetweenTwoDates(LocalDate startDate, LocalDate endDate, double startTime, double endTime);

    List<VehicleRequisitionDTO> getAllApprovedVehicleRequisitionBetweenTwoDates(
        LocalDate startDate,
        LocalDate endDate,
        double startTime,
        double endTime
    );

    VehicleRequisitionSummaryMasterDTO getVehicleRequisitionSummaryByDate(LocalDate date);
}
